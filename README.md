# 펫프렌즈 Kafka 토픽 통합 예제

## 모듈 의존성 그래프

![module-graphy-20240314.png](docs%2Fimages%2Fmodule-graphy-20240314.png)

- infrastructure: 인프라 관련 모듈(Kafka Consumer, Producer )
- order-service: 주문 서비스 모듈
    - order-api: 컨트롤러 API 모듈(runner)
    - order-domain: 도메인 core, service 모듈
    - order-dataacess: 데이터베이스 접근 로직 모듈
    - order-messaging: 카프카 메시지큐 관리 모듈

## 컴포넌트 다이어그램

![component-diagram-20240326.png](docs%2Fimages%2Fcomponent-diagram-20240326.png)

## 시퀀스 다이어그램

![sequence-diagram-20240326.png](docs%2Fimages%2Fsequence-diagram-20240326.png)

## 요구사항

1. 인터페이스 제공
2. 멀티모듈을 고려하여 도메인 영역에 위치
3. After Commit 보장
4. 전송 실패 레코드 등록 보장

## 요구사항 접근방법

### 1. 인터페이스 제공

- domain 모듈 input, output 포트 생성
    - [/pf/order/domain/service/port/input/OrderEventListener](https://github.com/jincrates-lee/pf-kafka/blob/main/order-service/order-domain/src/main/java/me/jincrates/pf/order/domain/service/port/input/OrderEventListener)
    - [/pf/order/domain/service/port/output/OrderCreatedEventPublisher](https://github.com/jincrates-lee/pf-kafka/blob/main/order-service/order-domain/src/main/java/me/jincrates/pf/order/domain/service/port/output/OrderCreatedEventPublisher.java)
    - [/pf/order/domain/service/port/output/OrderCompletedEventPublisher](https://github.com/jincrates-lee/pf-kafka/blob/main/order-service/order-domain/src/main/java/me/jincrates/pf/order/domain/service/port/output/OrderCompletedEventPublisher.java)
    - [/pf/order/domain/service/port/output/OrderCancelledEventPublisher](https://github.com/jincrates-lee/pf-kafka/blob/main/order-service/order-domain/src/main/java/me/jincrates/pf/order/domain/service/port/output/OrderCancelledEventPublisher.java)

### 2. 멀티모듈을 고려하여 도메인 영역에 위치

- infrastructure라는 별도 독립적인 모듈로 분리(의존성 그래프 참조)

### 3. After Commit 보장

- 2가지 방법을 구현해보았습니다.

#### 3.1. Transaction 서비스 분리 - [예제1](https://github.com/jincrates-lee/pf-kafka/blob/main/order-service/order-domain/src/main/java/me/jincrates/pf/order/domain/service/OrderServiceImpl.java)

- 장점: 상대적으로 간편하게 처리 가능
- 단점: after_commit이 직관적이지 않음

```java
// 예제1. Transaction 서비스 분리
// 1) 트랜잭션을 달지 않는다!
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderServiceHelper orderServiceHelper;
    private final OrderCreatedEventPublisher orderCreatedEventPublisher;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = orderMapper.toDomain(request);
        OrderCreatedEvent event = orderServiceHelper.persistOrder(order);  // 2) 하위 서비스에서 트랜잭션 처리 후
        orderCreatedEventPublisher.publish(event);  // 3) 이벤트 발행
        return orderMapper.toResponse(event.getOrder());
    }

    // ....
}

// 트랜잭션 처리하는 헬퍼 서비스
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderServiceHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;

    // 2.1) 별도 하위 서비스에서 트랜잭션을 처리하도록 처리
    @Transactional
    public OrderCreatedEvent persistOrder(Order order) {
        OrderCreatedEvent event = orderDomainService.createOrder(order);
        saveOrder(event.getOrder());
        return event;
    }

    //....
}

```

#### 3.2. TransactionListener 사용 - [예제2](https://github.com/jincrates-lee/pf-kafka/blob/main/order-service/order-domain/src/main/java/me/jincrates/pf/order/domain/service/OrderServiceImplV2.java)

- 장점: after_commit가 명시적으로 보장됨
- 단점: 관리 클래스가 늘어남

```java

@Primary
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImplV2 implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final ApplicationDomainEventPublisher domainEventPublisher;

    @Override
    @Transactional  // 1) 트랜잭션
    public OrderResponse createOrder(OrderRequest request) {
        Order order = orderMapper.toDomain(request);
        OrderCreatedEvent event = orderDomainService.createOrder(order);
        saveOrder(event.getOrder());
        domainEventPublisher.publish(event);  // 2) 스프링 이벤트 발행
        return orderMapper.toResponse(event.getOrder());
    }

    //....
}

@Slf4j
@Component
public class ApplicationDomainEventPublisher implements
        ApplicationEventPublisherAware,
        DomainEventPublisher<DomainEvent> {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        this.applicationEventPublisher.publishEvent(event);
        log.info("{} is published!", event.getClass().getSimpleName());
    }
}

// 스프링 이벤트 리스너
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventApplicationListener {

    private final OrderCreatedEventPublisher orderCreatedEventPublisher;

    // 트랜잭션 이벤트 리스너 적용
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void process(OrderCreatedEvent event) {
        orderCreatedEventPublisher.publish(event);
    }

    // ....
}
```

### 4. 전송 실패 레코드 등록 보장

[pf/kafka/producer/KafkaProducerImpl](https://github.com/jincrates-lee/pf-kafka/blob/main/infrastructure/src/main/java/me/jincrates/pf/kafka/producer/KafkaProducerImpl.java)

```java

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl<T> implements KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topic, String key, TopicMessage message) {
        log.info("Sending to topic = {}, from message: {}", topic, message);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String messageStr = objectMapper.writeValueAsString(message);

            kafkaTemplate.send(topic, key, messageStr);
        } catch (KafkaException | JsonProcessingException ex) {
            log.error("Error on kafka producer with key: {}, message: {}, and exception: {}", key,
                    message, ex.getMessage());
            throw new RuntimeException("카프카 메시지 발송 오류! - 전송 실패 오류 레코드 등록");
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}

```

## API Sample

- docs/rest.http

