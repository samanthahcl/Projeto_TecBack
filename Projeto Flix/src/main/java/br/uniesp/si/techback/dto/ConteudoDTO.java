package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConteudoDTO {

    private Long id;

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Tipo é obrigatório (FILME ou SERIE)")
    @Pattern(regexp = "(?i)FILME|SERIE", message = "Tipo deve ser FILME ou SERIE")
    private String tipo;

    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1888, message = "Ano mínimo é 1888")
    @Max(value = 2100, message = "Ano máximo é 2100")
    private Integer ano;

    @NotNull(message = "Duração em minutos é obrigatória")
    @Min(value = 1, message = "Duração mínima é 1 minuto")
    private Integer duracaoMinutos;

    @NotNull(message = "Relevância é obrigatória")
    @DecimalMin(value = "0.00", message = "Relevância mínima é 0")
    @DecimalMax(value = "10.00", message = "Relevância máxima é 10")
    private BigDecimal relevancia;

    private String sinopse;
    private String trailerUrl;
    private String genero;
}
