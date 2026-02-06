package sc.snicky.oms.domain.models.values.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.OrderErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderId Value Object Tests")
class OrderIdTest {

    @Nested
    @DisplayName("Valid OrderId Creation")
    class ValidCreationTests {

        @Test
        @DisplayName("Should create OrderId with valid value")
        void shouldCreateOrderIdWithValidValue() {
            String validId = "ORD-12345";

            OrderId orderId = new OrderId(validId);

            assertThat(orderId.getValue()).isEqualTo(validId);
        }
    }

    @Nested
    @DisplayName("Invalid OrderId Creation - Null or Empty")
    class NullOrEmptyTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when value is null or empty")
        void shouldThrowExceptionWhenValueIsNullOrEmpty(String invalidValue) {
            assertThatThrownBy(() -> new OrderId(invalidValue))
                    .isInstanceOf(ValidationRuleException.class)
                    .satisfies(ex -> {
                        ValidationRuleException vre = (ValidationRuleException) ex;
                        assertThat(vre.getErrorCode())
                                .isEqualTo(OrderErrorCode.ORDER_ID_EMPTY_OR_NULL.name());
                    });
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "  ", "   ", "\t", "\n", "\r", " \t\n\r "})
        @DisplayName("Should throw exception when value is only whitespace")
        void shouldThrowExceptionWhenValueIsOnlyWhitespace(String whitespaceValue) {
            assertThatThrownBy(() -> new OrderId(whitespaceValue))
                    .isInstanceOf(ValidationRuleException.class)
                    .satisfies(ex -> {
                        ValidationRuleException vre = (ValidationRuleException) ex;
                        assertThat(vre.getErrorCode())
                                .isEqualTo(OrderErrorCode.ORDER_ID_EMPTY_OR_NULL.name());
                    });
        }

        @Test
        @DisplayName("Should throw exception when value is multiple spaces")
        void shouldThrowExceptionWhenValueIsMultipleSpaces() {
            String whitespaceValue = "          ";

            assertThatThrownBy(() -> new OrderId(whitespaceValue))
                    .isInstanceOf(ValidationRuleException.class);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when values are the same")
        void shouldBeEqualWhenValuesAreTheSame() {
            OrderId id1 = new OrderId("ORD-123");
            OrderId id2 = new OrderId("ORD-123");

            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when values are different")
        void shouldNotBeEqualWhenValuesAreDifferent() {
            OrderId id1 = new OrderId("ORD-123");
            OrderId id2 = new OrderId("ORD-456");

            assertThat(id1).isNotEqualTo(id2);
            assertThat(id1.hashCode()).isNotEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("Should maintain equals contract with same value")
        void shouldMaintainEqualsContractWithSameValue() {
            OrderId id1 = new OrderId("ORD-123");
            OrderId id2 = new OrderId("ORD-123");
            OrderId id3 = new OrderId("ORD-123");


            assertThat(id1).isEqualTo(id2);
            assertThat(id2).isEqualTo(id3);
            assertThat(id1).isEqualTo(id3);
        }

        @Test
        @DisplayName("Should not be equal with case difference")
        void shouldNotBeEqualWithCaseDifference() {
            OrderId id1 = new OrderId("ORD-123");
            OrderId id2 = new OrderId("ord-123");

            assertThat(id1).isNotEqualTo(id2);
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable - getValue returns consistent value")
        void shouldBeImmutableGetValueReturnsConsistentValue() {
            OrderId orderId = new OrderId("ORD-123");
            String value1 = orderId.getValue();
            String value2 = orderId.getValue();

            assertThat(value1).isEqualTo(value2);
            assertThat(value1).isSameAs(value2);
        }

        @Test
        @DisplayName("Should maintain value integrity after creation")
        void shouldMaintainValueIntegrityAfterCreation() {
            String originalValue = "ORD-123";
            OrderId orderId = new OrderId(originalValue);

            assertThat(orderId.getValue()).isEqualTo(originalValue);
        }

        @Test
        @DisplayName("Should not be affected by changes to original string")
        void shouldNotBeAffectedByChangesToOriginalString() {
            StringBuilder sb = new StringBuilder("ORD-123");
            String originalValue = sb.toString();
            OrderId orderId = new OrderId(originalValue);

            sb.append("-CHANGED");

            assertThat(orderId.getValue()).isEqualTo("ORD-123");
        }
    }
}