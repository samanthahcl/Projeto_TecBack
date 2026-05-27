package br.uniesp.si.techback.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SenhaForteValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhaForte {
    String message() default "Senha fraca! Mínimo 8 caracteres com maiúscula, minúscula, número e caractere especial";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
