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

@DisplayName("OrderNumber Value Object Tests")
class OrderNumberTest {

    private static final int LENGTH = 20;

    @Nested
    @DisplayName("Valid OrderNumber Creation")
    class ValidCreationTests {

        @Test
        @DisplayName("Should create OrderNumber with exactly 20 characters")
        void shouldCreateOrderNumberWithExactly20Characters() {
            String validNumber = "1ORD-2024-0001-12345";

            OrderNumber orderNumber = new OrderNumber(validNumber);

            assertThat(orderNumber.getValue()).isEqualTo(validNumber);
            assertThat(orderNumber.getValue().length()).isEqualTo(LENGTH);
        }
    }

    @Nested
    @DisplayName("Invalid OrderNumber Creation - Null or Empty")
    class NullOrEmptyTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when value is null or empty")
        void shouldThrowExceptionWhenValueIsNullOrEmpty(String invalidValue) {
            assertThatThrownBy(() -> new OrderNumber(invalidValue))
                    .isInstanceOf(ValidationRuleException.class)
                    .satisfies(ex -> {
                        ValidationRuleException vre = (ValidationRuleException) ex;
                        assertThat(vre.getErrorCode())
                                .isEqualTo(OrderErrorCode.ORDER_NUMBER_EMPTY_OR_NULL.name());
                    });
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "  ", "   ", "\t", "\n", "\r", " \t\n\r ", "                    "})
        @DisplayName("Should throw exception when value is only whitespace")
        void shouldThrowExceptionWhenValueIsOnlyWhitespace(String whitespaceValue) {
            assertThatThrownBy(() -> new OrderNumber(whitespaceValue))
                    .isInstanceOf(ValidationRuleException.class)
                    .satisfies(ex -> {
                        ValidationRuleException vre = (ValidationRuleException) ex;
                        assertThat(vre.getErrorCode())
                                .isEqualTo(OrderErrorCode.ORDER_NUMBER_EMPTY_OR_NULL.name());
                    });
        }
    }

    @Nested
    @DisplayName("Invalid OrderNumber Creation - Invalid Length")
    class InvalidLengthTests {

        @Test
        @DisplayName("Should throw exception when length is less than 20")
        void shouldThrowExceptionWhenLengthIsLessThan20() {
            String shortNumber = "ORD-2024-001"; // 12 characters

            assertThatThrownBy(() -> new OrderNumber(shortNumber))
                    .isInstanceOf(ValidationRuleException.class)
                    .satisfies(ex -> {
                        ValidationRuleException vre = (ValidationRuleException) ex;
                        assertThat(vre.getErrorCode())
                                .isEqualTo(OrderErrorCode.ORDER_NUMBER_INVALID_LENGTH.name());
                    });
        }

        @Test
        @DisplayName("Should throw exception when length is more than 20")
        void shouldThrowExceptionWhenLengthIsMoreThan20() {
            String longNumber = "ORD-2024-0001-1234567"; // 21 characters

            assertThatThrownBy(() -> new OrderNumber(longNumber))
                    .isInstanceOf(ValidationRuleException.class)
                    .hasMessageContaining("Order number must be exactly " + LENGTH + " characters long")
                    .satisfies(ex -> {
                        ValidationRuleException vre = (ValidationRuleException) ex;
                        assertThat(vre.getErrorCode())
                                .isEqualTo(OrderErrorCode.ORDER_NUMBER_INVALID_LENGTH.name());
                    });
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when values are the same")
        void shouldBeEqualWhenValuesAreTheSame() {
            OrderNumber number1 = new OrderNumber("ABCDEFGHIJKLMNOPQRST");
            OrderNumber number2 = new OrderNumber("ABCDEFGHIJKLMNOPQRST");

            assertThat(number1).isEqualTo(number2);
            assertThat(number1.hashCode()).isEqualTo(number2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when values are different")
        void shouldNotBeEqualWhenValuesAreDifferent() {
            OrderNumber number1 = new OrderNumber("ABCDEFGHIJKLMNOPQRST");
            OrderNumber number2 = new OrderNumber("12345678901234567890");

            assertThat(number1).isNotEqualTo(number2);
            assertThat(number1.hashCode()).isNotEqualTo(number2.hashCode());
        }

        @Test
        @DisplayName("Should maintain equals contract")
        void shouldMaintainEqualsContract() {
            OrderNumber number1 = new OrderNumber("ABCDEFGHIJKLMNOPQRST");
            OrderNumber number2 = new OrderNumber("ABCDEFGHIJKLMNOPQRST");
            OrderNumber number3 = new OrderNumber("ABCDEFGHIJKLMNOPQRST");

            assertThat(number1).isEqualTo(number2);
            assertThat(number2).isEqualTo(number3);
            assertThat(number1).isEqualTo(number3);
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable - getValue returns consistent value")
        void shouldBeImmutableGetValueReturnsConsistentValue() {
            OrderNumber orderNumber = new OrderNumber("ABCDEFGHIJKLMNOPQRST");
            String value1 = orderNumber.getValue();
            String value2 = orderNumber.getValue();

            assertThat(value1).isEqualTo(value2);
            assertThat(value1).isSameAs(value2);
        }

        @Test
        @DisplayName("Should maintain value integrity after creation")
        void shouldMaintainValueIntegrityAfterCreation() {
            String originalValue = "ABCDEFGHIJKLMNOPQRST";
            OrderNumber orderNumber = new OrderNumber(originalValue);

            assertThat(orderNumber.getValue()).isEqualTo(originalValue);
        }

        @Test
        @DisplayName("Should not be affected by changes to original string")
        void shouldNotBeAffectedByChangesToOriginalString() {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRST");
            String originalValue = sb.toString();
            OrderNumber orderNumber = new OrderNumber(originalValue);

            sb.append("EXTRA");

            assertThat(orderNumber.getValue()).isEqualTo("ABCDEFGHIJKLMNOPQRST");
            assertThat(orderNumber.getValue().length()).isEqualTo(LENGTH);
        }
    }
}