package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.ClienteDTO;
import br.uniesp.si.techback.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public Cliente toEntity(ClienteDTO dto) {
        return Cliente.builder().id(dto.getId()).nome(dto.getNome())
                .cpf(dto.getCpf()).telefone(dto.getTelefone()).endereco(dto.getEndereco()).build();
    }
    public ClienteDTO toDTO(Cliente c) {
        return ClienteDTO.builder().id(c.getId()).nome(c.getNome())
                .cpf(c.getCpf()).telefone(c.getTelefone()).endereco(c.getEndereco()).build();
    }
}
