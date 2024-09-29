package uz.infinity.zero_one_demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationInfo {
    private String id;
    private String organizationName;
    private String parentOrganizationName;
    private String regionName;
}
