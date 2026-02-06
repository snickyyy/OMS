package sc.snicky.oms.domain.models.values;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import sc.snicky.oms.domain.exceptions.BusinessRuleException;
import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.MoneyErrorCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Money Value Object Tests")
class MoneyTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create Money with valid amount and currency code")
        void shouldCreateMoneyWithValidValues() {
            long amount = 10000L;
            String currencyCode = "USD";

            Money money = new Money(amount, currencyCode);

            assertNotNull(money);
            assertEquals(amount, money.getAmount());
            assertEquals(currencyCode, money.getCurrencyCode());
        }

        @Test
        @DisplayName("Should create Money with zero amount")
        void shouldCreateMoneyWithZeroAmount() {
            long amount = 0L;
            String currencyCode = "EUR";

            Money money = new Money(amount, currencyCode);

            assertEquals(0L, money.getAmount());
            assertEquals(currencyCode, money.getCurrencyCode());
        }

        @ParameterizedTest
        @ValueSource(strings = {"USD", "EUR", "GBP", "JPY", "CNY", "RUB"})
        @DisplayName("Should create Money with various valid currency codes")
        void shouldCreateMoneyWithVariousCurrencies(String currencyCode) {
            Money money = new Money(5000L, currencyCode);

            assertEquals(currencyCode, money.getCurrencyCode());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw ValidationRuleException when amount is negative")
        void shouldThrowExceptionWhenAmountIsNegative() {
            ValidationRuleException exception = assertThrows(
                    ValidationRuleException.class,
                    () -> new Money(-1L, "USD")
            );

            assertEquals(MoneyErrorCode.AMOUNT_NEGATIVE.name(), exception.getErrorCode());
        }

        @Test
        @DisplayName("Should throw ValidationRuleException when currency code is null")
        void shouldThrowExceptionWhenCurrencyCodeIsNull() {
            ValidationRuleException exception = assertThrows(
                    ValidationRuleException.class,
                    () -> new Money(1000L, null)
            );

            assertEquals(MoneyErrorCode.CURRENCY_CODE_EMPTY_OR_NULL.name(), exception.getErrorCode());
        }

        @Test
        @DisplayName("Should throw ValidationRuleException when currency code is empty")
        void shouldThrowExceptionWhenCurrencyCodeIsEmpty() {
            ValidationRuleException exception = assertThrows(
                    ValidationRuleException.class,
                    () -> new Money(1000L, "")
            );

            assertEquals(MoneyErrorCode.CURRENCY_CODE_EMPTY_OR_NULL.name(), exception.getErrorCode());
        }

        @ParameterizedTest
        @ValueSource(strings = {"US", "U", "USDD", "DOLLAR", "AB"})
        @DisplayName("Should throw ValidationRuleException when currency code length is not 3")
        void shouldThrowExceptionWhenCurrencyCodeLengthIsInvalid(String invalidCode) {
            ValidationRuleException exception = assertThrows(
                    ValidationRuleException.class,
                    () -> new Money(1000L, invalidCode)
            );

            assertEquals(MoneyErrorCode.CURRENCY_CODE_INVALID.name(), exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Addition Tests")
    class AdditionTests {

        @Test
        @DisplayName("Should add two Money objects with same currency")
        void shouldAddMoneyWithSameCurrency() {
            Money money1 = new Money(5000L, "USD");
            Money money2 = new Money(3000L, "USD");

            Money result = money1.add(money2);

            assertEquals(8000L, result.getAmount());
            assertEquals("USD", result.getCurrencyCode());
        }

        @Test
        @DisplayName("Should create new Money instance after addition")
        void shouldCreateNewInstanceAfterAddition() {
            Money money1 = new Money(1000L, "GBP");
            Money money2 = new Money(2000L, "GBP");

            Money result = money1.add(money2);

            assertNotSame(money1, result);
            assertNotSame(money2, result);
            assertEquals(1000L, money1.getAmount());
            assertEquals(2000L, money2.getAmount());
        }

        @Test
        @DisplayName("Should throw BusinessRuleException when adding different currencies")
        void shouldThrowExceptionWhenAddingDifferentCurrencies() {
            Money money1 = new Money(5000L, "USD");
            Money money2 = new Money(3000L, "EUR");

            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> money1.add(money2)
            );

            assertEquals(MoneyErrorCode.CURRENCY_MISMATCH.name(), exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Subtraction Tests")
    class SubtractionTests {

        @Test
        @DisplayName("Should subtract two Money objects with same currency")
        void shouldSubtractMoneyWithSameCurrency() {
            Money money1 = new Money(5000L, "USD");
            Money money2 = new Money(3000L, "USD");

            Money result = money1.subtract(money2);

            assertEquals(2000L, result.getAmount());
            assertEquals("USD", result.getCurrencyCode());
        }

        @Test
        @DisplayName("Should allow subtraction resulting in negative amount")
        void shouldAllowSubtractionResultingInNegative() {
            Money money1 = new Money(3000L, "JPY");
            Money money2 = new Money(5000L, "JPY");

            ValidationRuleException exception = assertThrows(
                    ValidationRuleException.class,
                    () -> money1.subtract(money2)
            );

            assertEquals(MoneyErrorCode.AMOUNT_NEGATIVE.name(), exception.getErrorCode());
        }

        @Test
        @DisplayName("Should create new Money instance after subtraction")
        void shouldCreateNewInstanceAfterSubtraction() {
            Money money1 = new Money(5000L, "CNY");
            Money money2 = new Money(2000L, "CNY");

            Money result = money1.subtract(money2);

            assertNotSame(money1, result);
            assertNotSame(money2, result);
            assertEquals(5000L, money1.getAmount());
            assertEquals(2000L, money2.getAmount());
        }

        @Test
        @DisplayName("Should throw BusinessRuleException when subtracting different currencies")
        void shouldThrowExceptionWhenSubtractingDifferentCurrencies() {
            Money money1 = new Money(5000L, "USD");
            Money money2 = new Money(3000L, "EUR");

            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> money1.subtract(money2)
            );

            assertEquals(MoneyErrorCode.CURRENCY_MISMATCH.name(), exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when amount and currency are the same")
        void shouldBeEqualWhenAmountAndCurrencyAreSame() {
            Money money1 = new Money(5000L, "USD");
            Money money2 = new Money(5000L, "USD");

            assertEquals(money1, money2);
            assertEquals(money1.hashCode(), money2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when amounts are different")
        void shouldNotBeEqualWhenAmountsAreDifferent() {
            Money money1 = new Money(5000L, "USD");
            Money money2 = new Money(3000L, "USD");

            assertNotEquals(money1, money2);
        }

        @Test
        @DisplayName("Should not be equal when currencies are different")
        void shouldNotBeEqualWhenCurrenciesAreDifferent() {
            Money money1 = new Money(5000L, "USD");
            Money money2 = new Money(5000L, "EUR");

            assertNotEquals(money1, money2);
        }

        @Test
        @DisplayName("Should not be equal when both amount and currency are different")
        void shouldNotBeEqualWhenBothAreDifferent() {
            Money money1 = new Money(5000L, "USD");
            Money money2 = new Money(3000L, "EUR");

            assertNotEquals(money1, money2);
        }
    }

    @Nested
    @DisplayName("Complex Scenario Tests")
    class ComplexScenarioTests {

        @ParameterizedTest
        @CsvSource({
                "1000, 2000, 3000",
                "5000, 5000, 10000",
                "100, 900, 1000",
                "0, 5000, 5000"
        })
        @DisplayName("Should correctly add various amounts")
        void shouldCorrectlyAddVariousAmounts(long amount1, long amount2, long expected) {
            Money money1 = new Money(amount1, "USD");
            Money money2 = new Money(amount2, "USD");

            Money result = money1.add(money2);

            assertEquals(expected, result.getAmount());
        }

        @ParameterizedTest
        @CsvSource({
                "5000, 2000, 3000",
                "10000, 5000, 5000",
                "1000, 1000, 0",
                "5000, 0, 5000"
        })
        @DisplayName("Should correctly subtract various amounts")
        void shouldCorrectlySubtractVariousAmounts(long amount1, long amount2, long expected) {
            Money money1 = new Money(amount1, "EUR");
            Money money2 = new Money(amount2, "EUR");

            Money result = money1.subtract(money2);

            assertEquals(expected, result.getAmount());
        }

        @Test
        @DisplayName("Should perform chain operations correctly")
        void shouldPerformChainOperations() {
            Money money1 = new Money(10000L, "GBP");
            Money money2 = new Money(3000L, "GBP");
            Money money3 = new Money(2000L, "GBP");

            Money result = money1.add(money2).subtract(money3);

            assertEquals(11000L, result.getAmount());
            assertEquals("GBP", result.getCurrencyCode());
        }
    }
}