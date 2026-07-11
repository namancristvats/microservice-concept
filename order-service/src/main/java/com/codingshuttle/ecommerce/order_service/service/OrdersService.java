package com.codingshuttle.ecommerce.order_service.service;

import com.codingshuttle.ecommerce.order_service.client.InventoryOpenFeignClient;
import com.codingshuttle.ecommerce.order_service.dto.OrderRequestDto;
import com.codingshuttle.ecommerce.order_service.entity.OrderItem;
import com.codingshuttle.ecommerce.order_service.entity.OrderStatus;
import com.codingshuttle.ecommerce.order_service.entity.Orders;
import com.codingshuttle.ecommerce.order_service.repoitory.OrdersRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    private static final Logger log = LoggerFactory.getLogger(OrdersService.class);

    private final OrdersRepository orderRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient client;

    public OrdersService(OrdersRepository orderRepository, ModelMapper modelMapper, InventoryOpenFeignClient client) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.client = client;
    }

    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Orders> orders = orderRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    public OrderRequestDto getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return modelMapper.map(order, OrderRequestDto.class);
    }

    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("Calling the createOrder method");
        Double totalPrice= client.reduceStocks(orderRequestDto);

        Orders orders=modelMapper.map(orderRequestDto,Orders.class);
        for(OrderItem orderItem:orders.getItems()){
            orderItem.setOrder(orders);
        }
        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);
        Orders savedOrder=orderRepository.save(orders);
        return modelMapper.map(savedOrder,OrderRequestDto.class);

    }

}










