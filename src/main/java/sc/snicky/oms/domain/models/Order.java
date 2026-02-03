package sc.snicky.oms.domain.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sc.snicky.oms.domain.models.values.order.OrderId;
import sc.snicky.oms.domain.models.values.order.OrderNumber;

@Builder
@Getter
public class Order {
    @Setter
    private OrderId id;

    @Setter
    private OrderNumber orderNumber;
}
