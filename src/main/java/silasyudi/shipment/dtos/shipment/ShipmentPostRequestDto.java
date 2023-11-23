package silasyudi.shipment.dtos.shipment;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentPostRequestDto {
    @Length(min = 2, max = 2)
    private String origin;
    @Length(min = 2, max = 2)
    private String destination;
    @Positive
    private Double price;
}
