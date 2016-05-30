package org.mule.modules.cookbook.exception;

public class CookbookException extends Exception {

    private static final long serialVersionUID = 1L;

    public CookbookException(String message, Throwable cause) {
        super(message, cause);
    }

    public CookbookException(String message) {
        super(message);
    }

    public CookbookException(Throwable throwable) {
        super(throwable);
    }

}
