package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.mapper.UsuarioMapper;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioDTO incluir(UsuarioDTO dto) {
        return usuarioMapper.toDTO(usuarioRepository.save(usuarioMapper.toEntity(dto)));
    }
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDTO).toList();
    }
    public UsuarioDTO buscarPorId(Long id) {
        return usuarioMapper.toDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id)));
    }
    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id));
        u.setNome(dto.getNome()); u.setEmail(dto.getEmail()); u.setSenha(dto.getSenha());
        return usuarioMapper.toDTO(usuarioRepository.save(u));
    }
    public void deletar(Long id) {
        usuarioRepository.delete(usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id)));
    }
}
