package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioDTO criar(UsuarioDTO dto) {
        // verifica se o e-mail já existe
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setEmail(dto.getEmail());
        usuario.setCpfCnpj(dto.getCpfCnpj());
        usuario.setPerfil(dto.getPerfil() == null ? "USER" : dto.getPerfil().toUpperCase());

        // RF3 - guardar senha como hash BCrypt
        usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        Usuario salvo = usuarioRepository.save(usuario);
        return toDTO(salvo);
    }

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id));
        return toDTO(usuario);
    }

    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id));

        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setEmail(dto.getEmail());
        usuario.setCpfCnpj(dto.getCpfCnpj());

        // só atualiza a senha se ela foi informada
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        }

        return toDTO(usuarioRepository.save(usuario));
    }

    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // nunca retorna a senha no DTO
    private UsuarioDTO toDTO(Usuario u) {
        return UsuarioDTO.builder()
                .id(u.getId())
                .nomeCompleto(u.getNomeCompleto())
                .dataNascimento(u.getDataNascimento())
                .email(u.getEmail())
                .cpfCnpj(u.getCpfCnpj())
                .perfil(u.getPerfil())
                .build();
    }
}
