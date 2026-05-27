package br.uniesp.si.techback.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhaForteValidator implements ConstraintValidator<SenhaForte, String> {

    @Override
    public boolean isValid(String senha, ConstraintValidatorContext context) {
        if (senha == null || senha.isBlank()) {
            return true; // use @NotBlank separado se for obrigatório
        }

        // mínimo 8 caracteres
        if (senha.length() < 8) return false;

        // pelo menos uma letra maiúscula
        if (!senha.matches(".*[A-Z].*")) return false;

        // pelo menos uma letra minúscula
        if (!senha.matches(".*[a-z].*")) return false;

        // pelo menos um número
        if (!senha.matches(".*[0-9].*")) return false;

        // pelo menos um caractere especial
        if (!senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|,.<>?].*")) return false;

        return true;
    }
}
