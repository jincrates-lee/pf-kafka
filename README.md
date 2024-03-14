# 펫프렌즈 Kafka 토픽 통합 예제

## 모듈 의존성 그래프

![module-graphy-20240314.png](docs%2Fimages%2Fmodule-graphy-20240314.png)

- infrastructure: 인프라 관련 모듈(Kafka Consumer, Producer )
- order-service: 주문 서비스 모듈
    - order-api: 컨트롤러 API 모듈(runner)
    - order-domain: 도메인 core, service 모듈
    - order-dataacess: 데이터베이스 접근 로직 모듈
    - order-messaging: 카프카 메시지큐 관리 모듈

## API Sample

- docs/rest.http