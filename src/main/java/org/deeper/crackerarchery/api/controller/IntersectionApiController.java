package org.deeper.crackerarchery.api.controller;

import jakarta.validation.Valid;
import org.deeper.crackerarchery.api.IntersectionApi;
import org.deeper.crackerarchery.api.model.request.IntersectionRequest;
import org.deeper.crackerarchery.api.model.response.IntersectionResponse;
import org.deeper.crackerarchery.api.model.response.RequestCountResponse;
import org.deeper.crackerarchery.service.IntersectionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IntersectionApiController implements IntersectionApi {
    private final IntersectionService intersectionService;

    public IntersectionApiController(IntersectionService intersectionService) {
        this.intersectionService = intersectionService;
    }

    @Override
    public IntersectionResponse checkIntersection(@Valid @RequestBody IntersectionRequest request) {
        return intersectionService.intersection(request);
    }

    @Override
    public RequestCountResponse getRequestCount() {
        return null;
    }
}
