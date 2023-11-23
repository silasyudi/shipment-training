package silasyudi.shipment.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import silasyudi.shipment.dtos.shipment.ShipmentGetListResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentPostRequestDto;
import silasyudi.shipment.dtos.shipment.ShipmentPostResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentPutRequestDto;
import silasyudi.shipment.dtos.shipment.ShipmentPutResponseDto;
import silasyudi.shipment.services.ShipmentService;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShipmentPostResponseDto> post(@Valid @RequestBody ShipmentPostRequestDto dto) {
        ShipmentPostResponseDto response = shipmentService.post(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShipmentGetListResponseDto> list() {
        ShipmentGetListResponseDto response = shipmentService.list();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShipmentPutResponseDto> put(@PathVariable Long id, @Valid @RequestBody ShipmentPutRequestDto dto) {
        ShipmentPutResponseDto response = shipmentService.put(id, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> put(@PathVariable Long id) {
        shipmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
