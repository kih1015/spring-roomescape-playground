package roomescape.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import roomescape.domain.Reservation;
import roomescape.domain.Time;

@Repository
@Transactional(readOnly = true)
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
            .withTableName("reservation")
            .usingGeneratedKeyColumns("id");
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("name"),
            rs.getDate("date").toLocalDate(),
            new Time(
                rs.getLong("time_id"),
                rs.getTime("time_value").toLocalTime()
            )
        );
    }

    public List<Reservation> findAll() {
        String sql = """
            SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.time as time_value
            FROM reservation as r inner join time as t on r.time_id = t.id
            """;
        return jdbcTemplate.query(sql, getReservationRowMapper());
    }

    public Optional<Reservation> findById(Long id) {
        String sql = """
            SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.time as time_value
            FROM reservation as r inner join time as t on r.time_id = t.id
            WHERE r.id = ?
            """;
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

    @Transactional
    public Reservation save(Reservation reservation) {
        Map<String, Object> params = Map.of(
            "name", reservation.getName(),
            "date", reservation.getDate(),
            "time_id", reservation.getTime().getId()
        );
        return findById(jdbcInsert.executeAndReturnKey(params).longValue()).get();
    }

    @Transactional
    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
