package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.ClienteDTO;
import br.uniesp.si.techback.mapper.ClienteMapper;
import br.uniesp.si.techback.model.Cliente;
import br.uniesp.si.techback.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteDTO incluir(ClienteDTO dto) {
        return clienteMapper.toDTO(clienteRepository.save(clienteMapper.toEntity(dto)));
    }
    public List<ClienteDTO> listar() {
        return clienteRepository.findAll().stream().map(clienteMapper::toDTO).toList();
    }
    public ClienteDTO buscarPorId(Long id) {
        return clienteMapper.toDTO(clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado: " + id)));
    }
    public ClienteDTO atualizar(Long id, ClienteDTO dto) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado: " + id));
        c.setNome(dto.getNome()); c.setCpf(dto.getCpf());
        c.setTelefone(dto.getTelefone()); c.setEndereco(dto.getEndereco());
        return clienteMapper.toDTO(clienteRepository.save(c));
    }
    public void deletar(Long id) {
        clienteRepository.delete(clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado: " + id)));
    }
}
