package uz.infinity.zero_one_demo.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import uz.infinity.zero_one_demo.dto.MonthRateDto;
import uz.infinity.zero_one_demo.dto.OrganizationWorkerDto;
import uz.infinity.zero_one_demo.dto.RegionWorkerInfoDto;

@Repository
public interface ICalculationRepository {
    ResponseEntity<?> calculateWorkRate(MonthRateDto request);

    ResponseEntity<?> calculationRegionWorkInfo(RegionWorkerInfoDto request);

    ResponseEntity<?> calculateOrganizationWorkers(OrganizationWorkerDto request);

    ResponseEntity<?> calculateSalaryVacation(RegionWorkerInfoDto request);
}
