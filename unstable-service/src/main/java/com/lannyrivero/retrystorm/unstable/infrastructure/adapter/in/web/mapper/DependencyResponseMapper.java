package com.lannyrivero.retrystorm.unstable.infrastructure.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import com.lannyrivero.retrystorm.unstable.domain.model.DependencyOutcome;
import com.lannyrivero.retrystorm.unstable.infrastructure.adapter.in.web.dto.DependencyResponse;

@Component
public class DependencyResponseMapper {

    public DependencyResponse toResponse(DependencyOutcome outcome) {
        return new DependencyResponse(
                outcome.logicalRequestId().value(),
                outcome.attempt().value(),
                outcome.successful());
    }
}
