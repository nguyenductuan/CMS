package com.vt.cms.Exception;

import com.vt.cms.model.resp.APIRessponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    /**
     * Xử lý handle validation. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new APIRessponse(400, "Dữ liệu không hợp lệ", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    /**
     * Xử lý handle constraint violation. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.info("Constraint validation failed: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new APIRessponse(400, "Tham số yêu cầu không hợp lệ", null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    /**
     * Xử lý handle not readable. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> handleNotReadable(HttpMessageNotReadableException ex) {
        log.debug("Malformed request body", ex);
        return ResponseEntity.badRequest()
                .body(new APIRessponse(400, "Dữ liệu không hợp lệ", null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    /**
     * Xử lý handle illegal argument. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.info("Request rejected: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new APIRessponse(400, ex.getMessage(), null));
    }

    @ExceptionHandler(BusinessException.class)
    /**
     * Xử lý handle business exception. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> handleBusinessException(BusinessException ex) {
        log.info("Business rule rejected: code={}, message={}", ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new APIRessponse(409, ex.getMessage(), Map.of("code", ex.getCode())));
    }

    @ExceptionHandler(IllegalStateException.class)
    /**
     * Xử lý handle illegal state. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> handleIllegalState(IllegalStateException ex) {
        log.info("Business state conflict: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new APIRessponse(409, ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    /**
     * Xử lý handle exception. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ResponseEntity<APIRessponse> handleException(Exception ex) {
        log.error("Unhandled application exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIRessponse(500, "Hệ thống đang bận"));
    }
}
