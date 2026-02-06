package sc.snicky.oms.domain.models.values.order;

import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.OrderErrorCode;
import sc.snicky.oms.domain.models.values.BaseValueObject;

public class OrderNumber extends BaseValueObject<String> {
    private static final int LENGTH = 20;

    public OrderNumber(String value) {
        super(value);
    }

    @Override
    public void validate() {
        if (getValue() == null || getValue().trim().isEmpty()) {
            throw new ValidationRuleException(
                    "Order number cannot be null or empty",
                    OrderErrorCode.ORDER_NUMBER_EMPTY_OR_NULL.name()
            );
        }
        if (getValue().trim().length() != LENGTH) {
            throw new ValidationRuleException(
                    "Order number must be exactly " + LENGTH + " characters long",
                    OrderErrorCode.ORDER_NUMBER_INVALID_LENGTH.name()
            );
        }
    }
}
