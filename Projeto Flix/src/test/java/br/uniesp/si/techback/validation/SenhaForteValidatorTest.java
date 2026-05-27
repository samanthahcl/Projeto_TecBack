package br.uniesp.si.techback.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do SenhaForteValidator")
class SenhaForteValidatorTest {

    private SenhaForteValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SenhaForteValidator();
    }

    @Test
    @DisplayName("deve aceitar senha forte")
    void deveAceitarSenhaForte() {
        assertThat(validator.isValid("Iespflix@2024", null)).isTrue();
    }

    @Test
    @DisplayName("deve rejeitar senha com menos de 8 caracteres")
    void deveRejeitarSenhaCurta() {
        assertThat(validator.isValid("Ab@1", null)).isFalse();
    }

    @Test
    @DisplayName("deve rejeitar senha sem letra maiúscula")
    void deveRejeitarSenhaSemMaiuscula() {
        assertThat(validator.isValid("abc@12345", null)).isFalse();
    }

    @Test
    @DisplayName("deve rejeitar senha sem letra minúscula")
    void deveRejeitarSenhaSemMinuscula() {
        assertThat(validator.isValid("ABC@12345", null)).isFalse();
    }

    @Test
    @DisplayName("deve rejeitar senha sem número")
    void deveRejeitarSenhaSemNumero() {
        assertThat(validator.isValid("Abcde@fgh", null)).isFalse();
    }

    @Test
    @DisplayName("deve rejeitar senha sem caractere especial")
    void deveRejeitarSenhaSemEspecial() {
        assertThat(validator.isValid("Abcde1234", null)).isFalse();
    }
}
