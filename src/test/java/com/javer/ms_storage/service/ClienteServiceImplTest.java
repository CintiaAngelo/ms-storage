package com.javer.ms_storage.service;

import com.javer.ms_storage.dto.ClienteRequestDTO;
import com.javer.ms_storage.dto.ClienteResponseDTO;
import com.javer.ms_storage.entity.Cliente;
import com.javer.ms_storage.exception.ClienteNotFoundException;
import com.javer.ms_storage.repository.ClienteRepository;
import com.javer.ms_storage.service.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
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
    void criar_deveRetornarClienteResponseDTO_quandoDadosValidos() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponseDTO response = clienteService.criar(requestDTO);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNome()).isEqualTo("João Silva");
        assertThat(response.getSaldoCc()).isEqualTo(2500.0f);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void buscarPorId_deveRetornarCliente_quandoIdExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteResponseDTO response = clienteService.buscarPorId(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNome()).isEqualTo("João Silva");
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_deveLancarException_quandoIdNaoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorId(99L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("99");

        verify(clienteRepository, times(1)).findById(99L);
    }

    @Test
    void listarTodos_deveRetornarListaDeClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<ClienteResponseDTO> response = clienteService.listarTodos();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getNome()).isEqualTo("João Silva");
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void listarTodos_deveRetornarListaVazia_quandoNaoHaClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of());

        List<ClienteResponseDTO> response = clienteService.listarTodos();

        assertThat(response).isEmpty();
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void atualizar_deveRetornarClienteAtualizado_quandoIdExiste() {
        Cliente atualizado = Cliente.builder()
                .id(1L)
                .nome("João Atualizado")
                .telefone(11999998888L)
                .correntista(false)
                .scoreCredito(800.0f)
                .saldoCc(3000.0f)
                .build();

        ClienteRequestDTO updateDTO = ClienteRequestDTO.builder()
                .nome("João Atualizado")
                .telefone(11999998888L)
                .correntista(false)
                .scoreCredito(800.0f)
                .saldoCc(3000.0f)
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(atualizado);

        ClienteResponseDTO response = clienteService.atualizar(1L, updateDTO);

        assertThat(response.getNome()).isEqualTo("João Atualizado");
        assertThat(response.getSaldoCc()).isEqualTo(3000.0f);
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void atualizar_deveLancarException_quandoIdNaoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.atualizar(99L, requestDTO))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("99");

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deletar_deveDeletarCliente_quandoIdExiste() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        assertThatCode(() -> clienteService.deletar(1L))
                .doesNotThrowAnyException();

        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletar_deveLancarException_quandoIdNaoExiste() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> clienteService.deletar(99L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("99");

        verify(clienteRepository, never()).deleteById(any());
    }
}
