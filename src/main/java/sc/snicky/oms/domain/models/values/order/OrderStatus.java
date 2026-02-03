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
    FAILED,
}
