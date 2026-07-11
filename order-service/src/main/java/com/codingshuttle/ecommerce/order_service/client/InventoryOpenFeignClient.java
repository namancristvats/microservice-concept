package com.codingshuttle.ecommerce.order_service.client;

import com.codingshuttle.ecommerce.order_service.dto.OrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="inventory-service",path="/inventory")
public interface InventoryOpenFeignClient {
    @PutMapping("/reduce-stocks")
    public Double reduceStocks(@RequestBody OrderRequestDto orderRequestDto);
}
