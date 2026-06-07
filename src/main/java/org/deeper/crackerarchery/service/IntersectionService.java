package org.deeper.crackerarchery.service;

import lombok.AllArgsConstructor;
import org.deeper.crackerarchery.api.model.request.IntersectionRequest;
import org.deeper.crackerarchery.api.model.response.IntersectionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IntersectionService {
    public IntersectionResponse intersection(IntersectionRequest request) {
        return new IntersectionResponse(List.of());
    }
}
