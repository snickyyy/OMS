package sc.snicky.oms.domain.models.values;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import sc.snicky.oms.domain.exceptions.BusinessRuleException;
import sc.snicky.oms.domain.exceptions.codes.PaymentErrorCode;
import sc.snicky.oms.domain.models.Payment;
import sc.snicky.oms.domain.models.values.order.OrderId;
import sc.snicky.oms.domain.models.values.payments.PaymentId;
import sc.snicky.oms.domain.models.values.payments.PaymentMethod;
import sc.snicky.oms.domain.models.values.payments.PaymentStatus;
import sc.snicky.oms.domain.models.values.payments.TransactionId;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Payment Entity Tests")
class PaymentTest {

    private static final PaymentId PAYMENT_ID = new PaymentId(UUID.randomUUID().toString());
    private static final TransactionId TRANSACTION_ID = new TransactionId(UUID.randomUUID().toString());
    private static final OrderId ORDER_ID = new OrderId(UUID.randomUUID().toString());
    private static final Money AMOUNT = new Money(10000, "USD");
    private static final PaymentMethod METHOD = PaymentMethod.CREDIT_CARD;

    @Nested
    @DisplayName("Constructor and Builder Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create payment with valid parameters")
        void shouldCreatePaymentWithValidParameters() {
            Payment payment = Payment.builder()
                    .id(PAYMENT_ID)
                    .transactionId(TRANSACTION_ID)
                    .orderId(ORDER_ID)
                    .amount(AMOUNT)
                    .method(METHOD)
                    .build();

            assertThat(payment.getId()).isEqualTo(PAYMENT_ID);
            assertThat(payment.getTransactionId()).isEqualTo(TRANSACTION_ID);
            assertThat(payment.getOrderId()).isEqualTo(ORDER_ID);
            assertThat(payment.getAmount()).isEqualTo(AMOUNT);
            assertThat(payment.getMethod()).isEqualTo(METHOD);
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
        }

