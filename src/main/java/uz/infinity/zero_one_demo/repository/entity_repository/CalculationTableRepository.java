package uz.infinity.zero_one_demo.repository.entity_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.infinity.zero_one_demo.entity.CalculationTable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalculationTableRepository extends JpaRepository<CalculationTable, String> {
    @Query(value = "from CalculationTable t where t.date BETWEEN :startDate AND :endDate AND t.rate>= :workRate")
    List<CalculationTable> getAllBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("workRate") Double workRate);

    @Query(value = "from CalculationTable t where t.date BETWEEN :startDate AND :endDate")
    List<CalculationTable> getAllRegionWorkerBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "from CalculationTable t where t.date BETWEEN :startDate AND :endDate AND (t.organization.id =:organizationId)")
    List<CalculationTable> getAllOrganizationWorkerBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("organizationId") String organizationId
    );

    List<CalculationTable> findByOrganizationId(String organizationId);
}
