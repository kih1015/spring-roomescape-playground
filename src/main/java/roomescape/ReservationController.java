package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestBody ReservationCreateRequest request
    ) {
        checkMissingParameter(request);
        Reservation newReservation = new Reservation(index.incrementAndGet(), request.name(), request.date(), request.time());
        reservations.add(newReservation);
        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(newReservation);
    }

    private static void checkMissingParameter(ReservationCreateRequest request) {
        if (request.name() == null || request.date() == null || request.time() == null) {
            throw new MissingParameterException();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable Long id) {
        checkNotFoundReservation(id);
        return ResponseEntity.noContent().build();
    }

    private void checkNotFoundReservation(Long id) {
        if (!reservations.removeIf(reservation -> reservation.getId().equals(id))) {
            throw new NotFoundReservationException();
        }
    }

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<Void> handleMissingParameterException(MissingParameterException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<Void> handleNotFoundReservationException(NotFoundReservationException e) {
        return ResponseEntity.badRequest().build();
    }
}
