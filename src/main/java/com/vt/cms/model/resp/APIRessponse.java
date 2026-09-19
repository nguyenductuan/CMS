package com.vt.cms.model.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIRessponse {

    private int code;
    private String message;
    private Object data;
    private Map<String, String> errors;

    // Thành công
    public APIRessponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Có data
    public APIRessponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = null;
    }

    // Có errors
    public APIRessponse(
            int code,
            String message,
            Map<String, String> errors
    ) {
        this.code = code;
        this.message = message;
        this.data = null;
        this.errors = errors;
    }
}
