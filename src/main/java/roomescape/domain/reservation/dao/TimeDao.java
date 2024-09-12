package roomescape.domain.reservation.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import roomescape.domain.reservation.domain.Time;

@Repository
@Transactional(readOnly = true)
public class TimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("time")
            .usingGeneratedKeyColumns("id");
    }

    private RowMapper<Time> getTimeRowMapper() {
        return (rs, rowNum) -> new Time(
            rs.getLong("id"),
            rs.getTime("time").toLocalTime()
        );
    }

    public List<Time> findAll() {
        String sql = "select * from time";
        return jdbcTemplate.query(sql, getTimeRowMapper());
    }

    public Optional<Time> findById(Long id) {
        String sql = "select * from time where id = ?";
        Time newTime = jdbcTemplate.queryForObject(
            sql,
            getTimeRowMapper(),
            id
        );
        return Optional.ofNullable(newTime);
    }

    public Time getById(Long id) {
        String sql = "select * from time where id = ?";
        return jdbcTemplate.queryForObject(
            sql,
            getTimeRowMapper(),
            id
        );
    }

    @Transactional
    public Time save(Time time) {
        Map<String, LocalTime> params = Map.of(
            "time", time.getTime()
        );
        return getById(jdbcInsert.executeAndReturnKey(params).longValue());
    }

    @Transactional
    public void delete(Long id) {
        String sql = "delete from time where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
