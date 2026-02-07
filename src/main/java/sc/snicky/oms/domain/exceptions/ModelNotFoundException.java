package sc.snicky.oms.domain.exceptions;

public class ModelNotFoundException extends DomainException {
    public ModelNotFoundException(String message, String code) {
        super(message, code);
    }
}
