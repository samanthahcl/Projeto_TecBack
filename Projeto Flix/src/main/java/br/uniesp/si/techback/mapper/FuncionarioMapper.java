package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.model.Funcionario;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioMapper {

    public Funcionario toEntity(FuncionarioDTO dto) {
        return Funcionario.builder()
                .nome(dto.getNome())
                .cargo(dto.getCargo())
                .build();
    }

    public FuncionarioDTO toDTO(Funcionario funcionario) {
        return FuncionarioDTO.builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .cargo(funcionario.getCargo())
                .build();
    }
}