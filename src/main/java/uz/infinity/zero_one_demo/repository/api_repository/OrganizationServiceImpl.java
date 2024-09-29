package uz.infinity.zero_one_demo.repository.api_repository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.infinity.zero_one_demo.dto.OrganizationDto;
import uz.infinity.zero_one_demo.entity.Organization;
import uz.infinity.zero_one_demo.entity.Region;
import uz.infinity.zero_one_demo.exception.GlobalExceptionHandler;
import uz.infinity.zero_one_demo.helper.Message;
import uz.infinity.zero_one_demo.helper.ResponseHelper;
import uz.infinity.zero_one_demo.repository.ICrudRepository;
import uz.infinity.zero_one_demo.repository.IInitRepository;
import uz.infinity.zero_one_demo.repository.entity_repository.OrganizationRepository;
import uz.infinity.zero_one_demo.repository.entity_repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements
        ICrudRepository<OrganizationDto>,
        IInitRepository {
    private final OrganizationRepository organizationRepository;
    private final RegionRepository regionRepository;
    private final ResponseHelper responseHelper;

    @Override
    public ResponseEntity<?> create(OrganizationDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot yaratishda xatolik.",
                            "Ошибка при создании организации.",
                            "Error creating organization."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Organizatsiya nomi bo'sh bo'lmasligi kerak.",
                            "Название организации не должно быть пустым.",
                            "The name of the organization must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getRegionId() == null || request.getRegionId().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Organizatsiya joylashgan viloyat idisi bo'sh bo'lmasligi kerak.",
                            "Идентификатор региона организации не должен быть пустым.",
                            "The region id of the organization should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<Region> regionOptional = regionRepository.findById(request.getRegionId());
        if (regionOptional.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Bunday idiga ega bo'lgan viloyat mavjud emas.",
                            "Нет такого региона.",
                            "There is no such region."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (organizationRepository.existsByNameAndRegion_Id(request.getName(), request.getRegionId())) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Bunday tashkilot keltirilgan viloyatda mavjud.",
                            "Такая организация существует в данном регионе.",
                            "Such an organization exists in the given region."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<Organization> organizationOptional = Optional.empty();
        if (request.getParentId() != null && !request.getParentId().isEmpty()) {
            organizationOptional = organizationRepository.findById(request.getParentId());
            if (organizationOptional.isEmpty()) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Bunday idiga ega bo'lgan ta'sischi tashkilot mavjud emas.",
                                "Не существует учредительной организации с такой идентичностью.",
                                "There is no constituent organization with such an identity."),
                        HttpStatus.BAD_REQUEST
                );
            }
            if (organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                    request.getName(),
                    request.getRegionId(),
                    organizationOptional.get().getId())) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Bunday ta'sischiga ega tashkilot keltirilgan viloyatda mavjud.",
                                "Организация с таким учредителем существует в указанном регионе.",
                                "An organization with such a founder exists in the listed region."),
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        try {
            if (organizationOptional.isPresent()) {
                organizationRepository.save(new Organization(
                        request.getName(),
                        regionOptional.get(),
                        organizationOptional.get()
                ));
            } else {
                organizationRepository.save(new Organization(
                        request.getName(),
                        regionOptional.get())
                );
            }
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot muvaffaqiyatli yaratildi.",
                            "Организация успешно создана",
                            "The organization was created successfully"),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot yaratishda xatolik.",
                            "Ошибка при создании организации.",
                            "Error creating organization."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<?> update(String id, OrganizationDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot taxrirlashda xatolik.",
                            "Ошибка редактирования организации.",
                            "Error editing organization."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (id == null || id.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot idisi bo'sh bo'lmasligi kerak.",
                            "Идентификатор организации не должен быть пустым.",
                            "The organization id should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Bunday idiga ega bo'lgan tashkilot mavjud emas.",
                            "Организации с таким идентификатором не существует.",
                            "There is no organization with such an id."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Organization organization = organizationOptional.get();
        if (request.getName() != null &&
                !request.getName().isEmpty() &&
                !organization.getName().equals(request.getName())) {
            organization.setName(request.getName());
        }
        if (request.getRegionId() != null &&
                !request.getRegionId().isEmpty() &&
                !organization.getRegion().getId().equals(request.getRegionId())) {
            Optional<Region> regionOptional = regionRepository.findById(request.getRegionId());
            if (regionOptional.isEmpty()) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Bunday idiga ega bo'lgan viloyat mavjud emas.",
                                "Нет такого региона.",
                                "There is no such region."),
                        HttpStatus.BAD_REQUEST
                );
            }
            organization.setRegion(regionOptional.get());
        }
        if (request.getParentId() != null &&
                organization.getParent() != null &&
                !request.getParentId().isEmpty() &&
                !organization.getParent().getId().equals(request.getParentId())) {
            Optional<Organization> parentOptional = organizationRepository
                    .findById(request.getParentId());
            if (parentOptional.isEmpty()) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Bunday idiga ega bo'lgan ta'sischi tashkilot mavjud emas.",
                                "Не существует учредительной организации с такой идентичностью.",
                                "There is no constituent organization with such an identity."),
                        HttpStatus.BAD_REQUEST
                );
            }
            organization.setParent(parentOptional.get());
        }
        try {
            if (organization.getParent() == null) {
                if (organizationRepository.existsByNameAndRegion_Id(
                        organization.getName(),
                        request.getRegionId())) {
                    return responseHelper.prepareResponse(
                            new ArrayList<>(),
                            new Message(
                                    "Bunday tashkilot keltirilgan viloyatda mavjud.",
                                    "Такая организация существует в данном регионе.",
                                    "Such an organization exists in the given region."),
                            HttpStatus.BAD_REQUEST
                    );
                }
            }else{
                if (organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                        organization.getName(),
                        organization.getRegion().getId(),
                        organization.getParent().getId())) {
                    return responseHelper.prepareResponse(
                            new ArrayList<>(),
                            new Message(
                                    "Bunday ta'sischiga ega tashkilot keltirilgan viloyatda mavjud.",
                                    "Организация с таким учредителем существует в указанном регионе.",
                                    "An organization with such a founder exists in the listed region."),
                            HttpStatus.BAD_REQUEST
                    );
                }
            }
            organizationRepository.save(organization);
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot muvaffaqiyatli taxrilandi.",
                            "Организация успешно изменена.",
                            "Organization successfully edited."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot taxrirlashda xatolik.",
                            "Ошибка редактирования организации.",
                            "Error editing organization."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<?> delete(String id) {
        if (id == null || id.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot idisi bo'sh bo'lmasligi kerak.",
                            "Идентификатор организации не должен быть пустым.",
                            "The organization id should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Bunday idiga ega bo'lgan tashkilot mavjud emas.",
                            "Организации с таким идентификатором не существует.",
                            "There is no organization with such an id."),
                    HttpStatus.BAD_REQUEST
            );
        }
        try {
            Organization organization = organizationOptional.get();
            List<Organization> subOrganizations = organizationRepository.findByParent_Id(organization.getId());
            organizationRepository.deleteAll(subOrganizations);
            organizationRepository.delete(organization);
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot muvaffaqiyatli o'chirildi.",
                            "Организация успешно удалена.",
                            "The organization was successfully deleted."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot o'chirishda xatolik.",
                            "Ошибка удаления организации.",
                            "Error deleting organization."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public void init() {
        Optional<Region> regionOptionalTosh = regionRepository.findByName("ТОШКЕНТ ШАХРИ");
        Optional<Region> regionOptionalAnd = regionRepository.findByName("АНДИЖОН ВИЛОЯТИ");
        Optional<Region> regionOptionalJiz = regionRepository.findByName("ЖИЗЗАХ ВИЛОЯТИ");
        Optional<Region> regionOptionalXor = regionRepository.findByName("ХОРАЗМ ВИЛОЯТИ");

        Organization zerOne;
        Organization greenWhiteSolutions;
        Organization beProEducation;
        Organization gita;
        if (regionOptionalTosh.isPresent()) {
            if (!organizationRepository.existsByNameAndRegion_Id("ZERO ONE",
                    regionOptionalTosh.get().getId())) {
                zerOne = organizationRepository.save(new Organization("ZERO ONE",
                        regionOptionalTosh.get()));
                if (!organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                        "ZERO ONE (SUB)",
                        regionOptionalTosh.get().getId(),
                        zerOne.getId())) {
                    organizationRepository.save(new Organization(
                            "ZERO ONE (SUB)",
                            regionOptionalTosh.get(),
                            zerOne));
                }
                if (regionOptionalAnd.isPresent()) {
                    if (!organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                            "ZERO ONE ANDIJON",
                            regionOptionalAnd.get().getId(),
                            zerOne.getId())) {
                        organizationRepository.save(new Organization(
                                "ZERO ONE ANDIJON",
                                regionOptionalAnd.get(),
                                zerOne));
                    }
                }
            }
        }
        if (regionOptionalAnd.isPresent()) {
            if (!organizationRepository.existsByNameAndRegion_Id(
                    "GREEN WHITE SOLUTIONS",
                    regionOptionalAnd.get().getId())) {
                greenWhiteSolutions = organizationRepository.save(new Organization(
                        "GREEN WHITE SOLUTIONS",
                        regionOptionalAnd.get()));
                if (!organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                        "GWS (SUB)",
                        regionOptionalAnd.get().getId(),
                        greenWhiteSolutions.getId())) {
                    organizationRepository.save(new Organization(
                            "GWS (SUB)",
                            regionOptionalAnd.get(),
                            greenWhiteSolutions));
                }
                if (regionOptionalXor.isPresent()) {
                    if (!organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                            "GWS XORAZM",
                            regionOptionalAnd.get().getId(),
                            greenWhiteSolutions.getId())) {
                        organizationRepository.save(new Organization(
                                "GWS XORAZM",
                                regionOptionalAnd.get(),
                                greenWhiteSolutions));
                    }
                }
            }
        }
        if (regionOptionalJiz.isPresent()) {
            if (!organizationRepository.existsByNameAndRegion_Id("BE PRO EDUCATION",
                    regionOptionalJiz.get().getId())) {
                beProEducation = organizationRepository.save(new Organization("BE PRO EDUCATION",
                        regionOptionalJiz.get()));
                if (!organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                        "BPE (SUB)",
                        regionOptionalJiz.get().getId(),
                        beProEducation.getId())) {
                    organizationRepository.save(new Organization(
                            "PBE (SUB)",
                            regionOptionalJiz.get(),
                            beProEducation));
                }
                if (regionOptionalTosh.isPresent()) {
                    if (!organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                            "BPE TOSHKENT",
                            regionOptionalTosh.get().getId(),
                            beProEducation.getId())) {
                        organizationRepository.save(new Organization(
                                "BPE TOSHKENT",
                                regionOptionalTosh.get(),
                                beProEducation));
                    }
                }
            }
        }
        if (regionOptionalXor.isPresent()) {
            if (!organizationRepository.existsByNameAndRegion_Id("GITA",
                    regionOptionalXor.get().getId())) {
                gita = organizationRepository.save(new Organization("GITA",
                        regionOptionalXor.get()));
                if (!organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                        "GITA (SUB)",
                        regionOptionalXor.get().getId(),
                        gita.getId())) {
                    organizationRepository.save(new Organization(
                            "GITA (SUB)",
                            regionOptionalXor.get(),
                            gita));
                }
                if (regionOptionalJiz.isPresent()) {
                    if (!organizationRepository.existsByNameAndRegion_IdAndParent_Id(
                            "GITA JIZZAH",
                            regionOptionalJiz.get().getId(),
                            gita.getId())) {
                        organizationRepository.save(new Organization(
                                "GITA JIZZAH",
                                regionOptionalJiz.get(),
                                gita));
                    }
                }
            }
        }
    }
}
