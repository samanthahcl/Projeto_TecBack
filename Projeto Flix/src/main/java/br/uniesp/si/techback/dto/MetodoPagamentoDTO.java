package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagamentoDTO {

    private Long id;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    @NotBlank(message = "Bandeira é obrigatória")
    private String bandeira;

    @NotBlank(message = "Últimos 4 dígitos são obrigatórios")
    @Size(min = 4, max = 4, message = "Informe exatamente 4 dígitos")
    private String ultimos4;

    @NotNull(message = "Mês de expiração é obrigatório")
    @Min(value = 1, message = "Mês deve ser entre 1 e 12")
    @Max(value = 12, message = "Mês deve ser entre 1 e 12")
    private Integer mesExp;

    @NotNull(message = "Ano de expiração é obrigatório")
    @Min(value = 2024, message = "Cartão vencido")
    private Integer anoExp;

    @NotBlank(message = "Nome do portador é obrigatório")
    private String nomePortador;

    @NotBlank(message = "Token do gateway é obrigatório")
    private String tokenGateway;
}
