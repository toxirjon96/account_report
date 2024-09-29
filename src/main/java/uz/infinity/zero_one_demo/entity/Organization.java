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
@Table(name = TableName.ORGANIZATION)
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REGION_ID", foreignKey = @ForeignKey(name = "FK_REGION_ID"))
    private Region region;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT_ID", foreignKey = @ForeignKey(name = "FK_PARENT_ID"))
    private Organization parent;

    public Organization(String name, Region region, Organization parent) {
        this.name = name;
        this.region = region;
        this.parent = parent;
    }

    public Organization(String name, Region region) {
        this.name = name;
        this.region = region;
    }
}
