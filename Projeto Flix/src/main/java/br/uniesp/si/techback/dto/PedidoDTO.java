package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PedidoDTO {
    private Long id;
    @NotNull(message = "Data do pedido é obrigatória")
    private LocalDate dataPedido;
    @NotBlank(message = "Status é obrigatório")
    private String status;
    @NotNull(message = "Valor total é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private BigDecimal valorTotal;
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
}
