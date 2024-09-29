package uz.infinity.zero_one_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.infinity.zero_one_demo.dto.MonthRateDto;
import uz.infinity.zero_one_demo.dto.OrganizationWorkerDto;
import uz.infinity.zero_one_demo.dto.RegionWorkerInfoDto;
import uz.infinity.zero_one_demo.service.api.IApiService;

@RestController
@RequestMapping(value = "/v1/api",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ApiController {
    @Qualifier("apiServiceImpl")
    private final IApiService apiService;

    @PostMapping("/calculateWorkRate")
    public ResponseEntity<?> calculateWorkRate(@RequestBody MonthRateDto request) {
        return apiService.calculateWorkRate(request);
    }

    @PostMapping("/calculateRegionWorker")
    public ResponseEntity<?> calculateRegionWorker(@RequestBody RegionWorkerInfoDto request) {
        return apiService.calculateRegionWorkerInfo(request);
    }

    @PostMapping("/calculateOrganizationWorkers")
    public ResponseEntity<?> calculateOrganizationWorkers(@RequestBody OrganizationWorkerDto request) {
        return apiService.calculateOrganizationWorkers(request);
    }

    @PostMapping("/calculateSalaryVacation")
    public ResponseEntity<?> calculateSalaryVacation(@RequestBody RegionWorkerInfoDto request) {
        return apiService.calculateSalaryVacation(request);
    }
}
