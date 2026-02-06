package sc.snicky.oms.domain.models.values.order;

import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.OrderErrorCode;
import sc.snicky.oms.domain.models.values.BaseValueObject;

public class OrderId extends BaseValueObject<String> {
    private static final int MAX_LENGTH = 255;

    public OrderId(String value) {
        super(value);
    }

    @Override
    public void validate() {
        if (getValue() == null || getValue().trim().isEmpty()) {
            throw new ValidationRuleException(
                    "Order ID cannot be null or empty",
                    OrderErrorCode.ORDER_ID_EMPTY_OR_NULL.name()
            );
        }
    }
}
