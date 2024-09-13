package roomescape.domain.time.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import roomescape.domain.time.dto.TimeCreateRequest;
import roomescape.domain.time.dto.TimeResponse;
import roomescape.domain.time.service.TimeService;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class TimeController {

    private final TimeService timeService;

    @GetMapping
    public ResponseEntity<List<TimeResponse>> getTimes() {
        List<TimeResponse> response = timeService.getTimes();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TimeResponse> createTime(
        @Valid @RequestBody TimeCreateRequest request
    ) {
        TimeResponse response = timeService.createTime(request);
        return ResponseEntity.created(URI.create("/times/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(
        @PathVariable Long id
    ) {
        timeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
