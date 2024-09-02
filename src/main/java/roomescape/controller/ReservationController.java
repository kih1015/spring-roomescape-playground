package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.exception.MissingParameterException;
import roomescape.exception.NotFoundReservationException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong();

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        return ResponseEntity.ok(
                reservations.stream()
                        .map(ReservationResponse::from)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationCreateRequest request
    ) {
        if (request.name().isBlank() || request.date() == null || request.time() == null) {
            throw new MissingParameterException();
        }
        Reservation newReservation = new Reservation(index.incrementAndGet(), request.name(), request.date(), request.time());
        reservations.add(newReservation);
        ReservationResponse response = ReservationResponse.from(newReservation);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (!reservations.removeIf(reservation -> reservation.getId().equals(id))) {
            throw new NotFoundReservationException();
        }
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<String> handleMissingParameterException(MissingParameterException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<Void> handleNotFoundReservationException(NotFoundReservationException e) {
        return ResponseEntity.badRequest().build();
    }
}
