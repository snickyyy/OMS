package sc.snicky.oms.domain.exceptions;

import lombok.Getter;

public abstract class DomainException extends RuntimeException {
    @Getter
    private final String errorCode;

    public DomainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
