package com.pedro.delivery_api.dto.request;

import com.pedro.delivery_api.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateDTO(
        @NotNull
        OrderStatus orderStatus
) {}
