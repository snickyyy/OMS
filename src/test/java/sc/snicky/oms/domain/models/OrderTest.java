package sc.snicky.oms.domain.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sc.snicky.oms.domain.exceptions.BusinessRuleException;
import sc.snicky.oms.domain.exceptions.codes.OrderErrorCode;
import sc.snicky.oms.domain.models.values.Money;
import sc.snicky.oms.domain.models.values.order.CustomerId;
import sc.snicky.oms.domain.models.values.order.OrderId;
import sc.snicky.oms.domain.models.values.order.OrderNumber;
import sc.snicky.oms.domain.models.values.order.OrderStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Order")
class OrderTest {

    // ### helpers ######################################################

    private static Order buildOrder() {
        return Order.builder()
                .id(new OrderId(UUID.randomUUID().toString()))
                .orderNumber(new OrderNumber("123456789-1234567890"))
                .customerId(new CustomerId(UUID.randomUUID().toString()))
                .initialTotal(new Money(100_00, "PLN"))
                .build();
    }

    // ### constructor / builder ######################################################

    @Nested
    @DisplayName("construction")
    class Construction {

        @Test
        @DisplayName("creates order with PENDING status")
        void createsOrderWithPendingStatus() {
            Order order = buildOrder();

            assertEquals(OrderStatus.PENDING, order.getStatus());
        }

        @Test
        @DisplayName("stores all provided fields")
        void storesAllProvidedFields() {
            var id = new OrderId(UUID.randomUUID().toString());
            var orderNumber = new OrderNumber("123456789-1234567890");
            var customerId = new CustomerId(UUID.randomUUID().toString());
            var total = new Money(200_00, "PLN");

            Order order = Order.builder()
                    .id(id)
                    .orderNumber(orderNumber)
                    .customerId(customerId)
                    .initialTotal(total)
                    .build();

            assertEquals(id, order.getId());
            assertEquals(orderNumber, order.getOrderNumber());
            assertEquals(customerId, order.getCustomerId());
            assertEquals(total, order.getTotal());
        }

        @Test
        @DisplayName("throws NullPointerException when id is null")
        void throwsWhenIdIsNull() {
            assertThrows(NullPointerException.class, () ->
                    Order.builder()
                            .id(null)
                            .orderNumber(new OrderNumber("123456789-1234567890"))
                            .customerId(new CustomerId(UUID.randomUUID().toString()))
                            .initialTotal(new Money(100_00, "PLN"))
                            .build()
            );
        }

        @Test
        @DisplayName("throws NullPointerException when orderNumber is null")
        void throwsWhenOrderNumberIsNull() {
            assertThrows(NullPointerException.class, () ->
                    Order.builder()
                            .id(new OrderId(UUID.randomUUID().toString()))
                            .orderNumber(null)
                            .customerId(new CustomerId(UUID.randomUUID().toString()))
                            .initialTotal(new Money(100_00, "PLN"))
                            .build()
            );
        }

        @Test
        @DisplayName("throws NullPointerException when customerId is null")
        void throwsWhenCustomerIdIsNull() {
            assertThrows(NullPointerException.class, () ->
                    Order.builder()
                            .id(new OrderId(UUID.randomUUID().toString()))
                            .orderNumber(new OrderNumber("123456789-1234567890"))
                            .customerId(null)
                            .initialTotal(new Money(100_00, "PLN"))
                            .build()
            );
        }

        @Test
        @DisplayName("throws NullPointerException when initialTotal is null")
        void throwsWhenInitialTotalIsNull() {
            assertThrows(NullPointerException.class, () ->
                    Order.builder()
                            .id(new OrderId(UUID.randomUUID().toString()))
                            .orderNumber(new OrderNumber("123456789-1234567890"))
                            .customerId(new CustomerId(UUID.randomUUID().toString()))
                            .initialTotal(null)
                            .build()
            );
        }

        // ### markAsPaid ######################################################

        @Nested
        @DisplayName("markAsPaid()")
        class MarkAsPaid {

            @Test
            @DisplayName("transitions PENDING -> PAID")
            void transitionsPendingToPaid() {
                Order order = buildOrder(); // PENDING

                order.markAsPaid();

                assertEquals(OrderStatus.PAID, order.getStatus());
            }

            @Test
            @DisplayName("throws BusinessRuleException when already PAID")
            void throwsWhenAlreadyPaid() {
                Order order = buildOrder();
                order.markAsPaid(); // PAID

                var ex = assertThrows(BusinessRuleException.class, order::markAsPaid);

                assertEquals(OrderErrorCode.ORDER_STATUS_INVALID_TRANSITION.name(), ex.getErrorCode());
                assertTrue(ex.getMessage().contains("PAID"));
            }

            @Test
            @DisplayName("throws BusinessRuleException when already COMPLETED")
            void throwsWhenCompleted() {
                Order order = buildOrder();
                order.markAsPaid();
                order.markAsCompleted(); // COMPLETED

                var ex = assertThrows(BusinessRuleException.class, order::markAsPaid);

                assertEquals(OrderErrorCode.ORDER_STATUS_INVALID_TRANSITION.name(), ex.getErrorCode());
            }
        }

        // ### markAsCompleted ######################################################

        @Nested
        @DisplayName("markAsCompleted()")
        class MarkAsCompleted {

            @Test
            @DisplayName("transitions PAID -> COMPLETED")
            void transitionsPaidToCompleted() {
                Order order = buildOrder();
                order.markAsPaid(); // PAID

                order.markAsCompleted();

                assertEquals(OrderStatus.COMPLETED, order.getStatus());
            }

            @Test
            @DisplayName("throws BusinessRuleException when still PENDING")
            void throwsWhenPending() {
                Order order = buildOrder(); // PENDING

                var ex = assertThrows(BusinessRuleException.class, order::markAsCompleted);

                assertEquals(OrderErrorCode.ORDER_STATUS_INVALID_TRANSITION.name(), ex.getErrorCode());
                assertTrue(ex.getMessage().contains("PENDING"));
                assertTrue(ex.getMessage().contains("COMPLETED"));
            }

            @Test
            @DisplayName("throws BusinessRuleException when already COMPLETED")
            void throwsWhenAlreadyCompleted() {
                Order order = buildOrder();
                order.markAsPaid();
                order.markAsCompleted(); // COMPLETED

                var ex = assertThrows(BusinessRuleException.class, order::markAsCompleted);

                assertEquals(OrderErrorCode.ORDER_STATUS_INVALID_TRANSITION.name(), ex.getErrorCode());
            }
        }

        // ### full lifecycle ######################################################

        @Nested
        @DisplayName("full lifecycle")
        class Lifecycle {

            @Test
            @DisplayName("PENDING -> PAID -> COMPLETED is the valid success path")
            void successPath() {
                Order order = buildOrder();

                assertEquals(OrderStatus.PENDING, order.getStatus());

                order.markAsPaid();
                assertEquals(OrderStatus.PAID, order.getStatus());

                order.markAsCompleted();
                assertEquals(OrderStatus.COMPLETED, order.getStatus());
            }

            @Test
            @DisplayName("total is unchanged across the full lifecycle")
            void totalDoesNotChangeAcrossTransitions() {
                var expectedTotal = new Money(100_00, "PLN");
                Order order = buildOrder();

                order.markAsPaid();
                order.markAsCompleted();

                assertEquals(expectedTotal, order.getTotal());
            }
        }
    }
}
