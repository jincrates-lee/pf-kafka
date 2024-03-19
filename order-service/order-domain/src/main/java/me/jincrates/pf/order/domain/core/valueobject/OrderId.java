package me.jincrates.pf.order.domain.core.valueobject;

import java.util.UUID;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderId extends BaseId<UUID> {

    public OrderId(UUID value) {
        super(value);
    }
}
