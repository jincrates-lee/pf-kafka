package me.jincrates.pf.order.dataacess;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.order.dataacess.entity.OrderEntity;
import me.jincrates.pf.order.dataacess.mapper.OrderDataAccessMapper;
import me.jincrates.pf.order.dataacess.repository.OrderJpaRepository;
import me.jincrates.pf.order.domain.core.entity.Order;
import me.jincrates.pf.order.domain.core.valueobject.OrderId;
import me.jincrates.pf.order.domain.service.port.output.OrderRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    @Override
    public Order save(Order order) {
        OrderEntity entity = orderJpaRepository.save(orderDataAccessMapper.toEntity(order));
        return orderDataAccessMapper.toDomain(entity);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return orderJpaRepository.findById(id.getValue())
            .map(orderDataAccessMapper::toDomain);
    }
}
