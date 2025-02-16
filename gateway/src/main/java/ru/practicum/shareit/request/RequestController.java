package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(userIdHeader) @NotNull Long userId,
                                             @RequestBody @Valid ItemRequestDto request) {
        log.info("Creating request {}, userId={}", request, userId);
        return requestClient.save(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(userIdHeader) @NotNull Long userId) {
        log.info("Get user requests, userId={}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        log.info("Get all requests");
        return requestClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequest(@PathVariable Long id) {
        log.info("Get request, requestId={}", id);
        return requestClient.getById(id);
    }
}
