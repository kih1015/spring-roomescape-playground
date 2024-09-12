package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.Time;
import roomescape.exception.NotFoundReservationException;
import roomescape.exception.NotFoundTimeException;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
