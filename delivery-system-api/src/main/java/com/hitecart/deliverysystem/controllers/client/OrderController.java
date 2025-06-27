package com.hitecart.deliverysystem.controllers.client;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.OrderApi;
import com.hitecart.deliverysystem.dto.order.OrderRequest;
import com.hitecart.deliverysystem.dto.order.OrderResponse;
import com.hitecart.deliverysystem.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderResponse> saveOrder(OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.save(orderRequest));
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrderInfo(Integer orderId ,OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.updateOrderInfo(orderId ,orderRequest));
    }

    @Override
    public ResponseEntity<OrderResponse> findOrderById(Integer id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @Override
    public ResponseEntity<OrderResponse> findOrderByCode(String code) {
        return ResponseEntity.ok(orderService.findByCode(code));
    }

    @Override
    public ResponseEntity<List<OrderResponse>> findAllOrder() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteOrder(Integer id) {
        try {
            orderService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Order deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Order not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<OrderResponse>> findAllOrdersPageable(int page, int size) {
        return ResponseEntity.ok(orderService.findAllOrders(page, size));
    }
}
