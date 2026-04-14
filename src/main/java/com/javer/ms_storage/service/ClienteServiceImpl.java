package com.javer.ms_storage.service;

import com.javer.ms_storage.dto.ClienteRequestDTO;
import com.javer.ms_storage.dto.ClienteResponseDTO;
import com.javer.ms_storage.entity.Cliente;
import com.javer.ms_storage.exception.ClienteNotFoundException;
import com.javer.ms_storage.repository.ClienteRepository;
import com.javer.ms_storage.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        Cliente cliente = toEntity(dto);
        Cliente salvo = clienteRepository.save(cliente);
        return toResponseDTO(salvo);
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        return toResponseDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));

        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCorrentista(dto.getCorrentista());
        cliente.setScoreCredito(dto.getScoreCredito());
        cliente.setSaldoCc(dto.getSaldoCc());

        return toResponseDTO(clienteRepository.save(cliente));
    }

    @Override
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }
        clienteRepository.deleteById(id);
    }

    private Cliente toEntity(ClienteRequestDTO dto) {
        return Cliente.builder()
                .nome(dto.getNome())
                .telefone(dto.getTelefone())
                .correntista(dto.getCorrentista())
                .scoreCredito(dto.getScoreCredito())
                .saldoCc(dto.getSaldoCc())
                .build();
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .telefone(cliente.getTelefone())
                .correntista(cliente.getCorrentista())
                .scoreCredito(cliente.getScoreCredito())
                .saldoCc(cliente.getSaldoCc())
                .build();
    }
}
