package com.coinvillage.backend.domain.order.entry;

public enum OrderStatus {
    PENDING,    // 주문이 처음 생성됨, 아직 아무 체결 되지 않음
    PARTIAL_FILLED,    // 주문이 일부만 체결됨
    FILLED,     // 주문 수량이 전부 체결됨, 주문이 종료됨
    CANCELLED   // 사용자가 주문을 취소함
}
