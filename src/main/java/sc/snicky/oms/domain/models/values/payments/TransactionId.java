package sc.snicky.oms.domain.models.values.payments;

import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.PaymentErrorCode;
import sc.snicky.oms.domain.models.values.BaseValueObject;

public class TransactionId extends BaseValueObject<String> {
    private static final int MAX_LENGTH = 255;

    public TransactionId(String value) {
        super(value);
    }

    @Override
    public void validate() {
        if (getValue() == null || getValue().trim().isEmpty()) {
            throw new ValidationRuleException(
                    "Transaction ID cannot be null or empty",
                    PaymentErrorCode.PAYMENT_TRANSACTION_ID_EMPTY_OR_NULL.name()
            );
        }
        if (getValue().trim().length() > MAX_LENGTH) {
            throw new ValidationRuleException(
                    "Transaction ID cannot exceed " + MAX_LENGTH + " characters",
                    PaymentErrorCode.PAYMENT_TRANSACTION_ID_INVALID_LENGTH.name()
            );
        }
    }
}
