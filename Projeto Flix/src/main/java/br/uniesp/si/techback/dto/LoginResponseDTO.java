package br.uniesp.si.techback.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

    private Long usuarioId;
    private String nomeCompleto;
    private String email;
    private String perfil;
    private String mensagem;
}