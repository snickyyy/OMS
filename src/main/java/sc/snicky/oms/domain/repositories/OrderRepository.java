package sc.snicky.oms.domain.repositories;

import jakarta.annotation.Nonnull;
import sc.snicky.oms.domain.exceptions.BusinessRuleException;
import sc.snicky.oms.domain.exceptions.ModelNotFoundException;
import sc.snicky.oms.domain.models.Order;
import sc.snicky.oms.domain.models.values.order.CustomerId;
import sc.snicky.oms.domain.models.values.order.OrderId;
import sc.snicky.oms.domain.models.values.order.OrderNumber;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    /**
     * Saves a given order.
     *
     * @param order The order to save. Must not be null.
     * @throws BusinessRuleException if the order creation fails due to a business rule violation (e.g., invalid data).
     */
    void save(@Nonnull Order order);

    /**
     * Finds an order by its unique identifier.
     *
     * @param id The ID of the order to find. Must not be null.
     * @return An {@link Optional} containing the found order, or {@link Optional#empty()} if no order is found.
     */
    Optional<Order> findById(@Nonnull OrderId id);

    /**
     * Finds an order by its unique identifier.
     *
     * @param number The number of the order to find. Must not be null.
     * @return An {@link Optional} containing the found order, or {@link Optional#empty()} if no order is found.
     */
    Optional<Order> findByNumber(@Nonnull OrderNumber number);

    /**
     * Gets all for customer.
     *
     * @param customerId the customer id
     * @return the all for customer
     */
    List<Order> getAllForCustomer(@Nonnull CustomerId customerId);

    /**
     * Is exist by order number boolean.
     *
     * @param number the number of orde
     * @return the boolean
     */
    boolean isExistByOrderNumber(@Nonnull OrderNumber number);

    /**
     * Deletes an order by its unique identifier.
     *
     * @param id The ID of the order to delete. Must not be null.
     * @throws ModelNotFoundException if no order is found with the given ID.
     */
    void delete(@Nonnull OrderId id);
}
