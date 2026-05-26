package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CategoriaDTO {
    private Long id;
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    private String descricao;
}
