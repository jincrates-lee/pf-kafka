package me.jincrates.pf.order.api.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.order.domain.core.valueobject.OrderId;
import me.jincrates.pf.order.domain.service.dto.OrderRequest;
import me.jincrates.pf.order.domain.service.dto.OrderResponse;
import me.jincrates.pf.order.domain.service.port.input.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @RequestBody OrderRequest request
    ) {
        log.info("[POST] 주문 생성 요청 - /api/v1/orders :: request: {}", request);
        OrderResponse response = orderService.createOrder(request);
        log.info("주문 생성 요청 성공 orderId: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderId> cancelrder(
        @PathVariable(name = "orderId") String orderId
    ) {
        log.info("[PUT] 주문 취소 요청 - /api/v1/orders :: id: {}", orderId);
        orderService.cancelOrder(orderId);
        log.info("주문 취소 요청 성공 orderId: {}", orderId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new OrderId(UUID.fromString(orderId)));
    }
}
