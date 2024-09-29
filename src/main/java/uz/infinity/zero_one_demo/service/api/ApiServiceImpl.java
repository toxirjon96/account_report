package uz.infinity.zero_one_demo.service.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.infinity.zero_one_demo.dto.MonthRateDto;
import uz.infinity.zero_one_demo.dto.OrganizationWorkerDto;
import uz.infinity.zero_one_demo.dto.RegionWorkerInfoDto;
import uz.infinity.zero_one_demo.repository.ICalculationRepository;

@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements IApiService {
    @Qualifier("calculationTableServiceImpl")
    private final ICalculationRepository calculationRepository;

    @Override
    public ResponseEntity<?> calculateWorkRate(MonthRateDto request) {
        return calculationRepository.calculateWorkRate(request);
    }

    @Override
    public ResponseEntity<?> calculateRegionWorkerInfo(RegionWorkerInfoDto request) {
        return calculationRepository.calculationRegionWorkInfo(request);
    }

    @Override
    public ResponseEntity<?> calculateOrganizationWorkers(OrganizationWorkerDto request) {
        return calculationRepository.calculateOrganizationWorkers(request);
    }

    @Override
    public ResponseEntity<?> calculateSalaryVacation(RegionWorkerInfoDto request) {
        return calculationRepository.calculateSalaryVacation(request);
    }
}
