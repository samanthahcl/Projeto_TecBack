package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioDTO dto) {
        return Usuario.builder()
                .id(dto.getId())
                .nomeCompleto(dto.getNomeCompleto())
                .dataNascimento(dto.getDataNascimento())
                .email(dto.getEmail())
                .senhaHash(dto.getSenha())
                .cpfCnpj(dto.getCpfCnpj())
                .perfil(dto.getPerfil())
                .build();
    }

    public UsuarioDTO toDTO(Usuario u) {
        return UsuarioDTO.builder()
                .id(u.getId())
                .nomeCompleto(u.getNomeCompleto())
                .dataNascimento(u.getDataNascimento())
                .email(u.getEmail())
                .senha(u.getSenhaHash())
                .cpfCnpj(u.getCpfCnpj())
                .perfil(u.getPerfil())
                .build();
    }
}