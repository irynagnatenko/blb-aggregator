package se.b3.healthtech.blackbird.blbaggregator.exception;

public class ContainerObjectNotFoundException extends RuntimeException{
    public ContainerObjectNotFoundException() {
        super();
    }

    public ContainerObjectNotFoundException(String message) {
        super(message);
    }

    public ContainerObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerObjectNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ContainerObjectNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
