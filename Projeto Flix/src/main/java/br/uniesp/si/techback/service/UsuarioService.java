package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.CadastroUsuarioDTO;
import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MetodoPagamentoService metodoPagamentoService;

    @Transactional
    public UsuarioDTO criar(CadastroUsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        if (usuarioRepository.existsByCpfCnpj(dto.getCpfCnpj())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF/CNPJ já cadastrado");
        }

        if (!dto.getSenha().equals(dto.getConfirmarSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha e confirmação de senha não conferem");
        }

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setEmail(dto.getEmail());
        usuario.setCpfCnpj(dto.getCpfCnpj());
        usuario.setPerfil("USER");

        // RF3 - guardar senha como hash BCrypt
        usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        Usuario salvo = usuarioRepository.save(usuario);
        metodoPagamentoService.cadastrarCartaoInicial(salvo.getId(), dto);
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
