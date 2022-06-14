package se.b3.healthtech.blackbird.blbaggregator.exception;

public class ContainerNotFoundException extends RuntimeException{

    public ContainerNotFoundException() {
        super();
    }

    public ContainerNotFoundException(String message) {
        super(message);
    }

    public ContainerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ContainerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
