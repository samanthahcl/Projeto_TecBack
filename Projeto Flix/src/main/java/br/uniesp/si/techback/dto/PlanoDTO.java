package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanoDTO {

    private Long id;

    @NotBlank(message = "Código do plano é obrigatório")
    private String codigo; // BASICO, PADRAO ou PREMIUM

    @NotNull(message = "Limite diário é obrigatório")
    @Min(value = 1, message = "Limite diário mínimo é 1")
    private Integer limiteDiario;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;
}
