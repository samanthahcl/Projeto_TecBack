package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaDTO {

    private Long id;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "ID do plano é obrigatório")
    private Long planoId;

    private String status;
    private LocalDateTime iniciadaEm;
    private LocalDateTime canceladaEm;
}
