package sc.snicky.oms.domain.models.values;

import lombok.Getter;
import sc.snicky.oms.domain.exceptions.BusinessRuleException;
import sc.snicky.oms.domain.exceptions.ValidationRuleException;
import sc.snicky.oms.domain.exceptions.codes.MoneyErrorCode;

@Getter
public class Money {
    private static final int CURRENCY_CODE_LENGTH = 3;

    private final long amount;
    private final String currencyCode;

    public Money(long amount, String currencyCode) {
        this.amount = amount;
        this.currencyCode = currencyCode;
        validate();
    }

    public void validate() {
        if (amount < 0) {
            throw new ValidationRuleException(
                    "Amount cannot be negative",
                    MoneyErrorCode.AMOUNT_NEGATIVE.name()
            );
        }
        if (currencyCode == null || currencyCode.isEmpty()) {
            throw new ValidationRuleException(
                    "Currency code cannot be null or empty",
                    MoneyErrorCode.CURRENCY_CODE_EMPTY_OR_NULL.name()
            );
        }
        if (currencyCode.length() != CURRENCY_CODE_LENGTH) {
            throw new ValidationRuleException(
                    "Currency code must be exactly 3 characters long",
                    MoneyErrorCode.CURRENCY_CODE_INVALID.name()
            );
        }
    }

    public Money add(Money money) {
        checkCurrencyMatch(money);
        return new Money(this.amount + money.amount, this.currencyCode);
    }

    public Money subtract(Money money) {
        checkCurrencyMatch(money);
        return new Money(this.amount - money.amount, this.currencyCode);
    }

    private void checkCurrencyMatch(Money money) {
        if (!this.currencyCode.equals(money.currencyCode)) {
            throw new BusinessRuleException(
                    "Currency codes do not match",
                    MoneyErrorCode.CURRENCY_MISMATCH.name()
            );
        }
    }
}
