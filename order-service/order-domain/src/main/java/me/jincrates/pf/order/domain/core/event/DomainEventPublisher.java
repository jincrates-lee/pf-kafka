package me.jincrates.pf.order.domain.core.event;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T event);
}

