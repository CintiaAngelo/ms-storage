package com.javer.ms_storage.controller;

import com.javer.ms_storage.dto.ClienteRequestDTO;
import com.javer.ms_storage.dto.ClienteResponseDTO;
import com.javer.ms_storage.exception.ClienteNotFoundException;
import com.javer.ms_storage.exception.GlobalExceptionHandler;
import com.javer.ms_storage.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ClienteResponseDTO responseDTO;
    private ClienteRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        responseDTO = ClienteResponseDTO.builder()
                .id(1L)
                .nome("João Silva")
                .telefone(11999998888L)
                .correntista(true)
                .scoreCredito(750.0f)
                .saldoCc(2500.0f)
                .build();

        requestDTO = ClienteRequestDTO.builder()
                .nome("João Silva")
                .telefone(11999998888L)
                .correntista(true)
                .scoreCredito(750.0f)
                .saldoCc(2500.0f)
                .build();
    }

    @Test
    void criar_deveRetornar201_quandoDadosValidos() throws Exception {
        when(clienteService.criar(any(ClienteRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(clienteService, times(1)).criar(any(ClienteRequestDTO.class));
    }

    @Test
    void criar_deveRetornar400_quandoDadosInvalidos() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(clienteService, never()).criar(any());
    }

    @Test
    void buscarPorId_deveRetornar200_quandoIdExiste() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void buscarPorId_deveRetornar404_quandoIdNaoExiste() throws Exception {
        when(clienteService.buscarPorId(99L))
                .thenThrow(new ClienteNotFoundException(99L));

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void listarTodos_deveRetornar200_comListaDeClientes() throws Exception {
        when(clienteService.listarTodos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    void atualizar_deveRetornar200_quandoIdExiste() throws Exception {
        when(clienteService.atualizar(eq(1L), any(ClienteRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void atualizar_deveRetornar404_quandoIdNaoExiste() throws Exception {
        when(clienteService.atualizar(eq(99L), any(ClienteRequestDTO.class)))
                .thenThrow(new ClienteNotFoundException(99L));

        mockMvc.perform(put("/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveRetornar204_quandoIdExiste() throws Exception {
        doNothing().when(clienteService).deletar(1L);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).deletar(1L);
    }

    @Test
    void deletar_deveRetornar404_quandoIdNaoExiste() throws Exception {
        doThrow(new ClienteNotFoundException(99L)).when(clienteService).deletar(99L);

        mockMvc.perform(delete("/clientes/99"))
                .andExpect(status().isNotFound());
    }
}