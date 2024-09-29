package uz.infinity.zero_one_demo.service.crud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.infinity.zero_one_demo.dto.CalculationTableDto;
import uz.infinity.zero_one_demo.dto.EmployeeDto;
import uz.infinity.zero_one_demo.dto.OrganizationDto;
import uz.infinity.zero_one_demo.dto.RegionDto;
import uz.infinity.zero_one_demo.repository.ICrudRepository;

@Service
@RequiredArgsConstructor
public class ApiCrudServiceImpl implements IApiCrudService {
    private final ICrudRepository<RegionDto> regionRepository;
    private final ICrudRepository<OrganizationDto> organizationRepository;
    private final ICrudRepository<EmployeeDto> employeeRepository;
    private final ICrudRepository<CalculationTableDto> calculationRepository;

    @Override
    public ResponseEntity<?> createRegion(RegionDto request) {
        return regionRepository.create(request);
    }

    @Override
    public ResponseEntity<?> updateRegion(String id, RegionDto request) {
        return regionRepository.update(id, request);
    }

    @Override
    public ResponseEntity<?> deleteRegion(String id) {
        return regionRepository.delete(id);
    }

    @Override
    public ResponseEntity<?> createOrganization(OrganizationDto request) {
        return organizationRepository.create(request);
    }

    @Override
    public ResponseEntity<?> updateOrganization(String id, OrganizationDto request) {
        return organizationRepository.update(id, request);
    }

    @Override
    public ResponseEntity<?> deleteOrganization(String id) {
        return organizationRepository.delete(id);
    }

    @Override
    public ResponseEntity<?> createEmployee(EmployeeDto request) {
        return employeeRepository.create(request);
    }

    @Override
    public ResponseEntity<?> updateEmployee(String id, EmployeeDto request) {
        return employeeRepository.update(id, request);
    }

    @Override
    public ResponseEntity<?> deleteEmployee(String id) {
        return employeeRepository.delete(id);
    }

    @Override
    public ResponseEntity<?> createCalculationTable(CalculationTableDto request) {
        return calculationRepository.create(request);
    }

    @Override
    public ResponseEntity<?> updateCalculationTable(String id, CalculationTableDto request) {
        return calculationRepository.update(id, request);
    }

    @Override
    public ResponseEntity<?> deleteCalculationTable(String id) {
        return calculationRepository.delete(id);
    }
}
