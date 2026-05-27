package br.uniesp.si.techback.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do CpfCnpjValidator")
class CpfCnpjValidatorTest {

    private CpfCnpjValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CpfCnpjValidator();
    }

    @Test
    @DisplayName("deve aceitar CPF válido")
    void deveAceitarCpfValido() {
        assertThat(validator.isValid("529.982.247-25", null)).isTrue();
    }

    @Test
    @DisplayName("deve aceitar CPF válido sem formatação")
    void deveAceitarCpfSemFormatacao() {
        assertThat(validator.isValid("52998224725", null)).isTrue();
    }

    @Test
    @DisplayName("deve rejeitar CPF inválido")
    void deveRejeitarCpfInvalido() {
        assertThat(validator.isValid("111.111.111-11", null)).isFalse();
    }

    @Test
    @DisplayName("deve rejeitar CPF com dígito verificador errado")
    void deveRejeitarCpfComDigitoErrado() {
        assertThat(validator.isValid("529.982.247-00", null)).isFalse();
    }

    @Test
    @DisplayName("deve aceitar null (campo opcional)")
    void deveAceitarNull() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    @DisplayName("deve rejeitar tamanho diferente de 11 ou 14 dígitos")
    void deveRejeitarTamanhoErrado() {
        assertThat(validator.isValid("1234567", null)).isFalse();
    }
}
