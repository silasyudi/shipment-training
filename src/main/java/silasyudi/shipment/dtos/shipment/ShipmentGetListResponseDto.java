package silasyudi.shipment.dtos.shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentGetListResponseDto {
    private List<ShipmentGetResponseDto> list;
}
