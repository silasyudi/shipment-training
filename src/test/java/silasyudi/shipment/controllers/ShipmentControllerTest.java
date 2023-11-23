package silasyudi.shipment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import silasyudi.shipment.dtos.shipment.ShipmentGetListResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentGetResponseDto;
import silasyudi.shipment.dtos.shipment.ShipmentPostRequestDto;
import silasyudi.shipment.dtos.system.ApiErrorDto;
import silasyudi.shipment.models.Shipment;
import silasyudi.shipment.repositories.ShipmentRepository;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShipmentRepository repository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testPostWhenDtoHaveSameOriginAndDestinationShouldReturnPreconditionFailed() throws Exception {
        String data = objectMapper.writeValueAsString(new ShipmentPostRequestDto("PB", "PB", 1.));

        MvcResult result = mockMvc.perform(
                        post("/shipment/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data)
                )
                .andExpect(status().isPreconditionFailed())
                .andReturn();

        String retorno = result.getResponse().getContentAsString(Charset.forName("UTF-8"));
        ApiErrorDto dto = objectMapper.readValue(retorno, ApiErrorDto.class);

        assertEquals(HttpStatus.PRECONDITION_FAILED.value(), dto.getStatus());
        assertEquals(HttpStatus.PRECONDITION_FAILED.getReasonPhrase(), dto.getCode());
        assertEquals("Não é permitido os dados de origem e destino serem iguais.", dto.getMessage());
    }

    @Test
    public void testGet() throws Exception {
        repository.save(new Shipment(null, "SP", "PB", 1.0));

        MvcResult result = mockMvc.perform(
                        get("/shipment/")
                )
                .andExpect(status().isOk())
                .andReturn();

        String retorno = result.getResponse().getContentAsString(Charset.forName("UTF-8"));
        ShipmentGetListResponseDto dto = objectMapper.readValue(retorno, ShipmentGetListResponseDto.class);

        assertEquals(1, dto.getList().size());

        ShipmentGetResponseDto getResponseDto = dto.getList().get(0);
        assertEquals(1, getResponseDto.getId());
        assertEquals("SP", getResponseDto.getOrigin());
        assertEquals("PB", getResponseDto.getDestination());
        assertEquals(1, getResponseDto.getPrice());
    }
}
