package sc.snicky.oms.domain.models;

import lombok.Builder;
import lombok.Getter;
import sc.snicky.oms.domain.models.values.payments.PaymentId;
import sc.snicky.oms.domain.models.values.payments.TransactionId;

import java.util.Objects;

@Getter
public class Payment {
    private final PaymentId id;
    private final TransactionId transactionId;

    @Builder
    public Payment(PaymentId id, TransactionId transactionId) {
        this.id = Objects.requireNonNull(id);
        this.transactionId = Objects.requireNonNull(transactionId);
        
    }
}
