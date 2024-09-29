package uz.infinity.zero_one_demo.repository.entity_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.infinity.zero_one_demo.entity.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    List<Employee> findByPinfl(String pinfl);
}
