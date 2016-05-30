package org.mule.modules.cookbook.exception;

public class CookbookRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CookbookRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CookbookRuntimeException(String message) {
        super(message);
    }

    public CookbookRuntimeException(Throwable throwable) {
        super(throwable);
    }

}

