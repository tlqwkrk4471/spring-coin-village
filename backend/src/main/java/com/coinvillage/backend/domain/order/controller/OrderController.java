package com.coinvillage.backend.domain.order.controller;

import com.coinvillage.backend.domain.coin.entry.Coin;
import com.coinvillage.backend.domain.coin.repository.CoinRepository;
import com.coinvillage.backend.domain.order.entry.Order;
import com.coinvillage.backend.domain.order.service.OrderService;
import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.order.dto.OrderRequest;
import com.coinvillage.backend.domain.order.dto.OrderResponse;
import com.coinvillage.backend.domain.order.dto.OrderResponseByCoin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final CoinRepository coinRepository;

    @Autowired
    public OrderController(OrderService orderService, CoinRepository coinRepository) {
        this.orderService = orderService;
        this.coinRepository = coinRepository;
    }

    /**
     * 주문 시도
     * @return 주문 성공 여부
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("")
    public Boolean placeOrder(@SessionAttribute(name = "loginUser") User loginUser,
                              @RequestBody OrderRequest orderRequest) {

        Order order = new Order(loginUser, coinRepository.findBySymbol(orderRequest.getCoinSymbol()).get(),
                orderRequest.getPrice(), orderRequest.getQuantity(), orderRequest.getOrderSide(), orderRequest.getOrderType());

        return orderService.placeOrder(order);
    }

    /**
     * 유저의 대기 중인 주문 조회 (활성화된 주문 조회)
     * @return loginUser의 PENDING, PARTIAL 상태인 주문 리스트
     */
    @GetMapping("/active")
    public List<OrderResponse> getActiveOrdersByUser(@SessionAttribute(name = "loginUser") User loginUser) {
        List<Order> activeOrders = orderService.getActiveOrdersByUser(loginUser);
        return activeOrders.stream()
                .map(OrderResponse::from)
                .toList();
    }

    /**
     * 코인의 대기 중인 주문 조회 (활성화된 주문 조회)
     * @return coin의 PENDING, PARTIAL 상태인 주문 리스트
     */
    @GetMapping("/active/{coinSymbol}")
    public ResponseEntity<List<OrderResponseByCoin>> getActiveOrdersByCoin(@PathVariable String coinSymbol) {
        Optional<Coin> entry = coinRepository.findBySymbol(coinSymbol.toUpperCase());

        return entry.map(coin -> ResponseEntity.ok(orderService.getAggregatedActiveOrdersByCoin(coin)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 완료된 주문 조회
     * @return PARTIAL_FILLED, FILLED 상태인 주문 리스트
     */
    @GetMapping("/filled")
    public List<OrderResponse> getFilledOrders(@SessionAttribute(name = "loginUser") User loginUser) {
        List<Order> filledOrders = orderService.getFilledOrdersByUser(loginUser);
        return filledOrders.stream()
                .map(OrderResponse::from)
                .toList();
    }

    /**
     * 주문 취소
     * @return 주문 취소 성공 여부
     */
    @DeleteMapping("/{orderId}")
    public Boolean cancelOrder(@PathVariable UUID orderId) {
        return orderService.cancelOrder(orderId);
    }
}
