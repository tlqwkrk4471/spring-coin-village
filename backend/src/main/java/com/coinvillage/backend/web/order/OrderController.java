package com.coinvillage.backend.web.order;

import com.coinvillage.backend.domain.coin.repository.CoinRepository;
import com.coinvillage.backend.domain.order.Order;
import com.coinvillage.backend.domain.order.OrderSide;
import com.coinvillage.backend.domain.order.service.OrderService;
import com.coinvillage.backend.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Boolean placeOrder(@SessionAttribute(name = "loginUser", required = true) User loginUser,
                              @RequestBody OrderRequest orderRequest) {

        Order order = new Order(loginUser, coinRepository.findBySymbol(orderRequest.getCoinSymbol()).get(),
                orderRequest.getPrice(), orderRequest.getQuantity(), orderRequest.getOrderSide(), orderRequest.getOrderType());

        return orderService.placeOrder(order);
    }

    /**
     * 대기 중인 주문 조회 (활성화된 주문 조회)
     * @return PENDING, PARTIAL 상태인 주문 리스트
     */
    @GetMapping("/active")
    public List<OrderResponse> getActiveOrders(@SessionAttribute(name = "loginUser", required = true) User loginUser) {
        List<Order> activeOrders = orderService.getActiveOrdersByUser(loginUser);
        return activeOrders.stream()
                .map(OrderResponse::from)
                .toList();
    }
}
