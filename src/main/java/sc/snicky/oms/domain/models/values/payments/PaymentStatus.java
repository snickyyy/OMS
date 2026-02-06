package sc.snicky.oms.domain.models.values.payments;

import sc.snicky.oms.domain.models.values.StatusValueObject;

public enum PaymentStatus implements StatusValueObject<PaymentStatus> {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED;

    @Override
    public boolean canTransitionTo(PaymentStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == COMPLETED || newStatus == FAILED;
            case COMPLETED -> newStatus == REFUNDED;
            case FAILED, REFUNDED -> false; // Terminal states
        };
    }
}
