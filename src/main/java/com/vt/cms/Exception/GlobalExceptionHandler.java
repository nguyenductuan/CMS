package com.vt.cms.Exception;


import com.vt.cms.model.resp.APIRessponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }
    /**
     * Lỗi @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIRessponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        APIRessponse response = new APIRessponse(
                400,
                "Dữ liệu không hợp lệ",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    /**
     * JSON gửi lên bị sai format
     * Ví dụ thiếu dấu } hoặc truyền String vào Integer
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIRessponse> handleNotReadable(
            HttpMessageNotReadableException ex
    ) {

        APIRessponse response = new APIRessponse(
                400,
                "Dữ liệu không hợp lệ",
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    /**
     * Các lỗi 400 khác
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIRessponse> handleIllegalArgument(
            IllegalArgumentException ex
    ) {

        APIRessponse response = new APIRessponse(
                400,
                "Dữ liệu không hợp lệ",
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    /**
     * Các lỗi còn lại -> 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIRessponse> handleException(
            Exception ex
    ) {

        // Log để developer biết lỗi thật
        ex.printStackTrace();

        APIRessponse response = new APIRessponse(
                500,
                "Hệ thống đang bận"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
