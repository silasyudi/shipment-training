package silasyudi.shipment.dtos.shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentPostResponseDto {
    private Long id;
    private String origin;
    private String destination;
    private Double price;
}
