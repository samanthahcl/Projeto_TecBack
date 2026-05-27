package br.uniesp.si.techback.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfCnpjValidator implements ConstraintValidator<CpfCnpj, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // se o campo for nulo ou vazio, considera válido
        // (use @NotBlank separado se o campo for obrigatório)
        if (value == null || value.isBlank()) {
            return true;
        }

        // remove pontos, traços e barras
        String soDigitos = value.replaceAll("\\D", "");

        if (soDigitos.length() == 11) {
            return validarCPF(soDigitos);
        }
        if (soDigitos.length() == 14) {
            return validarCNPJ(soDigitos);
        }

        return false; // tamanho diferente de 11 ou 14 é inválido
    }

    // --------------------------------------------------------
    // Validação de CPF
    // --------------------------------------------------------
    private boolean validarCPF(String cpf) {
        // CPF com todos os dígitos iguais é inválido (ex: 111.111.111-11)
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) primeiroDigito = 0;

        if (primeiroDigito != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) segundoDigito = 0;

        return segundoDigito == Character.getNumericValue(cpf.charAt(10));
    }

    // --------------------------------------------------------
    // Validação de CNPJ
    // --------------------------------------------------------
    private boolean validarCNPJ(String cnpj) {
        if (cnpj.chars().distinct().count() == 1) {
            return false;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos1[i];
        }
        int primeiroDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);
        if (primeiroDigito != Character.getNumericValue(cnpj.charAt(12))) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos2[i];
        }
        int segundoDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        return segundoDigito == Character.getNumericValue(cnpj.charAt(13));
    }
}
