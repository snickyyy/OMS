package sc.snicky.oms.domain.repositories;

import jakarta.annotation.Nonnull;
import sc.snicky.oms.domain.exceptions.BusinessRuleException;
import sc.snicky.oms.domain.models.Payment;

public interface PaymentRepository {
    /**
     * Saves a given payment.
     *
     * @param payment The payment to save. Must not be null.
     * @throws BusinessRuleException if the payment creation fails due to a business rule violation (e.g., invalid data).
     */
    void save(@Nonnull Payment payment);
}
