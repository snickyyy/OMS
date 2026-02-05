package sc.snicky.oms.domain.models;

import lombok.Builder;
import lombok.Getter;
import sc.snicky.oms.domain.models.values.Money;
import sc.snicky.oms.domain.models.values.order.OrderId;
import sc.snicky.oms.domain.models.values.payments.PaymentId;
import sc.snicky.oms.domain.models.values.payments.PaymentMethod;
import sc.snicky.oms.domain.models.values.payments.PaymentStatus;
import sc.snicky.oms.domain.models.values.payments.TransactionId;

import java.util.Objects;

@Getter
public class Payment {
    private final PaymentId id;
    private final TransactionId transactionId;
    private final OrderId orderId;
    private final Money amount;
    private final PaymentMethod method;
    private PaymentStatus status;

    @Builder
    public Payment(PaymentId id, TransactionId transactionId, OrderId orderId, PaymentStatus status, Money amount, PaymentMethod method) {
        this.id = Objects.requireNonNull(id);
        this.transactionId = Objects.requireNonNull(transactionId);
        this.orderId = Objects.requireNonNull(orderId);
        this.status = Objects.requireNonNull(status);
        this.amount = Objects.requireNonNull(amount);
        this.method = Objects.requireNonNull(method);
    }
}
