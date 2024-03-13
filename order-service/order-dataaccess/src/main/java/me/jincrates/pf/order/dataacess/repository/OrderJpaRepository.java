package me.jincrates.pf.order.dataacess.repository;

import java.util.UUID;
import me.jincrates.pf.order.dataacess.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

}