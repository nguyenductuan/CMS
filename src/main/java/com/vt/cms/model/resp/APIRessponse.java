package com.vt.cms.model.resp;

import java.util.Map;

public class APIRessponse {
    private int code;
    private String message;
    private Object data;
    private Map<String, String> errors;

    public APIRessponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public APIRessponse(
            int code,
            String message,
            Object data
    ) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public APIRessponse(
            int code,
            String message,
            Map<String, String> errors
    ) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }
}
