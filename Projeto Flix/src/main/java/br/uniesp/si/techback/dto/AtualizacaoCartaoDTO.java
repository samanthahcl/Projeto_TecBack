package br.uniesp.si.techback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AtualizacaoCartaoDTO {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Número do cartão é obrigatório")
    @Pattern(regexp = "\\d{13,19}", message = "Número do cartão deve possuir entre 13 e 19 dígitos")
    private String numeroCartao;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Validade do cartão é obrigatória")
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{4}", message = "Validade deve seguir o formato MM/AAAA")
    private String validadeCartao;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Código de segurança do cartão é obrigatório")
    @Pattern(regexp = "\\d{3,4}", message = "Código de segurança deve possuir 3 ou 4 dígitos")
    private String codigoSegurancaCartao;

    @NotBlank(message = "Nome do titular do cartão é obrigatório")
    @Size(max = 150, message = "Nome do titular deve ter no máximo 150 caracteres")
    private String nomeTitularCartao;
}
