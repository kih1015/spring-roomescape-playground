package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Time;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Transactional
    public Time save(Time time) {
        Map<String, LocalTime> params = Map.of(
            "time", time.getTime()
        );
        return findById(jdbcInsert.executeAndReturnKey(params).longValue()).get();
    }

    @Transactional
    public void delete(Long id) {
        String sql = "delete from time where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
