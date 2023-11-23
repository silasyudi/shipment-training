package silasyudi.shipment.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 2)
    private String origin;
    @Column(length = 2)
    private String destination;
    @Column
    private Double price;
}
