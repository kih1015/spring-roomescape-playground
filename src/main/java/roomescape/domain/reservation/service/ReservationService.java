package roomescape.domain.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.reservation.dao.TimeDao;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.Time;
import roomescape.domain.reservation.exception.NotFoundReservationException;
import roomescape.domain.reservation.exception.NotFoundTimeException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final TimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public List<ReservationResponse> getReservations() {
        return reservationDao.findAll().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest reservationCreateRequest) {
        Time time = timeDao.findById(reservationCreateRequest.time())
            .orElseThrow(NotFoundTimeException::new);
        Reservation reservation = new Reservation(
            null,
            reservationCreateRequest.name(),
            reservationCreateRequest.date(),
            time
        );
        return ReservationResponse.from(reservationDao.save(reservation));
    }

    @Transactional
    public void cancelReservation(Long id) {
        if (reservationDao.findById(id).isEmpty()) {
            throw new NotFoundReservationException();
        }
        reservationDao.delete(id);
    }
}
