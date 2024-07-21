package com.programmingtechie.order_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.programmingtechie.order_service.dto.OrderLineItemsDto;
import com.programmingtechie.order_service.dto.OrderRequest;
import com.programmingtechie.order_service.model.Order;
import com.programmingtechie.order_service.model.OrderLineItem;
import com.programmingtechie.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> listOrderItems = orderRequest.getOrderLineItemsDto()
        .stream().map(this::mapToDto).toList();
        order.setOrderLineItemsList(listOrderItems);
        orderRepository.save(order);
        
    }

    private OrderLineItem mapToDto(OrderLineItemsDto orderLineItemsDto) {
      
        OrderLineItem orderLineItem = new OrderLineItem(); 
        orderLineItem.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItem.setPrice(orderLineItemsDto.getPrice());
        orderLineItem.setQuantity(orderLineItemsDto.getQuantity());
        return orderLineItem;
    }

}
