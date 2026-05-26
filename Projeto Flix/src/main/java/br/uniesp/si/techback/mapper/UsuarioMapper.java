package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toEntity(UsuarioDTO dto) {
        return Usuario.builder().id(dto.getId()).nome(dto.getNome())
                .email(dto.getEmail()).senha(dto.getSenha()).build();
    }
    public UsuarioDTO toDTO(Usuario u) {
        return UsuarioDTO.builder().id(u.getId()).nome(u.getNome())
                .email(u.getEmail()).senha(u.getSenha()).build();
    }
}
