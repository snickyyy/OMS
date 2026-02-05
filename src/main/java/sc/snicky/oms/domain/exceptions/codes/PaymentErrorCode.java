package sc.snicky.oms.domain.exceptions.codes;

public enum PaymentErrorCode {
    PAYMENT_ID_EMPTY_OR_NULL,

    PAYMENT_TRANSACTION_ID_INVALID_LENGTH,
    PAYMENT_TRANSACTION_ID_EMPTY_OR_NULL,

    PAYMENT_METHOD_NOT_SUPPORTED,
}
