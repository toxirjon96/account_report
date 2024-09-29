package uz.infinity.zero_one_demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthRateResponse {
    private String firstName;
    private String lastName;
    private String pinfl;
    private Double totalRate;
}
