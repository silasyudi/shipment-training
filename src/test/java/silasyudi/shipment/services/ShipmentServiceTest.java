package silasyudi.shipment.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import silasyudi.shipment.dtos.shipment.ShipmentGetListResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentGetResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentPostRequestDto;
import silasyudi.shipment.dtos.shipment.ShipmentPostResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentPutRequestDto;
import silasyudi.shipment.models.Shipment;
import silasyudi.shipment.repositories.ShipmentRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;
    @Spy
    private ModelMapper modelMapper = new ModelMapper();
    @InjectMocks
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPostWhenDtoIsOkShouldSaveInRepository() {
        // ARRANGE
        when(shipmentRepository.findByOriginAndDestination("SP", "PB")).thenReturn(Optional.empty());

        when(shipmentRepository.save(any(Shipment.class))).thenAnswer(invocationOnMock -> {
            Shipment shipment = invocationOnMock.getArgument(0);
            shipment.setId(1L);
            return shipment;
        });

        ShipmentPostRequestDto input = new ShipmentPostRequestDto("SP", "PB", 10.0);

        // ACT
        ShipmentPostResponseDto dto = shipmentService.post(input);

        // ASSERTS
        assertEquals(1, dto.getId());
        assertEquals("SP", dto.getOrigin());
        assertEquals("PB", dto.getDestination());
        assertEquals(10, dto.getPrice());

        verify(shipmentRepository).findByOriginAndDestination("SP", "PB");
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    public void testListWhenRepositoryIsEmpty() {
        when(shipmentRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        ShipmentGetListResponseDto dtoList = shipmentService.list();

        assertTrue(dtoList.getList().isEmpty());
        verify(shipmentRepository).findAll();
    }

    @Test
    public void testListWhenRepositoryIsFull() {
        when(shipmentRepository.findAll()).thenReturn(Arrays.asList(new Shipment(1L, "SP", "PB", 10.0)));

        ShipmentGetListResponseDto dtoList = shipmentService.list();

        assertEquals(1, dtoList.getList().size());

        ShipmentGetResponseDto dto = dtoList.getList().get(0);
        assertEquals(1, dto.getId());
        assertEquals("SP", dto.getOrigin());
        assertEquals("PB", dto.getDestination());
        assertEquals(10, dto.getPrice());

        verify(shipmentRepository).findAll();
    }

    @Test
    public void testPutWhenIdNotExists() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> shipmentService.put(1L, new ShipmentPutRequestDto("SP", "PB", 10.0))
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND", exception.getMessage());

        verify(shipmentRepository).findById(1L);
        verify(shipmentRepository, never()).save(any(Shipment.class));
        verify(shipmentRepository, never()).findByOriginAndDestination(anyString(), anyString());
    }

    @Test
    public void testTesteWhenRepositoryThrowException() {
        doThrow(new RuntimeException("error")).when(shipmentRepository).findById(-1L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> shipmentService.testeException(-1L)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("deu ruim", exception.getReason());
        assertInstanceOf(RuntimeException.class, exception.getCause());

        verify(shipmentRepository).findById(-1L);
    }

    @Test
    public void testDelete() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(new Shipment(1L, "SP", "PB", 10.0)));

        shipmentService.delete(1L);

        verify(shipmentRepository).findById(1L);
        verify(shipmentRepository).delete(any(Shipment.class));
    }

    @Test
    public void testDeleteException() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> shipmentService.delete(1L)
        );

        verify(shipmentRepository).findById(1L);
        verify(shipmentRepository, never()).delete(any(Shipment.class));
    }
}
