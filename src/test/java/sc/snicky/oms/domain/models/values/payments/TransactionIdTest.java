package sc.snicky.oms.domain.models.values.payments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.PaymentErrorCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TransactionId Value Object Tests")
class TransactionIdTest {

    private static final int MAX_LENGTH = 255;

    @Test
    @DisplayName("Should create TransactionId with valid 255 character value")
    void shouldCreateTransactionIdWithValidValue() {
        String validId = "a".repeat(MAX_LENGTH);

        TransactionId transactionId = new TransactionId(validId);

        assertNotNull(transactionId);
        assertEquals(validId, transactionId.getValue());
        assertEquals(MAX_LENGTH, transactionId.getValue().length());
    }

    @Test
    @DisplayName("Should throw ValidationRuleException when value is null")
    void shouldThrowExceptionWhenValueIsNull() {
        ValidationRuleException exception = assertThrows(
                ValidationRuleException.class,
                () -> new TransactionId(null)
        );

        assertEquals(PaymentErrorCode.PAYMENT_TRANSACTION_ID_EMPTY_OR_NULL.name(), exception.getErrorCode());
    }

    @Test
    @DisplayName("Should throw ValidationRuleException when value is empty")
    void shouldThrowExceptionWhenValueIsEmpty() {
        ValidationRuleException exception = assertThrows(
                ValidationRuleException.class,
                () -> new TransactionId("")
        );

        assertEquals(PaymentErrorCode.PAYMENT_TRANSACTION_ID_EMPTY_OR_NULL.name(), exception.getErrorCode());
    }


    @Test
    @DisplayName("Should be equal when values are the same")
    void shouldBeEqualWhenValuesAreSame() {
        String id = "a".repeat(MAX_LENGTH);
        TransactionId transactionId1 = new TransactionId(id);
        TransactionId transactionId2 = new TransactionId(id);

        assertEquals(transactionId1, transactionId2);
        assertEquals(transactionId1.hashCode(), transactionId2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values are different")
    void shouldNotBeEqualWhenValuesAreDifferent() {
        String id1 = "a".repeat(MAX_LENGTH);
        String id2 = "b".repeat(MAX_LENGTH);
        TransactionId transactionId1 = new TransactionId(id1);
        TransactionId transactionId2 = new TransactionId(id2);

        assertNotEquals(transactionId1, transactionId2);
    }
}