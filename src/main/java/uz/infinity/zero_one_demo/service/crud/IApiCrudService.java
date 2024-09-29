package uz.infinity.zero_one_demo.service.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import uz.infinity.zero_one_demo.dto.CalculationTableDto;
import uz.infinity.zero_one_demo.dto.EmployeeDto;
import uz.infinity.zero_one_demo.dto.OrganizationDto;
import uz.infinity.zero_one_demo.dto.RegionDto;

@Repository
public interface IApiCrudService {
    ResponseEntity<?> createRegion(RegionDto request);

    ResponseEntity<?> updateRegion(String id, RegionDto request);

    ResponseEntity<?> deleteRegion(String id);

    ResponseEntity<?> createOrganization(OrganizationDto request);

    ResponseEntity<?> updateOrganization(String id, OrganizationDto request);

    ResponseEntity<?> deleteOrganization(String id);

    ResponseEntity<?> createEmployee(EmployeeDto request);

    ResponseEntity<?> updateEmployee(String id, EmployeeDto request);

    ResponseEntity<?> deleteEmployee(String id);

    ResponseEntity<?> createCalculationTable(CalculationTableDto request);

    ResponseEntity<?> updateCalculationTable(String id, CalculationTableDto request);

    ResponseEntity<?> deleteCalculationTable(String id);
}