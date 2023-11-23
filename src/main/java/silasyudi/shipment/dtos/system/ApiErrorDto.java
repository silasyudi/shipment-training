package silasyudi.shipment.dtos.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDto {
    private Integer status;
    private String code;
    private String message;
}
