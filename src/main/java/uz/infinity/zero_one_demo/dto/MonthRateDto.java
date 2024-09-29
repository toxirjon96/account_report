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
public class MonthRateDto {
    @Schema(description = "yyyy.dd formatida bo'lishi kerak")
    private String yearMonth;
    private Double workRate;
}
