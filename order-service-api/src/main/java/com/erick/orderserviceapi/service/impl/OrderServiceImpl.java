package com.erick.orderserviceapi.service.impl;

import com.erick.orderserviceapi.mapper.OrderMapper;
import com.erick.orderserviceapi.repository.OrderRepository;
import com.erick.orderserviceapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.requests.CreateOrderRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public void save(CreateOrderRequest request) {
        final var entity = mapper.toOrder(request);
        repository.save(entity);
        log.info("Order [{}] saved successfully", entity);
    }

}
