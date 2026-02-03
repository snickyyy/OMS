package sc.snicky.oms.domain.exceptions;

public class BusinessRuleException extends DomainException {
    public BusinessRuleException(String message, String code) {
        super(message, code);
    }
}
