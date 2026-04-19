package com.vt.cms.model.resp;

public class APIRessponse {
    private int status;
    private String message;

    public APIRessponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
