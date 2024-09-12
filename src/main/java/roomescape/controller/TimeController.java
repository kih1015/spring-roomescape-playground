package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.TimeCreateRequest;
import roomescape.controller.dto.TimeResponse;
import roomescape.domain.Time;
import roomescape.exception.NotFoundTimeException;

import javax.sql.DataSource;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public TimeController(
        JdbcTemplate jdbcTemplate,
        DataSource source
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
            .withTableName("time")
            .usingGeneratedKeyColumns("id");
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> getTimes() {
        String sql = "select * from time";
        List<TimeResponse> response = jdbcTemplate.query(sql, getTimeRowMapper()).stream()
            .map(TimeResponse::from)
            .toList();
        return ResponseEntity.ok(response);
    }

    private RowMapper<Time> getTimeRowMapper() {
        return (rs, rowNum) -> new Time(
            rs.getLong("id"),
            rs.getTime("time").toLocalTime()
        );
    }

    @PostMapping
    public ResponseEntity<TimeResponse> createTime(
        @Valid @RequestBody TimeCreateRequest request
    ) {
        Map<String, LocalTime> params = Map.of(
            "time", request.time()
        );
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        TimeResponse response = TimeResponse.from(
            getTimeById(id).orElseThrow(NotFoundTimeException::new)
        );
        return ResponseEntity.created(URI.create("/times/" + id)).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TimeResponse> deleteTime(
        @PathVariable Long id
    ) {
        getTimeById(id).orElseThrow(NotFoundTimeException::new);
        String sql = "delete from time where id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.noContent().build();
    }

    private Optional<Time> getTimeById(Long id) {
        String sql = "select * from time where id = ?";
        try {
            Time newTime = jdbcTemplate.queryForObject(
                sql,
                getTimeRowMapper(),
                id
            );
            return Optional.ofNullable(newTime);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
