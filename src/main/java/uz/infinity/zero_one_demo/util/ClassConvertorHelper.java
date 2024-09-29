package uz.infinity.zero_one_demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.infinity.zero_one_demo.constant.CalculationType;
import uz.infinity.zero_one_demo.entity.CalculationTable;
import uz.infinity.zero_one_demo.model.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClassConvertorHelper {
    private final DateTimeConvertor convertor;

    public List<MonthRateResponse> convertFromMonthRateList(List<CalculationTable> resultList) {
        List<MonthRateResponse> monthRateResponseList = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            Double rate = resultList.get(i).getRate();
            boolean isExist = false;
            for (int j = i + 1; j < resultList.size(); j++) {
                if (resultList.get(i).getEmployee() != null &&
                        resultList.get(j).getEmployee() != null &&
                        resultList.get(i).getEmployee().getPinfl()
                                .equals(resultList.get(j).getEmployee().getPinfl())) {
                    rate += resultList.get(j).getRate();
                }
            }
            for (MonthRateResponse monthRateResponse : monthRateResponseList) {
                if (resultList.get(i).getEmployee().getPinfl().equals(monthRateResponse.getPinfl())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                monthRateResponseList.add(new MonthRateResponse(
                        resultList.get(i).getEmployee().getFirstName(),
                        resultList.get(i).getEmployee().getLastName(),
                        resultList.get(i).getEmployee().getPinfl(),
                        rate
                ));
            }
        }
        return monthRateResponseList;
    }

    public List<OrganizationCount> convertFromCalculationTableList(List<CalculationTable> resultList) {
        List<OrganizationCount> organizationCountList = new ArrayList<>();

        for (int i = 0; i < resultList.size(); i++) {
            List<OrganizationInfo> organizationInfoList = new ArrayList<>();
            double amount = 0.0;
            if (resultList.get(i).getCalculationType() == CalculationType.SALARY) {
                amount = resultList.get(i).getAmount();
                organizationInfoList.add(new OrganizationInfo(
                        resultList.get(i).getOrganization().getId(),
                        resultList.get(i).getOrganization().getName(),
                        resultList.get(i).getOrganization().getParent() != null ?
                                resultList.get(i).getOrganization().getParent().getName() : "",
                        resultList.get(i).getOrganization().getRegion().getName()
                ));
            }
            for (int j = i + 1; j < resultList.size(); j++) {
                if (resultList.get(i).getEmployee().getPinfl().equals(resultList.get(j).getEmployee().getPinfl())) {
                    if (resultList.get(j).getCalculationType() == CalculationType.SALARY) {
                        amount += resultList.get(j).getAmount();
                        boolean isExist = false;
                        for (OrganizationInfo organizationInfo : organizationInfoList) {
                            if (organizationInfo.getId().equals(resultList.get(j).getOrganization().getId())) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            organizationInfoList.add(new OrganizationInfo(
                                    resultList.get(j).getOrganization().getId(),
                                    resultList.get(j).getOrganization().getName(),
                                    resultList.get(j).getOrganization().getParent() != null ?
                                            resultList.get(j).getOrganization().getParent().getName() : "",
                                    resultList.get(j).getOrganization().getRegion().getName()
                            ));
                        }
                    }
                }
            }
            boolean isExistPerson = false;
            for (OrganizationCount organizationInfo : organizationCountList) {
                if (organizationInfo.getPinfl().equals(resultList.get(i).getEmployee().getPinfl())) {
                    isExistPerson = true;
                    break;
                }
            }

            if (!isExistPerson) {
                if (organizationInfoList.size() > 1) {
                    organizationCountList.add(new OrganizationCount(
                            resultList.get(i).getEmployee().getFirstName(),
                            resultList.get(i).getEmployee().getLastName(),
                            resultList.get(i).getEmployee().getPinfl(),
                            amount,
                            organizationInfoList.size(),
                            organizationInfoList
                    ));
                }
            }
        }
        return organizationCountList;
    }

    public List<OrganizationWorkerResponse> convertOrganizationFromCalculationTableList(List<CalculationTable> resultList) {
        List<OrganizationWorkerResponse> organizationWorkerResponseList = new ArrayList<>();

        for (int i = 0; i < resultList.size(); i++) {
            List<OrganizationInfo> organizationInfoList = new ArrayList<>();
            double amount = 0.0;
            int salaryCount = 0;
            if (resultList.get(i).getCalculationType() == CalculationType.SALARY) {
                amount = resultList.get(i).getAmount();
                salaryCount++;
                organizationInfoList.add(new OrganizationInfo(
                        resultList.get(i).getOrganization().getId(),
                        resultList.get(i).getOrganization().getName(),
                        resultList.get(i).getOrganization().getParent() != null ?
                                resultList.get(i).getOrganization().getParent().getName() : "",
                        resultList.get(i).getOrganization().getRegion().getName()
                ));
            }
            for (int j = i + 1; j < resultList.size(); j++) {
                if (resultList.get(i).getEmployee().getPinfl().equals(resultList.get(j).getEmployee().getPinfl())) {
                    if (resultList.get(j).getCalculationType() == CalculationType.SALARY) {
                        amount += resultList.get(j).getAmount();
                        salaryCount++;
                        boolean isExist = false;
                        for (OrganizationInfo organizationInfo : organizationInfoList) {
                            if (organizationInfo.getId().equals(resultList.get(j).getOrganization().getId())) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            organizationInfoList.add(new OrganizationInfo(
                                    resultList.get(j).getOrganization().getId(),
                                    resultList.get(j).getOrganization().getName(),
                                    resultList.get(j).getOrganization().getParent() != null ?
                                            resultList.get(j).getOrganization().getParent().getName() : "",
                                    resultList.get(j).getOrganization().getRegion().getName()
                            ));
                        }
                    }
                }
            }
            boolean isExistPerson = false;
            for (OrganizationWorkerResponse organizationInfo : organizationWorkerResponseList) {
                if (organizationInfo.getPinfl().equals(resultList.get(i).getEmployee().getPinfl())) {
                    isExistPerson = true;
                    break;
                }
            }

            if (!isExistPerson) {
                organizationWorkerResponseList.add(new OrganizationWorkerResponse(
                        resultList.get(i).getEmployee().getFirstName(),
                        resultList.get(i).getEmployee().getLastName(),
                        resultList.get(i).getEmployee().getPinfl(),
                        amount / salaryCount,
                        organizationInfoList
                ));
            }
        }
        return organizationWorkerResponseList;
    }

    public List<SalaryVacationResponse> convertSalaryVacationFromCalculationTableList(List<CalculationTable> resultList) {
        List<SalaryVacationResponse> salaryVacationResponseList = new ArrayList<>();

        for (int i = 0; i < resultList.size(); i++) {
            List<SalaryVacation> salaryVacationList = new ArrayList<>();
            if (resultList.get(i).getCalculationType() == CalculationType.SALARY ||
                    resultList.get(i).getCalculationType() == CalculationType.VACATION) {
                salaryVacationList.add(new SalaryVacation(
                        resultList.get(i).getOrganization().getId(),
                        convertor.convertDateToString(resultList.get(i).getDate(), "dd.MM.yyyy"),
                        resultList.get(i).getCalculationType().name(),
                        resultList.get(i).getAmount(),
                        resultList.get(i).getOrganization().getName(),
                        resultList.get(i).getOrganization().getParent() != null ?
                                resultList.get(i).getOrganization().getParent().getName() : ""

                ));
            }
            for (int j = i + 1; j < resultList.size(); j++) {
                if (resultList.get(i).getEmployee().getPinfl().equals(resultList.get(j).getEmployee().getPinfl())) {
                    if (resultList.get(j).getCalculationType() == CalculationType.SALARY ||
                            resultList.get(j).getCalculationType() == CalculationType.VACATION) {
                        salaryVacationList.add(new SalaryVacation(
                                resultList.get(j).getOrganization().getId(),
                                convertor.convertDateToString(resultList.get(i).getDate(), "dd.MM.yyyy"),
                                resultList.get(j).getCalculationType().name(),
                                resultList.get(j).getAmount(),
                                resultList.get(j).getOrganization().getName(),
                                resultList.get(j).getOrganization().getParent() != null ?
                                        resultList.get(j).getOrganization().getParent().getName() : ""

                        ));

                    }
                }
            }
            boolean isExistPerson = false;
            for (SalaryVacationResponse salaryVacationResponse : salaryVacationResponseList) {
                if (salaryVacationResponse.getPinfl().equals(resultList.get(i).getEmployee().getPinfl())) {
                    isExistPerson = true;
                    break;
                }
            }

            if (!isExistPerson) {
                salaryVacationResponseList.add(new SalaryVacationResponse(
                        resultList.get(i).getEmployee().getFirstName(),
                        resultList.get(i).getEmployee().getLastName(),
                        resultList.get(i).getEmployee().getPinfl(),
                        salaryVacationList
                ));
            }
        }
        return salaryVacationResponseList;
    }
}
