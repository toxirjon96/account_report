package uz.infinity.zero_one_demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationCount {
    private String firstName;
    private String lastName;
    private String pinfl;
    private Double totalCash;
    private Integer organizationCount;
    private List<OrganizationInfo> organizationList = new ArrayList<>();
}

