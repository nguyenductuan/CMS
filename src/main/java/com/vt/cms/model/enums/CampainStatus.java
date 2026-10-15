package com.vt.cms.model.enums;

public enum CampainStatus {
    PENDING_APPROVAL(
            "PENDING_APPROVAL",
            "Chờ phê duyệt"
    ),
    APPROVED(
            "APPROVED",
            "Đã phê duyệt"
    ),
    REJECTED(
            "REJECTED",
            "Bị từ chối"
    );


    private final String code;
    private final String message;

    CampainStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
