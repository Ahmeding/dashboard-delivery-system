package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.order.OrderRequest;
import com.hitecart.deliverysystem.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse save(OrderRequest dto);
    OrderResponse updateOrderInfo(Integer orderId ,OrderRequest dto);
    OrderResponse findById(Integer id);
    OrderResponse findByCode(String email);
    List<OrderResponse> findAll();
    void delete(Integer id);

    PageResponse<OrderResponse> findAllOrders(int page, int size);
}
