package me.jincrates.pf.order.domain.core.valueobject;

import java.util.Objects;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class BaseId<T> {

    private T value;

    protected BaseId(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        BaseId<?> baseId = (BaseId<?>) object;
        return Objects.equals(value, baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
