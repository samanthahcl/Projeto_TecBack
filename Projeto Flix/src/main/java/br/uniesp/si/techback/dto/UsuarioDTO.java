package br.uniesp.si.techback.dto;

import br.uniesp.si.techback.validation.CpfCnpj;
import br.uniesp.si.techback.validation.SenhaForte;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nomeCompleto;

    @NotNull(message = "Data de nascimento é obrigatória")
    private LocalDate dataNascimento;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @SenhaForte
    private String senha;

    @CpfCnpj
    private String cpfCnpj;

    private String perfil;
}
