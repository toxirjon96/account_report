package uz.infinity.zero_one_demo.service.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import uz.infinity.zero_one_demo.dto.MonthRateDto;
import uz.infinity.zero_one_demo.dto.OrganizationWorkerDto;
import uz.infinity.zero_one_demo.dto.RegionDto;
import uz.infinity.zero_one_demo.dto.RegionWorkerInfoDto;

@Repository
public interface IApiService {
    ResponseEntity<?> calculateWorkRate(MonthRateDto request);

    ResponseEntity<?> calculateRegionWorkerInfo(RegionWorkerInfoDto request);

    ResponseEntity<?> calculateOrganizationWorkers(OrganizationWorkerDto request);

    public ResponseEntity<?> calculateSalaryVacation(RegionWorkerInfoDto request);
}
