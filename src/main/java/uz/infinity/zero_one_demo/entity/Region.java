package uz.infinity.zero_one_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.infinity.zero_one_demo.constant.TableName;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.REGION)
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "NAME")
    private String name;

    public Region(String name) {
        this.name = name;
    }
}