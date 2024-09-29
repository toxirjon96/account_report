package uz.infinity.zero_one_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.infinity.zero_one_demo.constant.TableName;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.EMPLOYEE)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "FIRST_NAME", length = 60, nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", length = 60, nullable = false)
    private String lastName;

    @Column(name = "PINFL", length = 14, nullable = false)
    private String pinfl;

    @Column(name = "HIRE_DATE")
    private LocalDate hireDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_EMP_ORGANIZATION_ID"))
    private Organization organization;

    public Employee(String firstName, String lastName,
                    String pinfl, LocalDate hireDate,
                    Organization organization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pinfl = pinfl;
        this.hireDate = hireDate;
        this.organization = organization;
    }
}
