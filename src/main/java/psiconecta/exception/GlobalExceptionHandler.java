package psiconecta.exception;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarRegraNegocio(
            IllegalArgumentException exception) {

        Map<String, Object> resposta = new HashMap<>();

        resposta.put(
                "timestamp",
                OffsetDateTime.now()
        );

        resposta.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );

        resposta.put(
                "erro",
                "Requisição inválida"
        );

        resposta.put(
                "mensagem",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resposta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException exception) {

        Map<String, String> campos = new HashMap<>();

        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(erro ->
                        campos.put(
                                erro.getField(),
                                erro.getDefaultMessage()
                        )
                );

        Map<String, Object> resposta = new HashMap<>();

        resposta.put(
                "timestamp",
                OffsetDateTime.now()
        );

        resposta.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );

        resposta.put(
                "erro",
                "Erro de validação"
        );

        resposta.put(
                "campos",
                campos
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resposta);
    }
}