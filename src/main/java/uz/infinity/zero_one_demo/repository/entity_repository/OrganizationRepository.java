package uz.infinity.zero_one_demo.repository.entity_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.infinity.zero_one_demo.entity.Organization;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {
    List<Organization> findByParent_Id(String parentId);
    List<Organization> findByRegion_Id(String regionId);

    Optional<Organization> findByName(String organizationName);

    boolean existsByNameAndRegion_Id(String name, String regionId);

    boolean existsByNameAndRegion_IdAndParent_Id(String name, String regionId, String organizationId);
}

