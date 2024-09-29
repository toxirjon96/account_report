package uz.infinity.zero_one_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.infinity.zero_one_demo.dto.CalculationTableDto;
import uz.infinity.zero_one_demo.dto.EmployeeDto;
import uz.infinity.zero_one_demo.dto.OrganizationDto;
import uz.infinity.zero_one_demo.dto.RegionDto;
import uz.infinity.zero_one_demo.service.crud.IApiCrudService;

@RestController
@RequestMapping(value = "/v1/crud-api",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CrudController {
    @Qualifier("apiCrudServiceImpl")
    private final IApiCrudService apiService;

    @PostMapping("/saveRegion")
    public ResponseEntity<?> saveRegion(@RequestBody RegionDto request) {
        return apiService.createRegion(request);
    }

    @PutMapping("/updateRegion/{regionId}")
    public ResponseEntity<?> updateRegion(@RequestBody RegionDto request,
                                          @PathVariable String regionId) {
        return apiService.updateRegion(regionId, request);
    }

    @DeleteMapping("/deleteRegion/{regionId}")
    public ResponseEntity<?> updateRegion(@PathVariable String regionId) {
        return apiService.deleteRegion(regionId);
    }

    @PostMapping("/saveOrganization")
    public ResponseEntity<?> saveOrganization(@RequestBody OrganizationDto request) {
        return apiService.createOrganization(request);
    }

    @PutMapping("/updateOrganization/{organizationId}")
    public ResponseEntity<?> updateOrganization(@RequestBody OrganizationDto request,
                                                @PathVariable String organizationId) {
        return apiService.updateOrganization(organizationId, request);
    }

    @DeleteMapping("/deleteOrganization/{organizationId}")
    public ResponseEntity<?> updateOrganization(@PathVariable String organizationId) {
        return apiService.deleteOrganization(organizationId);
    }

    @PostMapping("/saveEmployee")
    public ResponseEntity<?> saveEmployee(@RequestBody EmployeeDto request) {
        return apiService.createEmployee(request);
    }

    @PutMapping("/updateEmployee/{employeeId}")
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDto request,
                                            @PathVariable String employeeId) {
        return apiService.updateEmployee(employeeId, request);
    }

    @DeleteMapping("/deleteEmployee/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable String employeeId) {
        return apiService.deleteEmployee(employeeId);
    }

    @PostMapping("/saveCalculation")
    public ResponseEntity<?> saveCalculation(@RequestBody CalculationTableDto request) {
        return apiService.createCalculationTable(request);
    }

    @PutMapping("/updateCalculation/{calculationId}")
    public ResponseEntity<?> updateCalculation(@RequestBody CalculationTableDto request,
                                               @PathVariable String calculationId) {
        return apiService.updateCalculationTable(calculationId, request);
    }

    @DeleteMapping("/deleteCalculation/{calculationId}")
    public ResponseEntity<?> updateCalculation(@PathVariable String calculationId) {
        return apiService.deleteCalculationTable(calculationId);
    }
}
