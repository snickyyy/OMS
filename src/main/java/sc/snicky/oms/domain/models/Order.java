package sc.snicky.oms.domain.models;

import lombok.Builder;
import lombok.Getter;
import sc.snicky.oms.domain.models.values.Money;
import sc.snicky.oms.domain.models.values.order.CustomerId;
import sc.snicky.oms.domain.models.values.order.OrderId;
import sc.snicky.oms.domain.models.values.order.OrderNumber;
import sc.snicky.oms.domain.models.values.order.OrderStatus;

@Builder
@Getter
public class Order {
    private final OrderId id;
    private final OrderNumber orderNumber;
    private final CustomerId customerId;
    private OrderStatus orderStatus;
    /**
     * Represents the total amount for the order.
     * Note: In the future, this field may be updated to include discounts.
     */
    private Money orderTotal;
}
