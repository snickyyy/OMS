package sc.snicky.oms.domain.models.values;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class BaseValueObject<T> {
    private final T value;

    protected BaseValueObject(T value) {
        this.value = value;
        validate();
    }

    public abstract void validate();
}
