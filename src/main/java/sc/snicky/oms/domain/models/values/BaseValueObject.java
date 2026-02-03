package sc.snicky.oms.domain.models.values;

import lombok.Getter;

@Getter
public abstract class BaseValueObject<T> {
    private final T value;

    protected BaseValueObject(T value) {
        this.value = value;
        validate();
    }

    public abstract void validate();
}
