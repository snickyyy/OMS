package sc.snicky.oms.domain.models.values.payments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.PaymentErrorCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("PaymentId Value Object Tests")
class PaymentIdTest {

    @Test
    @DisplayName("Should create PaymentId with valid value")
    void shouldCreatePaymentIdWithValidValue() {
        String validId = "PAY-123456";

        PaymentId paymentId = new PaymentId(validId);

        assertNotNull(paymentId);
        assertEquals(validId, paymentId.getValue());
    }

    @Test
    @DisplayName("Should throw ValidationRuleException when value is null")
    void shouldThrowExceptionWhenValueIsNull() {
        ValidationRuleException exception = assertThrows(
                ValidationRuleException.class,
                () -> new PaymentId(null)
        );

        assertEquals("Payment ID cannot be null or empty", exception.getMessage());
        assertEquals(PaymentErrorCode.PAYMENT_ID_EMPTY_OR_NULL.name(), exception.getErrorCode());
    }

    @Test
    @DisplayName("Should throw ValidationRuleException when value is empty")
    void shouldThrowExceptionWhenValueIsEmpty() {
        ValidationRuleException exception = assertThrows(
                ValidationRuleException.class,
                () -> new PaymentId("")
        );

        assertEquals("Payment ID cannot be null or empty", exception.getMessage());
        assertEquals(PaymentErrorCode.PAYMENT_ID_EMPTY_OR_NULL.name(), exception.getErrorCode());
    }

    @Test
    @DisplayName("Shouldn't create PaymentId with whitespace value")
    void shouldThrowExceptionWhenValueIsWhitespace() {
        ValidationRuleException exception = assertThrows(
                ValidationRuleException.class,
                () -> new PaymentId("    ")
        );

        assertEquals("Payment ID cannot be null or empty", exception.getMessage()); // TODO: remove checks with error messages
        assertEquals(PaymentErrorCode.PAYMENT_ID_EMPTY_OR_NULL.name(), exception.getErrorCode());
    }

    @Test
    @DisplayName("Should be equal when values are the same")
    void shouldBeEqualWhenValuesAreSame() {
        String id = "PAY-123";
        PaymentId paymentId1 = new PaymentId(id);
        PaymentId paymentId2 = new PaymentId(id);

        assertEquals(paymentId1, paymentId2);
        assertEquals(paymentId1.hashCode(), paymentId2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values are different")
    void shouldNotBeEqualWhenValuesAreDifferent() {
        PaymentId paymentId1 = new PaymentId("PAY-123");
        PaymentId paymentId2 = new PaymentId("PAY-456");

        assertNotEquals(paymentId1, paymentId2);
    }
}
