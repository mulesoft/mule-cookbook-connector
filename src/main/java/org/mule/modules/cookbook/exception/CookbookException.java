package org.mule.modules.cookbook.exception;

/**
 * Created by corar on 24/05/16.
 */
public class CookbookException extends RuntimeException {

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
