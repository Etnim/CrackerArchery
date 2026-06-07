package org.deeper.crackerarchery.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.deeper.crackerarchery.api.IntersectionApi;
import org.deeper.crackerarchery.api.model.request.IntersectionRequest;
import org.deeper.crackerarchery.api.model.response.IntersectionResponse;
import org.deeper.crackerarchery.api.model.response.RequestCountResponse;
import org.deeper.crackerarchery.service.IntersectionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class IntersectionApiController implements IntersectionApi {
    private final IntersectionService intersectionService;

    @Override
    public IntersectionResponse checkIntersection(@Valid @RequestBody IntersectionRequest request) {
        return intersectionService.intersection(request);
    }

    @Override
    public RequestCountResponse getRequestCount() {
        return null;
    }
}
