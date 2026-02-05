package sc.snicky.oms.domain.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import sc.snicky.oms.domain.exceptions.BusinessRuleException;
import sc.snicky.oms.domain.exceptions.codes.OrderErrorCode;
import sc.snicky.oms.domain.models.values.Money;
import sc.snicky.oms.domain.models.values.order.CustomerId;
import sc.snicky.oms.domain.models.values.order.OrderId;
import sc.snicky.oms.domain.models.values.order.OrderNumber;
import sc.snicky.oms.domain.models.values.order.OrderStatus;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class Order {
    private final OrderId id;
    private final OrderNumber orderNumber;
    private final CustomerId customerId;
    private OrderStatus status;
    /**
     * The total monetary amount for this order.
     * <p>
     * Currently this represents the total before any discounts are applied.
     * In the future, this field may be updated to include discounts once
     * discount handling is implemented for orders.
     */
    private Money total;

    @Builder
    public Order(OrderId id, OrderNumber orderNumber, CustomerId customerId, Money initialTotal) {
        this.id = Objects.requireNonNull(id, "OrderId is required");
        this.orderNumber = Objects.requireNonNull(orderNumber, "OrderNumber is required");
        this.customerId = Objects.requireNonNull(customerId, "CustomerId is required");
        this.total = Objects.requireNonNull(initialTotal, "Initial total is required");

        this.status = OrderStatus.PENDING;
    }

    public void markAsPaid() {
        transitionTo(OrderStatus.PAID);
    }

    public void markAsCompleted() {
        transitionTo(OrderStatus.COMPLETED);
    }

    private void transitionTo(OrderStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus)) {
            throw new BusinessRuleException(
                    String.format("Cannot transition from %s to %s", this.status, nextStatus),
                    OrderErrorCode.ORDER_STATUS_INVALID_TRANSITION.name()
            );
        }
        this.status = nextStatus;
    }
}
