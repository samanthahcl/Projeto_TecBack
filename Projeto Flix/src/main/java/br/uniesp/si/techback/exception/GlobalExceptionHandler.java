package br.uniesp.si.techback.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Mantem o retorno 400 para erros de validacao de entrada.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        ProblemDetail problem = criarProblemDetail(HttpStatus.BAD_REQUEST, "Dados inválidos na requisição", request);
        problem.setProperty("errors", fieldErrors);
        return resposta(problem);
    }

    /**
     * Trata a excecao customizada da aplicacao e retorna 400.
     */
    @ExceptionHandler(CustomBeanException.class)
    public ResponseEntity<ProblemDetail> handleCustomBeanException(CustomBeanException ex,
                                                                    HttpServletRequest request) {
        return resposta(criarProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException ex,
                                                                        HttpServletRequest request) {
        String detail = ex.getReason() == null ? ex.getStatusCode().toString() : ex.getReason();
        return resposta(criarProblemDetail(ex.getStatusCode(), detail, request));
    }
    /**
     * Fallback simples para qualquer erro nao tratado.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex,
                                                                 HttpServletRequest request) {
        return resposta(criarProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado", request));
    }

    private ProblemDetail criarProblemDetail(HttpStatusCode status, String detail, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setProperty("path", request.getRequestURI());
        return problem;
    }

    private ResponseEntity<ProblemDetail> resposta(ProblemDetail problem) {
        return ResponseEntity.status(problem.getStatus())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
