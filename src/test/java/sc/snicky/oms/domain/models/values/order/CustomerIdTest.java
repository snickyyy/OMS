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

@DisplayName("CustomerId Value Object Tests")
class CustomerIdTest {

    private static final int MAX_LENGTH = 255;

    @Nested
    @DisplayName("Valid CustomerId Creation")
    class ValidCreationTests {

        @Test
        @DisplayName("Should create CustomerId with valid value")
        void shouldCreateCustomerIdWithValidValue() {
            String validId = "CUST-12345";

            CustomerId customerId = new CustomerId(validId);

            assertThat(customerId.getValue()).isEqualTo(validId);
        }
    }

    @Nested
    @DisplayName("Invalid CustomerId Creation - Null or Empty")
    class NullOrEmptyTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw exception when value is null or empty")
        void shouldThrowExceptionWhenValueIsNullOrEmpty(String invalidValue) {
            assertThatThrownBy(() -> new CustomerId(invalidValue))
                    .isInstanceOf(ValidationRuleException.class)
                    .satisfies(ex -> {
                        ValidationRuleException vre = (ValidationRuleException) ex;
                        assertThat(vre.getErrorCode())
                                .isEqualTo(OrderErrorCode.CUSTOMER_ID_EMPTY_OR_NULL.name());
                    });
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "  ", "   ", "\t", "\n", " \t\n "})
        @DisplayName("Should throw exception when value is only whitespace")
        void shouldThrowExceptionWhenValueIsOnlyWhitespace(String whitespaceValue) {
            assertThatThrownBy(() -> new CustomerId(whitespaceValue))
                    .isInstanceOf(ValidationRuleException.class)
                    .satisfies(ex -> {
                        ValidationRuleException vre = (ValidationRuleException) ex;
                        assertThat(vre.getErrorCode())
                                .isEqualTo(OrderErrorCode.CUSTOMER_ID_EMPTY_OR_NULL.name());
                    });
        }
    }

    @Nested
    @DisplayName("Invalid CustomerId Creation - Length Exceeded")
    class LengthExceededTests {

        @Test
        @DisplayName("Should throw exception when value exceeds maximum length")
        void shouldThrowExceptionWhenValueExceedsMaxLength() {
            String tooLongId = "A".repeat(MAX_LENGTH + 1);

            assertThatThrownBy(() -> new CustomerId(tooLongId))
                    .isInstanceOf(ValidationRuleException.class)
                    .satisfies(ex -> {
                        ValidationRuleException vre = (ValidationRuleException) ex;
                        assertThat(vre.getErrorCode())
                                .isEqualTo(OrderErrorCode.CUSTOMER_ID_INVALID_LENGTH.name());
                    });
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when values are the same")
        void shouldBeEqualWhenValuesAreTheSame() {
            CustomerId id1 = new CustomerId("CUST-123");
            CustomerId id2 = new CustomerId("CUST-123");

            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when values are different")
        void shouldNotBeEqualWhenValuesAreDifferent() {
            CustomerId id1 = new CustomerId("CUST-123");
            CustomerId id2 = new CustomerId("CUST-456");

            assertThat(id1).isNotEqualTo(id2);
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable - getValue returns same reference")
        void shouldBeImmutableGetValueReturnsSameReference() {
            CustomerId customerId = new CustomerId("CUST-123");
            String value1 = customerId.getValue();
            String value2 = customerId.getValue();

            assertThat(value1).isSameAs(value2);
        }

        @Test
        @DisplayName("Should maintain value integrity")
        void shouldMaintainValueIntegrity() {
            String originalValue = "CUST-123";
            CustomerId customerId = new CustomerId(originalValue);

            assertThat(customerId.getValue()).isEqualTo(originalValue);
        }
    }
}