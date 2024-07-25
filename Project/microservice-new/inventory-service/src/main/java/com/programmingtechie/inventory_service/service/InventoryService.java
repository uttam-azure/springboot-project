package com.programmingtechie.inventory_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.inventory_service.dto.InventoryResponse;
import com.programmingtechie.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse>  inStock(List<String> skuCode){

       return inventoryRepository.findBySkuCodeIn(skuCode)
       .stream()
       .map(
        inventory ->InventoryResponse.builder()
        .skuCode(inventory.getSkuCode())
        .isInStock(inventory.getQuantity()>0)
        .build()).toList();
    
}

}
