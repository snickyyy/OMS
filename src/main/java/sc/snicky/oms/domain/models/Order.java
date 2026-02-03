package sc.snicky.oms.domain.models;

import lombok.Builder;
import lombok.Getter;
import sc.snicky.oms.domain.models.values.order.OrderId;
import sc.snicky.oms.domain.models.values.order.OrderNumber;
import sc.snicky.oms.domain.models.values.order.OrderStatus;

@Builder
@Getter
public class Order {
    private final OrderId id;
    private final OrderNumber orderNumber;

    private OrderStatus orderStatus;


}
