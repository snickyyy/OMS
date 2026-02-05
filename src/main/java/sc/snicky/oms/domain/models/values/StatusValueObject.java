package sc.snicky.oms.domain.models.values;

public interface StatusValueObject<T> {
    boolean canTransitionTo(T newStatus);
}
