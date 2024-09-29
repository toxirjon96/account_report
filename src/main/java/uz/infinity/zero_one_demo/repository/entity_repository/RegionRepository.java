package uz.infinity.zero_one_demo.repository.entity_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.infinity.zero_one_demo.entity.Region;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
    boolean existsByName(String name);
    Optional<Region> findByName(String name);
}
