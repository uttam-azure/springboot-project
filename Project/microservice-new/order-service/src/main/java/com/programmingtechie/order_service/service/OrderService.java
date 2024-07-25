package com.programmingtechie.order_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.programmingtechie.order_service.dto.InventoryResponse;
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
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> listOrderItems = orderRequest.getOrderLineItemsDto()
        .stream().map(this::mapToDto).toList();
        order.setOrderLineItemsList(listOrderItems);

       List<String> skuCodes = order.getOrderLineItemsList().stream().map(
           OrderLineItem::getSkuCode
        ).toList();
        //Plcae oder when it is present in inventory
        InventoryResponse [] inventoryResponsesArray = webClientBuilder.build().get()
        .uri("http://localhost:8082/api/inventory",uriBuilder->
        uriBuilder.queryParam("skuCode",skuCodes).build() )
        .retrieve()
        .bodyToMono(InventoryResponse[].class)
        .block();

        boolean allProductInStock = Arrays.stream(inventoryResponsesArray)
        .allMatch(InventoryResponse::getIsInStock);
        if(allProductInStock)
        orderRepository.save(order);
        else
        throw new IllegalArgumentException("Product is not available, try nest time");
        
    }

    private OrderLineItem mapToDto(OrderLineItemsDto orderLineItemsDto) {
      
        OrderLineItem orderLineItem = new OrderLineItem(); 
        orderLineItem.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItem.setPrice(orderLineItemsDto.getPrice());
        orderLineItem.setQuantity(orderLineItemsDto.getQuantity());
        return orderLineItem;
    }

}
