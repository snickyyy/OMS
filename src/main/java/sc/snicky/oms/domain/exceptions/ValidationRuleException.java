package sc.snicky.oms.domain.exceptions;

public class ValidationRuleException extends DomainException {
    public ValidationRuleException(String message, String errorCode) {
        super(message, errorCode);
    }
}
