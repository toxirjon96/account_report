package uz.infinity.zero_one_demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryVacation {
    private String organizationId;
    private String date;
    private String cashType;
    private Double amount;
    private String organizationName;
    private String parentOrganizationName;
}
