package me.jincrates.pf.order.domain.service.port.output;

import me.jincrates.pf.order.domain.core.event.DomainEventPublisher;
import me.jincrates.pf.order.domain.core.event.OrderCompletedEvent;

public interface OrderCompletedEventPublisher extends
    DomainEventPublisher<OrderCompletedEvent> {

}
