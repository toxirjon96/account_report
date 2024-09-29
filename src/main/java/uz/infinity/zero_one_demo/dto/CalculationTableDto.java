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
public class CalculationTableDto {
    private String employeeId;
    private Double amount;
    private Double rate;
    @Schema(description = "Sana yyyy.MM.dd formatida bo'lishi kerak")
    private String date;
    private String organizationId;
    @Schema(type = "string", allowableValues = {"SALARY", "PENSION", "AWARD", "VACATION"})
    private String calculationType;
}
