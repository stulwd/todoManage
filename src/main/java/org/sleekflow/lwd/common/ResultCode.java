package org.sleekflow.lwd.common;

public enum ResultCode implements IErrorCode {
    SUCCESS(200, "operate success"),
    FAILED(500, "operate failed"),
    VALIDATE_FAILED(404, "validate failed"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "unauthenticated");
    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
