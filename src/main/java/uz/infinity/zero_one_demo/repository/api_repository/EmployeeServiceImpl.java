package uz.infinity.zero_one_demo.repository.api_repository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.infinity.zero_one_demo.constant.CalculationType;
import uz.infinity.zero_one_demo.dto.EmployeeDto;
import uz.infinity.zero_one_demo.entity.CalculationTable;
import uz.infinity.zero_one_demo.entity.Employee;
import uz.infinity.zero_one_demo.entity.Organization;
import uz.infinity.zero_one_demo.exception.GlobalExceptionHandler;
import uz.infinity.zero_one_demo.helper.Message;
import uz.infinity.zero_one_demo.helper.ResponseHelper;
import uz.infinity.zero_one_demo.repository.ICrudRepository;
import uz.infinity.zero_one_demo.repository.IInitRepository;
import uz.infinity.zero_one_demo.repository.entity_repository.CalculationTableRepository;
import uz.infinity.zero_one_demo.repository.entity_repository.EmployeeRepository;
import uz.infinity.zero_one_demo.repository.entity_repository.OrganizationRepository;
import uz.infinity.zero_one_demo.util.DateTimeConvertor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements
        ICrudRepository<EmployeeDto>,
        IInitRepository {
    private final OrganizationRepository organizationRepository;
    private final ResponseHelper responseHelper;
    private final EmployeeRepository employeeRepository;
    private final DateTimeConvertor convertor;
    private final CalculationTableRepository calculationTableRepository;

    @Override
    public ResponseEntity<?> create(EmployeeDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi yaratishda xatolik.",
                            "Ошибка создания работника.",
                            "Error creating worker."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi ismi bo'sh bo'lmasligi kerak.",
                            "Имя работника не должно быть пустым.",
                            "The worker name must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi familiyasi bo'sh bo'lmasligi kerak.",
                            "Фамилия сотрудника не должна быть пустой.",
                            "Employee last name must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getPinfl() == null || request.getPinfl().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi jshshiri bo'sh bo'lmasligi kerak.",
                            "ПИНФЛ сотрудника не должна быть пустой.",
                            "The worker PINPP must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (!request.getPinfl().matches("^\\d{14}")) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "JSHSHIR uzunligi 14 ta raqamdan iborat bo'lishi kerak.",
                            "ПИНФЛ должен состоять из 14 цифр.",
                            "PINPP must be 14 digits long."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (request.getOrganizationId() == null || request.getOrganizationId().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchining ishlaydigan tashkilot idisi bo'sh bo'lmasligi kerak.",
                            "Идентификатор рабочей организации работника не должен быть пустым.",
                            "The ID of the employee's working organization should not be empty."),
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
        if (request.getHireDate() == null || request.getHireDate().isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi ishga kirish sanasi bo'sh bo'lmasligi kerak.",
                            "Дата трудоустройства не должна быть пустой.",
                            "The date of employment must not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        LocalDate hireDate = convertor
                .convertStringToDate(request.getHireDate(), "yyyy.MM.dd");
        if (hireDate == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Sana noto'g'ri formatda kiritildi.",
                            "Дата введена в неправильном формате",
                            "The date was entered in the wrong format"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            employeeRepository.save(new Employee(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getPinfl(),
                    hireDate,
                    organizationOptional.get()
            ));
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi muvaffaqiyatli yaratildi.",
                            "Рабочий успешно создан.",
                            "Worker created successfully."),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi yaratishda xatolik.",
                            "Ошибка создания работника.",
                            "Error creating worker."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<?> update(String id, EmployeeDto request) {
        if (request == null) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi taxrirlashda xatolik.",
                            "Произошла ошибка при редактировании работника.",
                            "An error occurred while editing the worker."),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (id == null || id.isEmpty()) {
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi idisi bo'sh bo'lmasligi kerak.",
                            "Рабочий идентификатор не должен быть пустым.",
                            "The working id should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
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
        Employee employee = employeeOptional.get();

        if (request.getFirstName() != null &&
                !request.getFirstName().isEmpty() &&
                !employee.getFirstName().equals(request.getFirstName())) {
            employee.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null &&
                !request.getLastName().isEmpty() &&
                !employee.getLastName().equals(request.getLastName())) {
            employee.setLastName(request.getLastName());
        }

        if (request.getPinfl() != null &&
                !request.getPinfl().isEmpty() &&
                !employee.getPinfl().equals(request.getPinfl())) {
            if (!request.getPinfl().matches("^\\d{14}")) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "JSHSHIR uzunligi 14 ta raqamdan iborat bo'lishi kerak.",
                                "ПИНФЛ должен состоять из 14 цифр.",
                                "PINPP must be 14 digits long."),
                        HttpStatus.BAD_REQUEST
                );
            }
            employee.setPinfl(request.getPinfl());
        }
        if (request.getHireDate() != null &&
                !request.getHireDate().isEmpty() &&
                !convertor.convertDateToString(employee.getHireDate(), "yyyy.MM.dd")
                        .equals(request.getHireDate())) {
            LocalDate hireDate = convertor
                    .convertStringToDate(request.getHireDate(), "yyyy.MM.dd");
            if (hireDate == null) {
                return responseHelper.prepareResponse(
                        new ArrayList<>(),
                        new Message(
                                "Sana noto'g'ri formatda kiritildi.",
                                "Дата введена в неправильном формате",
                                "The date was entered in the wrong format"),
                        HttpStatus.BAD_REQUEST
                );
            }
            employee.setHireDate(hireDate);
        }
        if (request.getOrganizationId() != null &&
                !request.getOrganizationId().isEmpty() &&
                employee.getOrganization() != null &&
                !employee.getOrganization().getId().equals(request.getOrganizationId())) {
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
            employee.setOrganization(organizationOptional.get());
        }
        try {
            employeeRepository.save(employee);
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi muvaffaqiyatli taxrilandi.",
                            "Рабочий успешно отредактирован.",
                            "The worker was successfully edited."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi taxrirlashda xatolik.",
                            "Произошла ошибка при редактировании работника.",
                            "An error occurred while editing the worker."),
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
                            "Ishchi idisi bo'sh bo'lmasligi kerak.",
                            "Идентификатор работника не должен быть пустым.",
                            "The worker id should not be empty."),
                    HttpStatus.BAD_REQUEST
            );
        }
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
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
        try {
            Employee employee = employeeOptional.get();
            employeeRepository.delete(employee);
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi xodim muvaffaqiyatli o'chirildi.",
                            "Работник был успешно удален.",
                            "The worker was successfully deleted."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logger.error(e.getMessage());
            return responseHelper.prepareResponse(
                    new ArrayList<>(),
                    new Message(
                            "Ishchi xodim o'chirishda xatolik.",
                            "Ошибка удаления работника.",
                            "Error deleting worker."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public void init() {
        Optional<Organization> organizationOptionalZO =
                organizationRepository.findByName("ZERO ONE");
        Optional<Organization> organizationOptionalZOA =
                organizationRepository.findByName("ZERO ONE ANDIJON");
        Optional<Organization> organizationOptionalG =
                organizationRepository.findByName("GITA JIZZAH");
        Optional<Organization> organizationOptionalGWS =
                organizationRepository.findByName("GREEN WHITE SOLUTIONS");
        Optional<Organization> organizationOptionalGWSX =
                organizationRepository.findByName("GREEN WHITE SOLUTIONS");
        Optional<Organization> organizationOptionalBPE =
                organizationRepository.findByName("BE PRO EDUCATION");
        List<Employee> employeeList = employeeRepository.findAll();
        if (employeeList.isEmpty()) {
            organizationOptionalZO.ifPresent(organization -> employeeRepository.saveAll(
                    List.of(
                            new Employee("Toxirjon", "Oribjonov", "31305965140045",
                                    convertor.convertStringToDate("2024.08.01", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test1", "Testov1", "31201025140041",
                                    convertor.convertStringToDate("2024.03.10", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test", "Testov", "32005005140043",
                                    convertor.convertStringToDate("2024.05.05", "yyyy.MM.dd"),
                                    organization)
                    )
            ));
            if (organizationOptionalZO.isPresent()) {
                Optional<Employee> e1 = employeeRepository.findByPinfl("31305965140045").stream().findFirst();
                Optional<Employee> e2 = employeeRepository.findByPinfl("31201025140041").stream().findFirst();
                Optional<Employee> e3 = employeeRepository.findByPinfl("32005005140043").stream().findFirst();
                if (e1.isPresent()) {
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e1.get(),
                                    1500000.0,
                                    2.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalZO.get(),
                                    CalculationType.VACATION
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e1.get(),
                                    10000000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalZO.get(),
                                    CalculationType.SALARY
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e1.get(),
                                    1000000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalZO.get(),
                                    CalculationType.AWARD
                            )
                    );
                }
                if (e2.isPresent()) {
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e2.get(),
                                    2000000.0,
                                    2.5,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalZO.get(),
                                    CalculationType.VACATION
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e2.get(),
                                    12000000.0,
                                    1.5,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalZO.get(),
                                    CalculationType.SALARY
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e2.get(),
                                    1200000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalZO.get(),
                                    CalculationType.AWARD
                            )
                    );
                }
                if (e3.isPresent()) {
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e3.get(),
                                    2400000.0,
                                    3.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalZO.get(),
                                    CalculationType.VACATION
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e3.get(),
                                    16000000.0,
                                    2.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalZO.get(),
                                    CalculationType.SALARY
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e3.get(),
                                    1600000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalZO.get(),
                                    CalculationType.AWARD
                            )
                    );
                }

            }

            organizationOptionalGWSX.ifPresent(organization -> employeeRepository.saveAll(
                    List.of(
                            new Employee("Toxirjon", "Oribjonov", "31305965140045",
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test1", "Testov1", "31201025140041",
                                    convertor.convertStringToDate("2024.04.10", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test", "Testov", "32005005140043",
                                    convertor.convertStringToDate("2024.06.01", "yyyy.MM.dd"),
                                    organization)
                    )
            ));
            if (organizationOptionalGWSX.isPresent()) {
                Optional<Employee> e1 = employeeRepository.findByPinfl("31305965140045").stream().findFirst();
                Optional<Employee> e2 = employeeRepository.findByPinfl("31201025140041").stream().findFirst();
                Optional<Employee> e3 = employeeRepository.findByPinfl("32005005140043").stream().findFirst();
                if (e3.isPresent()) {
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e3.get(),
                                    1500000.0,
                                    2.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWSX.get(),
                                    CalculationType.VACATION
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e3.get(),
                                    10000000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWSX.get(),
                                    CalculationType.SALARY
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e3.get(),
                                    1000000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWSX.get(),
                                    CalculationType.AWARD
                            )
                    );
                }
                if (e2.isPresent()) {
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e2.get(),
                                    2000000.0,
                                    2.5,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWSX.get(),
                                    CalculationType.VACATION
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e2.get(),
                                    12000000.0,
                                    1.5,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWSX.get(),
                                    CalculationType.SALARY
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e2.get(),
                                    1200000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWSX.get(),
                                    CalculationType.AWARD
                            )
                    );
                }
                if (e1.isPresent()) {
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e1.get(),
                                    2400000.0,
                                    3.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWSX.get(),
                                    CalculationType.VACATION
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e1.get(),
                                    16000000.0,
                                    2.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWSX.get(),
                                    CalculationType.SALARY
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e1.get(),
                                    1600000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWSX.get(),
                                    CalculationType.AWARD
                            )
                    );
                }
            }

            organizationOptionalZOA.ifPresent(organization -> employeeRepository.saveAll(
                    List.of(
                            new Employee("Toxirjon", "Oribjonov", "31305965140045",
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test2", "Testov2", "30102965140033",
                                    convertor.convertStringToDate("2024.02.10", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test", "Testov", "32005005140043",
                                    convertor.convertStringToDate("2024.03.01", "yyyy.MM.dd"),
                                    organization)
                    )
            ));
            organizationOptionalBPE.ifPresent(organization -> employeeRepository.saveAll(
                    List.of(
                            new Employee("Toxirjon", "Oribjonov", "31305965140045",
                                    convertor.convertStringToDate("2024.07.01", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test2", "Testov2", "30102965140033",
                                    convertor.convertStringToDate("2024.01.10", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test", "Testov", "32005005140043",
                                    convertor.convertStringToDate("2024.02.01", "yyyy.MM.dd"),
                                    organization)
                    )
            ));

            organizationOptionalG.ifPresent(organization -> employeeRepository.saveAll(
                    List.of(
                            new Employee("Toxirjon", "Oribjonov", "31305965140045",
                                    convertor.convertStringToDate("2024.05.01", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test1", "Testov1", "31201025140041",
                                    convertor.convertStringToDate("2024.09.10", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test", "Testov", "32005005140043",
                                    convertor.convertStringToDate("2024.05.01", "yyyy.MM.dd"),
                                    organization)
                    )
            ));
            organizationOptionalGWS.ifPresent(organization -> employeeRepository.saveAll(
                    List.of(
                            new Employee("Toxirjon", "Oribjonov", "31305965140045",
                                    convertor.convertStringToDate("2024.01.01", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test2", "Testov2", "30306845140031",
                                    convertor.convertStringToDate("2024.02.10", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test3", "Testov3", "30306915140032",
                                    convertor.convertStringToDate("2024.11.16", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test4", "Testov4", "31212955140035",
                                    convertor.convertStringToDate("2024.08.02", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test5", "Testov5", "30509975140036",
                                    convertor.convertStringToDate("2024.04.15", "yyyy.MM.dd"),
                                    organization),
                            new Employee("Test", "Testov", "32005005140043",
                                    convertor.convertStringToDate("2024.01.01", "yyyy.MM.dd"),
                                    organization)
                    )
            ));
            if (organizationOptionalGWS.isPresent()) {
                Optional<Employee> e1 = employeeRepository.findByPinfl("31305965140045").stream().findFirst();
                Optional<Employee> e2 = employeeRepository.findByPinfl("30306915140032").stream().findFirst();
                Optional<Employee> e3 = employeeRepository.findByPinfl("31212955140035").stream().findFirst();
                if (e3.isPresent()) {
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e3.get(),
                                    1500000.0,
                                    2.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWS.get(),
                                    CalculationType.VACATION
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e3.get(),
                                    10000000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWS.get(),
                                    CalculationType.SALARY
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e3.get(),
                                    1000000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWS.get(),
                                    CalculationType.AWARD
                            )
                    );
                }
                if (e2.isPresent()) {
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e2.get(),
                                    2000000.0,
                                    2.5,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWS.get(),
                                    CalculationType.VACATION
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e2.get(),
                                    12000000.0,
                                    1.5,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWS.get(),
                                    CalculationType.SALARY
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e2.get(),
                                    1200000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWS.get(),
                                    CalculationType.AWARD
                            )
                    );
                }
                if (e1.isPresent()) {
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e1.get(),
                                    2400000.0,
                                    3.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWS.get(),
                                    CalculationType.VACATION
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e1.get(),
                                    16000000.0,
                                    2.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWS.get(),
                                    CalculationType.SALARY
                            )
                    );
                    calculationTableRepository.save(
                            new CalculationTable(
                                    e1.get(),
                                    1600000.0,
                                    1.0,
                                    convertor.convertStringToDate("2024.09.01", "yyyy.MM.dd"),
                                    organizationOptionalGWS.get(),
                                    CalculationType.AWARD
                            )
                    );
                }
            }
        }
    }
}
