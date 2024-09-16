package roomescape.domain.reservation.controller;

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
import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.service.ReservationService;

@RequestMapping("/reservations")
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> response = reservationService.getReservations();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
        @Valid @RequestBody ReservationCreateRequest request
    ) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
        @PathVariable Long id
    ) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
