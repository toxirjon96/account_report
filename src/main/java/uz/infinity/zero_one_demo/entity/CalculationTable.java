package uz.infinity.zero_one_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.infinity.zero_one_demo.constant.CalculationType;
import uz.infinity.zero_one_demo.constant.TableName;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.CALCULATION_TABLE)
public class CalculationTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "FK_CT_EMPLOYEE_ID"))
    private Employee employee;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "RATE")
    private Double rate;

    @Column(name = "DATE")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_CT_ORGANIZATION_ID"))
    private Organization organization;

    @Column(name = "CALCULATION_TYPE")
    private CalculationType calculationType;

    public CalculationTable(Employee employee, Double amount, Double rate, LocalDate date, Organization organization, CalculationType calculationType) {
        this.employee = employee;
        this.amount = amount;
        this.rate = rate;
        this.date = date;
        this.organization = organization;
        this.calculationType = calculationType;
    }
}
