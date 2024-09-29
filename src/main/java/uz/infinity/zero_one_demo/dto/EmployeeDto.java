package uz.infinity.zero_one_demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private String pinfl;
    @Schema(description = "Sana yyyy.MM.dd formatida bo'lishi kerak")
    private String hireDate;
    private String organizationId;
}
