package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.exception.NotFoundReservationException;

import javax.sql.DataSource;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationController(
        JdbcTemplate jdbcTemplate,
        DataSource source
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
            .withTableName("reservation")
            .usingGeneratedKeyColumns("id");
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        String sql = "select * from reservation";
        List<ReservationResponse> response = jdbcTemplate.query(
                sql,
                getReservationRowMapper()
            ).stream()
            .map(ReservationResponse::from)
            .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
        @Valid @RequestBody ReservationCreateRequest request
    ) {
        Map<String, Object> params = Map.of(
            "name", request.name(),
            "date", request.date(),
            "time", request.time()
        );
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        String sql = "select * from reservation where id = ?";
        Reservation newReservation = jdbcTemplate.queryForObject(
            sql,
            getReservationRowMapper(),
            id
        );
        ReservationResponse response = ReservationResponse.from(newReservation);
        return ResponseEntity.created(URI.create("/reservations/" + id)).body(response);
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (rs, rowNum) -> new Reservation(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDate("date").toLocalDate(),
            rs.getTime("time").toLocalTime()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
        @PathVariable Long id
    ) {
        getReservationById(id).orElseThrow(NotFoundReservationException::new);
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.noContent().build();
    }

    private Optional<Reservation> getReservationById(Long id) {
        String sql = "select * from reservation where id = ?";
        try {
            Reservation newReservation = jdbcTemplate.queryForObject(
                sql,
                getReservationRowMapper(),
                id
            );
            return Optional.ofNullable(newReservation);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
