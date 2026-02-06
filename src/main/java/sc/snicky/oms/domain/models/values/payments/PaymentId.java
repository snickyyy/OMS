package sc.snicky.oms.domain.models.values.payments;

import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.PaymentErrorCode;
import sc.snicky.oms.domain.models.values.BaseValueObject;

public class PaymentId extends BaseValueObject<String> {
    public PaymentId(String value) {
        super(value);
    }


    @Override
    public void validate() {
        if (getValue() == null || getValue().trim().isEmpty()) {
            throw new ValidationRuleException(
                    "Payment ID cannot be null or empty",
                    PaymentErrorCode.PAYMENT_ID_EMPTY_OR_NULL.name()
            );
        }
    }
}
