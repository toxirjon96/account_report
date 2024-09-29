package uz.infinity.zero_one_demo.repository.api_repository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.infinity.zero_one_demo.constant.CalculationType;
import uz.infinity.zero_one_demo.dto.CalculationTableDto;
import uz.infinity.zero_one_demo.dto.MonthRateDto;
import uz.infinity.zero_one_demo.dto.OrganizationWorkerDto;
import uz.infinity.zero_one_demo.dto.RegionWorkerInfoDto;
import uz.infinity.zero_one_demo.entity.CalculationTable;
import uz.infinity.zero_one_demo.entity.Employee;
import uz.infinity.zero_one_demo.entity.Organization;
import uz.infinity.zero_one_demo.entity.Region;
import uz.infinity.zero_one_demo.exception.GlobalExceptionHandler;
import uz.infinity.zero_one_demo.helper.Message;
import uz.infinity.zero_one_demo.helper.ResponseHelper;
import uz.infinity.zero_one_demo.repository.ICalculationRepository;
import uz.infinity.zero_one_demo.repository.ICrudRepository;
import uz.infinity.zero_one_demo.repository.IInitRepository;
import uz.infinity.zero_one_demo.repository.entity_repository.CalculationTableRepository;
import uz.infinity.zero_one_demo.repository.entity_repository.EmployeeRepository;
import uz.infinity.zero_one_demo.repository.entity_repository.OrganizationRepository;
import uz.infinity.zero_one_demo.util.ClassConvertorHelper;
import uz.infinity.zero_one_demo.util.DateTimeConvertor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalculationTableServiceImpl implements
        ICrudRepository<CalculationTableDto>,
        ICalculationRepository {
    private final OrganizationRepository organizationRepository;
    private final ResponseHelper responseHelper;
    private final EmployeeRepository employeeRepository;
    private final CalculationTableRepository calculationTableRepository;
    private final DateTimeConvertor convertor;
    private final ClassConvertorHelper classConvertorHelper;

    @Override
    public ResponseEntity<?> create(CalculationTableDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotini yaratishda xatolik.",
                            "Ошибка при создании информации об учетной записи.",
                            "Error creating account information."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getEmployeeId() == null || request.getEmployeeId().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi idisi bo'sh bo'lmasligi kerak.",
                            "Идентификатор работника не должен быть пустым.",
                            "The worker id should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<Employee> employeeOptional = employeeRepository.findById(request.getEmployeeId());
        if (employeeOptional.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Bunday idiga ega bo'lgan ishchi mavjud emas.",
                            "Нет работника с таким идентификатором.",
                            "There is no worker with this id."),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (request.getAmount() == null || request.getAmount() <= 0) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Pul miqdori noto'g'ri kiritildi.",
                            "Сумма была введена неверно.",
                            "The amount was entered incorrectly."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getRate() == null || request.getRate() <= 0) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ish stavkasi noto'g'ri kiritildi.",
                            "Неверно введена норма работы.",
                            "The work rate was entered incorrectly."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getDate() == null || request.getDate().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Sana bo'sh bo'lmasligi kerak.",
                            "Дата не должна быть пустой.",
                            "The date must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate givenDate = convertor
                .convertStringToDate(request.getDate(), "yyyy.MM.dd");
        if (givenDate == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Sana noto'g'ri formatda kiritildi.",
                            "Дата введена в неправильном формате",
                            "The date was entered in the wrong format"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (request.getCalculationType() == null || request.getCalculationType().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob turi kiritilishi majburiy.",
                            "Тип учетной записи является обязательным.",
                            "Account type is mandatory."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (!request.getCalculationType().equals(CalculationType.AWARD.name()) &&
                !request.getCalculationType().equals(CalculationType.PENSION.name()) &&
                !request.getCalculationType().equals(CalculationType.VACATION.name()) &&
                !request.getCalculationType().equals(CalculationType.SALARY.name())) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob turi noto'g'ri kiritildi.",
                            "Тип счета введен неверно.",
                            "The account type was entered incorrectly.."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getOrganizationId() == null || request.getOrganizationId().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot idisi bo'sh bo'lmasligi kerak.",
                            "Идентификатор организации не должен быть пустым.",
                            "The organization id should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<Organization> organizationOptional = organizationRepository
                .findById(request.getOrganizationId());

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
            calculationTableRepository.save(new CalculationTable(
                    employeeOptional.get(),
                    request.getAmount(),
                    request.getRate(),
                    givenDate,
                    organizationOptional.get(),
                    CalculationType.valueOf(request.getCalculationType())
            ));
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotlari muvaffaqiyatli yaratildi.",
                            "Информация об учетной записи успешно создана.",
                            "Account information has been created successfully."),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotlari yaratishda xatolik.",
                            "Ошибка при создании информации об учетной записи.",
                            "Error creating account information."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<?> update(String id, CalculationTableDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotlarini taxrirlashda xatolik.",
                            "Ошибка редактирования информации об аккаунте.",
                            "Error editing account information."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (id == null || id.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotlari idisi bo'sh bo'lmasligi kerak.",
                            "Контейнер с информацией об учетной записи не должен быть пустым.",
                            "Account information container should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<CalculationTable> calculationTableOptional = calculationTableRepository.findById(id);
        if (calculationTableOptional.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Bunday idiga ega bo'lgan Hisob kitob ma'lumotlari mavjud emas.",
                            "С этим идентификатором нет информации об учетной записи.",
                            "There is no Account information with this id."),
                    HttpStatus.BAD_REQUEST
            );
        }
        CalculationTable calculationTable = calculationTableOptional.get();

        if (request.getEmployeeId() != null &&
                !request.getEmployeeId().isEmpty() &&
                !calculationTable.getEmployee().getId().equals(request.getEmployeeId())) {
            Optional<Employee> employeeOptional = employeeRepository.findById(request.getEmployeeId());
            if (employeeOptional.isEmpty()) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Bunday idiga ega bo'lgan ishchi mavjud emas.",
                                "Нет работника с таким идентификатором.",
                                "There is no worker with this id."),
                        HttpStatus.BAD_REQUEST
                );
            }
            calculationTable.setEmployee(employeeOptional.get());
        }

        if (request.getAmount() != null &&
                request.getAmount() > 0 &&
                !calculationTable.getAmount().equals(request.getAmount())) {
            calculationTable.setAmount(request.getAmount());
        }
        if (request.getRate() != null &&
                request.getRate() > 0 &&
                !calculationTable.getRate().equals(request.getRate())) {
            calculationTable.setRate(request.getRate());
        }
        if (request.getDate() != null && !request.getDate().isEmpty() &&
                !convertor.convertDateToString(calculationTable.getDate(), "yyyy.MM.dd")
                        .equals(request.getDate())) {
            LocalDate givenDate = convertor
                    .convertStringToDate(request.getDate(), "yyyy.MM.dd");
            if (givenDate == null) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Sana noto'g'ri formatda kiritildi.",
                                "Дата введена в неправильном формате",
                                "The date was entered in the wrong format"),
                        HttpStatus.BAD_REQUEST
                );
            }
            calculationTable.setDate(givenDate);
        }

        if (request.getOrganizationId() != null &&
                !request.getOrganizationId().isEmpty() &&
                !request.getOrganizationId().equals(calculationTable.getOrganization().getId())) {
            Optional<Organization> organizationOptional = organizationRepository
                    .findById(request.getOrganizationId());

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
            calculationTable.setOrganization(organizationOptional.get());
        }
        if (request.getCalculationType() != null &&
                !request.getCalculationType().isEmpty() &&
                !request.getCalculationType().equals(calculationTable.getCalculationType().name())) {
            if (!request.getCalculationType().equals(CalculationType.AWARD.name()) &&
                    !request.getCalculationType().equals(CalculationType.PENSION.name()) &&
                    !request.getCalculationType().equals(CalculationType.VACATION.name()) &&
                    !request.getCalculationType().equals(CalculationType.SALARY.name())) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Hisob kitob turi noto'g'ri kiritildi.",
                                "Тип счета введен неверно.",
                                "The account type was entered incorrectly.."),
                        HttpStatus.BAD_REQUEST
                );
            }
            calculationTable.setCalculationType(CalculationType.valueOf(request.getCalculationType()));
        }
        try {
            calculationTableRepository.save(calculationTable);
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotlari muvaffaqiyatli taxrilandi.",
                            "Информация об аккаунте успешно отредактирована.",
                            "Account information was successfully edited."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotlarini taxrirlashda xatolik.",
                            "Ошибка редактирования информации об аккаунте.",
                            "Error editing account information."),
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
                            "Hisob kitob ma'lumotlar idisi bo'sh bo'lmasligi kerak.",
                            "Идентификатор учетных данных не должен быть пустым.",
                            "The accounting data id must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<CalculationTable> calculationTableOptional = calculationTableRepository.findById(id);
        if (calculationTableOptional.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Bunday idiga ega bo'lgan hisob kitob ma'lumotlari mavjud emas.",
                            "Информация об учетной записи с этим идентификатором отсутствует.",
                            "There is no account information with this id."),
                    HttpStatus.BAD_REQUEST
            );
        }
        try {
            calculationTableRepository.delete(calculationTableOptional.get());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotlari muvaffaqiyatli o'chirildi.",
                            "Данные аккаунта успешно удалены.",
                            "Account data has been deleted successfully."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotlarini o'chirishda xatolik.",
                            "Ошибка удаления информации об аккаунте.",
                            "Error deleting account information."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<?> calculateWorkRate(MonthRateDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotini hisoblashda xatolik.",
                            "Ошибка расчета данных бухгалтерской книги.",
                            "Error calculating account book information."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getYearMonth() == null || request.getYearMonth().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Yil va oy bo'sh bo'lmasligi kerak.",
                            "Необходимо указать год и месяц.",
                            "Year and month must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate gDate = convertor.convertStringToDate(
                request.getYearMonth().trim().concat(".01"),
                "yyyy.MM.dd");


        if (gDate == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Sana noto'g'ri formatda kiritildi.",
                            "Дата введена в неправильном формате",
                            "The date was entered in the wrong format"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (request.getWorkRate() == null || request.getWorkRate() <= 0) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ish stavkasi noto'g'ri kiritildi.",
                            "Неверно введена норма работы.",
                            "The work rate was entered incorrectly."),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate lastDay = gDate.withDayOfMonth(
                gDate.getMonth().length(gDate.isLeapYear()));
        try {
            List<CalculationTable> resultList = calculationTableRepository.getAllBetweenDates(gDate, lastDay,
                    request.getWorkRate());
            if (resultList.isEmpty()) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Berilgan oyda ma'lumot topilmadi.",
                                "За данный месяц информации не найдено",
                                "No information found for the given month"),
                        HttpStatus.NO_CONTENT
                );
            }
            return responseHelper.prepareResponse(
                    classConvertorHelper.convertFromMonthRateList(resultList),
                    new Message(
                            "OK.",
                            "OK.",
                            "OK."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob qilishda xatolik.",
                            "Ошибка бухгалтерского учета.",
                            "Accounting error."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<?> calculationRegionWorkInfo(RegionWorkerInfoDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotini hisoblashda xatolik.",
                            "Ошибка расчета данных бухгалтерской книги.",
                            "Error calculating account book information."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getYearMonth() == null || request.getYearMonth().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Yil va oy bo'sh bo'lmasligi kerak.",
                            "Необходимо указать год и месяц.",
                            "Year and month must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate gDate = convertor.convertStringToDate(
                request.getYearMonth().trim().concat(".01"),
                "yyyy.MM.dd");


        if (gDate == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Sana noto'g'ri formatda kiritildi.",
                            "Дата введена в неправильном формате",
                            "The date was entered in the wrong format"),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate lastDay = gDate.withDayOfMonth(
                gDate.getMonth().length(gDate.isLeapYear()));
        try {
            List<CalculationTable> resultList = calculationTableRepository
                    .getAllRegionWorkerBetweenDates(gDate, lastDay);
            if (resultList.isEmpty()) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Berilgan oyda ma'lumot topilmadi.",
                                "За данный месяц информации не найдено",
                                "No information found for the given month"),
                        HttpStatus.NO_CONTENT
                );
            }
            return responseHelper.prepareResponse(
                    classConvertorHelper.convertFromCalculationTableList(resultList),
                    new Message(
                            "OK.",
                            "OK.",
                            "OK."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob qilishda xatolik.",
                            "Ошибка бухгалтерского учета.",
                            "Accounting error."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<?> calculateOrganizationWorkers(OrganizationWorkerDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotini hisoblashda xatolik.",
                            "Ошибка расчета данных бухгалтерской книги.",
                            "Error calculating account book information."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getYearMonth() == null || request.getYearMonth().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Yil va oy bo'sh bo'lmasligi kerak.",
                            "Необходимо указать год и месяц.",
                            "Year and month must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate gDate = convertor.convertStringToDate(
                request.getYearMonth().trim().concat(".01"),
                "yyyy.MM.dd");


        if (gDate == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Sana noto'g'ri formatda kiritildi.",
                            "Дата введена в неправильном формате",
                            "The date was entered in the wrong format"),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate lastDay = gDate.withDayOfMonth(
                gDate.getMonth().length(gDate.isLeapYear()));
        if (request.getOrganizationId() == null || request.getOrganizationId().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Tashkilot idisi bo'sh bo'lmasligi kerak.",
                            "Идентификатор организации не должен быть пустым.",
                            "The organization id should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<Organization> organizationOptional = organizationRepository
                .findById(request.getOrganizationId());

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
            List<CalculationTable> resultList = calculationTableRepository.getAllOrganizationWorkerBetweenDates(
                    gDate, lastDay, organizationOptional.get().getId()
            );
            List<Organization> organizationList =
                    organizationRepository.findByParent_Id(organizationOptional.get().getId());
            for (Organization organization : organizationList) {
                resultList.addAll(calculationTableRepository.getAllOrganizationWorkerBetweenDates(
                        gDate, lastDay, organization.getId()
                ));
            }
            if (resultList.isEmpty()) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Berilgan oyda ma'lumot topilmadi.",
                                "За данный месяц информации не найдено",
                                "No information found for the given month"),
                        HttpStatus.NOT_FOUND
                );
            }
            return responseHelper.prepareResponse(
                    classConvertorHelper.convertOrganizationFromCalculationTableList(resultList),
                    new Message(
                            "OK.",
                            "OK.",
                            "OK."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob qilishda xatolik.",
                            "Ошибка бухгалтерского учета.",
                            "Accounting error."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<?> calculateSalaryVacation(RegionWorkerInfoDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob ma'lumotini hisoblashda xatolik.",
                            "Ошибка расчета данных бухгалтерской книги.",
                            "Error calculating account book information."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getYearMonth() == null || request.getYearMonth().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Yil va oy bo'sh bo'lmasligi kerak.",
                            "Необходимо указать год и месяц.",
                            "Year and month must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate gDate = convertor.convertStringToDate(
                request.getYearMonth().trim().concat(".01"),
                "yyyy.MM.dd");


        if (gDate == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Sana noto'g'ri formatda kiritildi.",
                            "Дата введена в неправильном формате",
                            "The date was entered in the wrong format"),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate lastDay = gDate.withDayOfMonth(
                gDate.getMonth().length(gDate.isLeapYear()));
        try {
            List<CalculationTable> resultList = calculationTableRepository
                    .getAllRegionWorkerBetweenDates(gDate, lastDay);
            if (resultList.isEmpty()) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Berilgan oyda ma'lumot topilmadi.",
                                "За данный месяц информации не найдено",
                                "No information found for the given month"),
                        HttpStatus.NO_CONTENT
                );
            }
            return responseHelper.prepareResponse(
                    classConvertorHelper.convertSalaryVacationFromCalculationTableList(resultList),
                    new Message(
                            "OK.",
                            "OK.",
                            "OK."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Hisob kitob qilishda xatolik.",
                            "Ошибка бухгалтерского учета.",
                            "Accounting error."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
