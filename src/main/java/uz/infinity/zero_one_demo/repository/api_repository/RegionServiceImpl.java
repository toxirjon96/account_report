package uz.infinity.zero_one_demo.repository.api_repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.infinity.zero_one_demo.dto.RegionDto;
import uz.infinity.zero_one_demo.entity.Region;
import uz.infinity.zero_one_demo.exception.GlobalExceptionHandler;
import uz.infinity.zero_one_demo.helper.Message;
import uz.infinity.zero_one_demo.helper.ResponseHelper;
import uz.infinity.zero_one_demo.repository.ICrudRepository;
import uz.infinity.zero_one_demo.repository.IInitRepository;
import uz.infinity.zero_one_demo.repository.entity_repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements
        IInitRepository,
        ICrudRepository<RegionDto> {
    private final RegionRepository regionRepository;
    private final ResponseHelper responseHelper;
    @Autowired
    @Qualifier("organizationServiceImpl")
    private IInitRepository organizationInitRepository;
    @Autowired
    @Qualifier("employeeServiceImpl")
    private IInitRepository employeeInitRepository;

    private final List<RegionDto> regionDtoList = List.of(
            new RegionDto("АНДИЖОН ВИЛОЯТИ"),
            new RegionDto("БУХОРО ВИЛОЯТИ"),
            new RegionDto("ФАРҒОНА ВИЛОЯТИ"),
            new RegionDto("НАМАНГАН ВИЛОЯТИ"),
            new RegionDto("ЖИЗЗАХ ВИЛОЯТИ"),
            new RegionDto("СИРДАРЁ ВИЛОЯТИ"),
            new RegionDto("ҚАШҚАДАРЁ ВИЛОЯТИ"),
            new RegionDto("СУРХАНДАРЁ ВИЛОЯТИ"),
            new RegionDto("САМАРҚАНД ВИЛОЯТИ"),
            new RegionDto("ҚОРАҚАЛПОҒИСТОН РЕСПУБЛИКАСИ"),
            new RegionDto("ТОШКЕНТ ВИЛОЯТИ"),
            new RegionDto("ХОРАЗМ ВИЛОЯТИ"),
            new RegionDto("НАВОИЙ ВИЛОЯТИ"),
            new RegionDto("ТОШКЕНТ ШАХРИ")
    );

    @Override
    public ResponseEntity<?> create(RegionDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat yaratishda xatolik.",
                            "Ошибка создания региона.",
                            "Error creating region."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getRegionName() == null || request.getRegionName().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat nomi bo'sh bo'lmasligi kerak.",
                            "Название региона не должно быть пустым.",
                            "The name of the region must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (regionRepository.existsByName(request.getRegionName())) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Bunday nomga ega viloyat mavjud.",
                            "Есть регион с таким названием.",
                            "There is a region with that name."),
                    HttpStatus.BAD_REQUEST
            );
        }
        try {
            regionRepository.save(new Region(request.getRegionName()));
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat muvaffaqiyatli yaratildi.",
                            "Регион успешно создан.",
                            "The region was successfully created."),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat yaratishda xatolik.",
                            "Ошибка создания региона.",
                            "Error creating region."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<?> update(String id, RegionDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat taxrirlashda xatolik.",
                            "Ошибка редактирования региона.",
                            "Error editing region."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (id == null || id.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat idisi bo'sh bo'lmasligi kerak.",
                            "Ид региона не должен быть пустым.",
                            "The region id should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<Region> regionOptional = regionRepository.findById(id);
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
        Region region = regionOptional.get();
        if (request.getRegionName() != null &&
                !request.getRegionName().isEmpty() &&
                !region.getName().equals(request.getRegionName())) {
            if (regionRepository.existsByName(request.getRegionName())) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Bunday nomga ega viloyat mavjud.",
                                "Есть регион с таким названием.",
                                "There is a region with that name."),
                        HttpStatus.BAD_REQUEST
                );
            }
            region.setName(request.getRegionName());
        }
        try {
            regionRepository.save(region);
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat muvaffaqiyatli taxrilandi.",
                            "Регион успешно отредактирован.",
                            "The region was successfully edited."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat taxrirlashda xatolik.",
                            "Ошибка редактирования региона.",
                            "Error editing region."),
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
                            "Viloyat idisi bo'sh bo'lmasligi kerak.",
                            "Ид региона не должен быть пустым.",
                            "The region id should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<Region> regionOptional = regionRepository.findById(id);
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
        try {
            Region region = regionOptional.get();
            regionRepository.delete(region);
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat muvaffaqiyatli o'chirildi.",
                            "Регион успешно удален.",
                            "Region deleted successfully."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Viloyat o'chirishda xatolik.",
                            "Ошибка удаления региона.",
                            "Error deleting region."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostConstruct
    @Override
    public void init() {
        for (RegionDto regionDto : regionDtoList) {
            if (!regionRepository.existsByName(regionDto.getRegionName())) {
                regionRepository.save(new Region(regionDto.getRegionName()));
            }
        }
        organizationInitRepository.init();
        employeeInitRepository.init();
    }
}
