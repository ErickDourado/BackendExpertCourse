package com.erick.orderserviceapi.service;

import models.requests.CreateOrderRequest;

public interface OrderService {

    void save(CreateOrderRequest request);

}
