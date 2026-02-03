package sc.snicky.oms.domain.models.values.order;

public enum OrderStatus {
    /**
     * Packed and awaiting payment; cancellable.
     */
    PENDING,
    /**
     * Payment received; cancellable.
     */
    PAID,
    /**
     * Completed and closed; cannot be canceled.
     */
    COMPLETED,
    /**
     * Canceled by user or system.
     */
    CANCELLED,
    /**
     * Failed during processing.
     */
    FAILED;

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PENDING -> next == PAID || next == FAILED || next == CANCELLED;
            case PAID -> next == COMPLETED || next == CANCELLED || next == FAILED;
            case CANCELLED, COMPLETED, FAILED -> false; // Terminal states
        };
    }
}
