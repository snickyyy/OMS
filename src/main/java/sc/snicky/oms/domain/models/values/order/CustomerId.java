package sc.snicky.oms.domain.models.values.order;

import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.OrderErrorCode;
import sc.snicky.oms.domain.models.values.BaseValueObject;

public class CustomerId extends BaseValueObject<String> {
    private static final int MAX_LENGTH = 255;

    public CustomerId(String value) {
        super(value);
    }

    @Override
    public void validate() {
        if (getValue() == null || getValue().trim().isEmpty()) {
            throw new ValidationRuleException(
                    "Customer ID cannot be null or empty",
                    OrderErrorCode.CUSTOMER_ID_EMPTY_OR_NULL.name()
            );
        }
        if (getValue().trim().length() > MAX_LENGTH) {
            throw new ValidationRuleException(
                    "Customer ID cannot exceed " + MAX_LENGTH + " characters",
                    OrderErrorCode.CUSTOMER_ID_INVALID_LENGTH.name()
            );
        }
    }
}
