package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.ClientRepository;
import com.hitecart.deliverysystem.dao.DeliveryServiceRepository;
import com.hitecart.deliverysystem.dao.OrderRepository;
import com.hitecart.deliverysystem.dto.order.OrderRequest;
import com.hitecart.deliverysystem.dto.order.OrderResponse;
import com.hitecart.deliverysystem.dto.orderline.OrderLineRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.OrderLineMapper;
import com.hitecart.deliverysystem.mapper.OrderMapper;
import com.hitecart.deliverysystem.model.*;
import com.hitecart.deliverysystem.dao.OrderLineRepository;
import com.hitecart.deliverysystem.model.enums.OrderStatus;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.services.OrderService;
import com.hitecart.deliverysystem.services.DeliveryServiceService;
import com.hitecart.deliverysystem.services.UserService;
import com.hitecart.deliverysystem.utils.CalculeTva;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.OrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ClientService clientService;
    private final UserService userServiceImpl;
    private final DeliveryServiceService deliveryServiceService;
    private final ClientRepository clientRepository;
    private final OrderMapper orderMapper;
    private final OrderLineMapper orderLineMapper;
    private final DeliveryServiceRepository deliveryServiceRepository;


    @Override
    public OrderResponse save(OrderRequest orderRequest) {
        checkDto(orderRequest, "Commande Client n'est pas valide",
                "la commande client n'est pas valide", ErrorCodes.ORDER_NOT_VALID);
        if (orderRequest.id() != null && orderRequest.orderStatus().equals(OrderStatus.COMPLETED)) {
            throw new InvalidOperationException("Unable to modify the Order once it is completed",
                    ErrorCodes.ORDER_NOT_VALID_OPERATION);
        }
        return savingOrder(orderRequest);
    }

    private OrderResponse savingOrder(OrderRequest orderRequest) {

        BigDecimal[] totalAmountHT = {BigDecimal.ZERO};
        BigDecimal[] totalAmountTTC = {BigDecimal.ZERO};

        Client client = clientRepository.findById(orderRequest.clientId()).orElseThrow(
                () -> new EntityNotFoundException("Client not found ", ErrorCodes.CLIENT_NOT_FOUND)
        );
        Order savedOrder = orderMapper.toEntity(orderRequest);
        savedOrder.setClient(client);
        savedOrder = orderRepository.save(savedOrder);

        if (!orderRequest.orderLines().isEmpty()) {

            Order finalSavedOrder = savedOrder;

            for (OrderLineRequest orderLineRequest : orderRequest.orderLines()) {
                BigDecimal prixttc = CalculeTva.calculeTTC(orderLineRequest.unitPrice(), orderLineRequest.tva());
                // Cumul des montants HT et TTC
                totalAmountHT[0] = totalAmountHT[0].add(orderLineRequest.unitPrice().multiply(orderLineRequest.quantity()));
                totalAmountTTC[0] = totalAmountTTC[0].add(prixttc.multiply(orderLineRequest.quantity()));

                OrderLine cmdLineToSave = orderLineMapper.toEntity(orderLineRequest);
                cmdLineToSave.setTtcPrice(prixttc);
                cmdLineToSave.setOrder(finalSavedOrder);
                DeliveryService deliveryService = this.getServiceDeliveryById(orderLineRequest.deliveryServiceId());
                if (deliveryService == null) {
                    throw new InvalidOperationException("DeliveryServiceFullDto ID is missing for one of the command lines",
                            ErrorCodes.DELIVERY_SERVICE_NOT_FOUND);
                }
                cmdLineToSave.setDeliveryService(deliveryService);

                orderLineRepository.save(cmdLineToSave);
            }
        }
        savedOrder.setTotalAmountHT(totalAmountHT[0]);
        savedOrder.setTotalAmountTTC(totalAmountTTC[0]);
        savedOrder = orderRepository.save(savedOrder);

        return orderMapper.toResponse(savedOrder);

    }

    @Override
    public OrderResponse updateOrderInfo(Integer orderId ,OrderRequest orderRequest) {
/*        checkDto(orderRequest, "Commande Client n'est pas valide",
                "la commande client n'est pas valide", ErrorCodes.ORDER_NOT_VALID);*/
        var existingOrder = orderRepository.findById(orderRequest.id()).orElseThrow(
                () -> new EntityNotFoundException("Order with ::ID : " + orderRequest.id() + "not found ", ErrorCodes.ORDER_NOT_FOUND)
        );
        if (orderId != null && orderRequest.orderStatus().equals(OrderStatus.COMPLETED)) {
            throw new InvalidOperationException("Unable to modify the Order once it is completed",
                    ErrorCodes.ORDER_NOT_VALID_OPERATION);
        }

        orderMapper.updateOrderFromRequest(orderRequest, existingOrder);
        Order updatedOrder = orderRepository.save(existingOrder);

    return orderMapper.toResponse(updatedOrder);
    }

    /**
     * @param deliveryServiceId
     * @return DeliveryService
     */
    private DeliveryService getServiceDeliveryById(Integer deliveryServiceId) {
        return deliveryServiceRepository.findById(deliveryServiceId).orElseThrow(
                () -> new EntityNotFoundException("Delivery service whit ::ID : " + deliveryServiceId +
                        " not found", ErrorCodes.ORDER_NOT_FOUND)
        );
    }

    @Override
    public OrderResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Commande Client est null",
                "Impossible de trouver une commande client avec un ID null",
                ErrorCodes.ORDER_NOT_VALID_OPERATION);

        Order orderById = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Opps! Aucune Commande Client  avec l'id :"
                + id + "n'a ete trouvé dans la base deonnée"
                , ErrorCodes.ORDER_NOT_FOUND));

        return orderMapper.toResponse(orderById);
    }

    @Override
    public OrderResponse findByCode(String code) {
        CheckParmasUtil.checkCode(code, "ERREUR ! Le parametre Code Commande client  est null ",
                "Impossible de trouver une commande client avec un Code null", ErrorCodes.ORDER_NOT_VALID_OPERATION);

        Order orderByCode = orderRepository.findOrderByCode(code).orElseThrow(() -> new EntityNotFoundException("Opps! Aucune Commande Client  avec l'id :"
                + code + "n'a ete trouvé dans la base donnée"
                , ErrorCodes.ORDER_NOT_FOUND));
        return orderMapper.toResponse(orderByCode);
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                //.map(OrderResponse::fromEntity)
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id, "l'ID de commande client  à supprimer est null",
                "Impossible de supprimer une commande client  avec un ID null",
                ErrorCodes.ORDER_NOT_VALID_OPERATION);

        List<OrderLine> orderLines = orderLineRepository.findAllByOrderId(id);
        if (!orderLines.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer une commande client qui a deja des lignes de commandes"
                    , ErrorCodes.ORDER_NOT_VALID_OPERATION);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public PageResponse<OrderResponse> findAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findAllOrders(pageable);
        List<OrderResponse> orderResponses = orders.stream()
                .map(orderMapper::toResponse)
                .toList();

        return new PageResponse<>(
                orderResponses,
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages(),
                orders.isFirst(),
                orders.isLast()
        );
    }

    private void checkDto(OrderRequest orderRequest, String msgLog, String msgException, ErrorCodes commandeclientNotValid) {
        List<String> errors = OrderValidator.validate(orderRequest);
        if (!errors.isEmpty()) {
            log.error(msgLog, orderRequest);
            throw new InvalidEntityException(msgException, ErrorCodes.DRIVER_NOT_VALID, errors);
        }
    }

}