        @Test
        @DisplayName("Should throw exception when id is null")
        void shouldThrowExceptionWhenIdIsNull() {
            assertThatThrownBy(() -> Payment.builder()
                    .id(null)
                    .transactionId(TRANSACTION_ID)
                    .orderId(ORDER_ID)
                    .amount(AMOUNT)
                    .method(METHOD)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw exception when transactionId is null")
        void shouldThrowExceptionWhenTransactionIdIsNull() {
            assertThatThrownBy(() -> Payment.builder()
                    .id(PAYMENT_ID)
                    .transactionId(null)
                    .orderId(ORDER_ID)
                    .amount(AMOUNT)
                    .method(METHOD)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw exception when orderId is null")
        void shouldThrowExceptionWhenOrderIdIsNull() {
            assertThatThrownBy(() -> Payment.builder()
                    .id(PAYMENT_ID)
                    .transactionId(TRANSACTION_ID)
                    .orderId(null)
                    .amount(AMOUNT)
                    .method(METHOD)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw exception when amount is null")
        void shouldThrowExceptionWhenAmountIsNull() {
            assertThatThrownBy(() -> Payment.builder()
                    .id(PAYMENT_ID)
                    .transactionId(TRANSACTION_ID)
                    .orderId(ORDER_ID)
                    .amount(null)
                    .method(METHOD)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw exception when method is null")
        void shouldThrowExceptionWhenMethodIsNull() {
            assertThatThrownBy(() -> Payment.builder()
                    .id(PAYMENT_ID)
                    .transactionId(TRANSACTION_ID)
                    .orderId(ORDER_ID)
                    .amount(AMOUNT)
                    .method(null)
                    .build())
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Restore Method Tests")
    class RestoreTests {

        @ParameterizedTest
        @EnumSource(PaymentStatus.class)
        @DisplayName("Should restore payment with any status")
        void shouldRestorePaymentWithAnyStatus(PaymentStatus status) {
            Payment payment = Payment.restore(PAYMENT_ID, TRANSACTION_ID, ORDER_ID, AMOUNT, METHOD, status);

            assertThat(payment.getId()).isEqualTo(PAYMENT_ID);
            assertThat(payment.getTransactionId()).isEqualTo(TRANSACTION_ID);
            assertThat(payment.getOrderId()).isEqualTo(ORDER_ID);
            assertThat(payment.getAmount()).isEqualTo(AMOUNT);
            assertThat(payment.getMethod()).isEqualTo(METHOD);
            assertThat(payment.getStatus()).isEqualTo(status);
        }

        @Test
        @DisplayName("Should restore completed payment")
        void shouldRestoreCompletedPayment() {
            Payment payment = Payment.restore(PAYMENT_ID, TRANSACTION_ID, ORDER_ID,
                    AMOUNT, METHOD, PaymentStatus.COMPLETED);

            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        }
    }

    @Nested
    @DisplayName("Status Transition Tests")
    class StatusTransitionTests {

        @Test
        @DisplayName("Should mark pending payment as completed")
        void shouldMarkPendingPaymentAsCompleted() {
            Payment payment = createPendingPayment();

            payment.markAsCompleted();

            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should mark pending payment as failed")
        void shouldMarkPendingPaymentAsFailed() {
            Payment payment = createPendingPayment();

            payment.markAsFailed();

            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        }

        @Test
        @DisplayName("Should mark completed payment as refunded")
        void shouldMarkCompletedPaymentAsRefunded() {
            Payment payment = Payment.restore(PAYMENT_ID, TRANSACTION_ID, ORDER_ID,
                    AMOUNT, METHOD, PaymentStatus.COMPLETED);

            payment.markAsRefunded();

            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REFUNDED);
        }

        @Test
        @DisplayName("Should throw exception when invalid transition to completed")
        void shouldThrowExceptionWhenInvalidTransitionToCompleted() {
            Payment payment = Payment.restore(PAYMENT_ID, TRANSACTION_ID, ORDER_ID,
                    AMOUNT, METHOD, PaymentStatus.REFUNDED);

            assertThatThrownBy(payment::markAsCompleted)
                    .isInstanceOf(BusinessRuleException.class)
                    .satisfies(ex -> {
                        BusinessRuleException bre = (BusinessRuleException) ex;
                        assertThat(bre.getErrorCode())
                                .isEqualTo(PaymentErrorCode.PAYMENT_STATUS_INVALID_TRANSITION.name());
                    });
        }

        @Test
        @DisplayName("Should throw exception when invalid transition to failed")
        void shouldThrowExceptionWhenInvalidTransitionToFailed() {
            Payment payment = Payment.restore(PAYMENT_ID, TRANSACTION_ID, ORDER_ID,
                    AMOUNT, METHOD, PaymentStatus.COMPLETED);

            assertThatThrownBy(payment::markAsFailed)
                    .isInstanceOf(BusinessRuleException.class);
        }

        @Test
        @DisplayName("Should throw exception when invalid transition to refunded")
        void shouldThrowExceptionWhenInvalidTransitionToRefunded() {
            Payment payment = createPendingPayment();

            assertThatThrownBy(payment::markAsRefunded)
                    .isInstanceOf(BusinessRuleException.class);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when payment IDs are equal")
        void shouldBeEqualWhenPaymentIdsAreEqual() {
            Payment payment1 = createPendingPayment();
            Payment payment2 = Payment.builder()
                    .id(PAYMENT_ID)
                    .transactionId(new TransactionId(UUID.randomUUID().toString()))
                    .orderId(new OrderId(UUID.randomUUID().toString()))
                    .amount(new Money(99999, "USD"))
                    .method(PaymentMethod.CREDIT_CARD)
                    .build();

            assertThat(payment1).isEqualTo(payment2);
            assertThat(payment1.hashCode()).isEqualTo(payment2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when payment IDs are different")
        void shouldNotBeEqualWhenPaymentIdsAreDifferent() {
            Payment payment1 = createPendingPayment();
            Payment payment2 = Payment.builder()
                    .id(new PaymentId(UUID.randomUUID().toString()))
                    .transactionId(TRANSACTION_ID)
                    .orderId(ORDER_ID)
                    .amount(AMOUNT)
                    .method(METHOD)
                    .build();

            assertThat(payment1).isNotEqualTo(payment2);
        }
    }

    // Helper method
    private Payment createPendingPayment() {
        return Payment.builder()
                .id(PAYMENT_ID)
                .transactionId(TRANSACTION_ID)
                .orderId(ORDER_ID)
                .amount(AMOUNT)
                .method(METHOD)
                .build();
    }
}
