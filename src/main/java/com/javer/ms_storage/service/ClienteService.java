package com.javer.ms_storage.service;

import com.javer.ms_storage.dto.ClienteRequestDTO;
import com.javer.ms_storage.dto.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {
    ClienteResponseDTO criar(ClienteRequestDTO dto);
    ClienteResponseDTO buscarPorId(Long id);
    List<ClienteResponseDTO> listarTodos();
    ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto);
    void deletar(Long id);
}