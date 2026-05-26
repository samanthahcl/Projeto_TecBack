package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProdutoDTO {
    private Long id;
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    private String descricao;
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    private BigDecimal preco;
    @Min(value = 0, message = "Estoque não pode ser negativo")
    private Integer estoque;
}
