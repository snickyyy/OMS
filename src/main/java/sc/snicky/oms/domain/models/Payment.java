package sc.snicky.oms.domain.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import sc.snicky.oms.domain.exceptions.BusinessRuleException;
import sc.snicky.oms.domain.exceptions.codes.PaymentErrorCode;
import sc.snicky.oms.domain.models.values.Money;
import sc.snicky.oms.domain.models.values.order.OrderId;
import sc.snicky.oms.domain.models.values.payments.PaymentId;
import sc.snicky.oms.domain.models.values.payments.PaymentMethod;
import sc.snicky.oms.domain.models.values.payments.PaymentStatus;
import sc.snicky.oms.domain.models.values.payments.TransactionId;

import java.util.Objects;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Payment {
    @EqualsAndHashCode.Include
    private final PaymentId id;
    private final TransactionId transactionId;
    private final OrderId orderId;
    private final Money amount;
    private final PaymentMethod method;
    private PaymentStatus status;

    @Builder
    private Payment(PaymentId id, TransactionId transactionId, OrderId orderId, Money amount, PaymentMethod method) {
        this.id = Objects.requireNonNull(id);
        this.transactionId = Objects.requireNonNull(transactionId);
        this.orderId = Objects.requireNonNull(orderId);
        this.amount = Objects.requireNonNull(amount);
        this.method = Objects.requireNonNull(method);
        this.status = PaymentStatus.PENDING;
    }

    /**
     * Restores a `Payment` entity from the database or another persistent storage.
     * This method is used to recreate a `Payment` object with all its attributes,
     * including its current status.
     */
    public static Payment restore(PaymentId id, TransactionId tId, OrderId oId,
                                  Money amount, PaymentMethod method, PaymentStatus status) {
        Payment p = new Payment(id, tId, oId, amount, method);
        p.status = status;
        return p;
    }

    public void markAsCompleted() {
        transitionTo(PaymentStatus.COMPLETED);
    }

    public void markAsFailed() {
        transitionTo(PaymentStatus.FAILED);
    }

    public void markAsRefunded() {
        transitionTo(PaymentStatus.REFUNDED);
    }

    private void transitionTo(PaymentStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus)) {
            throw new BusinessRuleException(
                    String.format("Cannot transition from %s to %s", this.status, nextStatus),
                    PaymentErrorCode.PAYMENT_STATUS_INVALID_TRANSITION.name()
            );
        }
        this.status = nextStatus;
    }
}
