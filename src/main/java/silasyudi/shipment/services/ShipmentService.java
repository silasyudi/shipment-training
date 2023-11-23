package silasyudi.shipment.services;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import silasyudi.shipment.dtos.shipment.ShipmentGetListResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentGetResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentPostRequestDto;
import silasyudi.shipment.dtos.shipment.ShipmentPostResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentPutRequestDto;
import silasyudi.shipment.dtos.shipment.ShipmentPutResponseDto;
import silasyudi.shipment.models.Shipment;
import silasyudi.shipment.repositories.ShipmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ModelMapper modelMapper;

    public ShipmentService(ShipmentRepository shipmentRepository, ModelMapper modelMapper) {
        this.shipmentRepository = shipmentRepository;
        this.modelMapper = modelMapper;
    }

    public ShipmentPostResponseDto post(ShipmentPostRequestDto dto) {
        checkOriginAndDestinationIsSame(dto.getOrigin(), dto.getDestination());
        checkOriginAndDestinationAlreadyExists(dto.getOrigin(), dto.getDestination());

        Shipment shipment = modelMapper.map(dto, Shipment.class);
        Shipment shipmentSaved = shipmentRepository.save(shipment);
        return modelMapper.map(shipmentSaved, ShipmentPostResponseDto.class);
    }

    public ShipmentGetListResponseDto list() {
        List<Shipment> shipments = shipmentRepository.findAll();
        List<ShipmentGetResponseDto> list = shipments.stream().map(f -> modelMapper.map(f, ShipmentGetResponseDto.class)).toList();
        return new ShipmentGetListResponseDto(list);
    }

    public ShipmentPutResponseDto put(Long id, ShipmentPutRequestDto dto) {
        Shipment shipment = getShipmentById(id);

        checkOriginAndDestinationIsSame(dto.getOrigin(), dto.getDestination());
        checkOriginAndDestinationAlreadyExists(shipment, dto.getOrigin(), dto.getDestination());

        modelMapper.map(dto, shipment);
        shipmentRepository.save(shipment);

        return modelMapper.map(shipment, ShipmentPutResponseDto.class);
    }

    public void delete(Long id) {
        Shipment shipment = getShipmentById(id);
        shipmentRepository.delete(shipment);
    }

    public void testeException(Long id) {
        try {
            Optional<Shipment> shipment = shipmentRepository.findById(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "deu ruim", e);
        }
    }

    private Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void checkOriginAndDestinationIsSame(String origin, String destination) {
        if (origin.equalsIgnoreCase(destination)) {
            throw new ResponseStatusException(
                    HttpStatus.PRECONDITION_FAILED,
                    "Não é permitido os dados de origem e destino serem iguais.");
        }
    }

    private void checkOriginAndDestinationAlreadyExists(String origin, String destination) {
        if (shipmentRepository.findByOriginAndDestination(origin, destination).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um frete com os mesmos dados de origem e destino.");
        }
    }

    private void checkOriginAndDestinationAlreadyExists(Shipment oldShipment, String newOrigin, String newDestination) {
        if (oldShipment.getOrigin().equalsIgnoreCase(newOrigin)
                && oldShipment.getDestination().equalsIgnoreCase(newDestination)) {
            return;
        }

        checkOriginAndDestinationAlreadyExists(newOrigin, newDestination);
    }
}
