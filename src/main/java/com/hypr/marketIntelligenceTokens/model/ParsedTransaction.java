package com.hypr.marketIntelligenceTokens.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public record ParsedTransaction(
        LocalDateTime timestamp,
        String token,
        Long sku,
        Integer brand,
        BigDecimal gmv,
        Integer quantity,
        boolean complete
) {}
