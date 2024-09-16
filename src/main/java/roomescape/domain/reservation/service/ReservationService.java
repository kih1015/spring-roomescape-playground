package roomescape.domain.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.time.dao.TimeDao;
import roomescape.domain.reservation.model.Reservation;
import roomescape.domain.time.model.Time;
import roomescape.domain.reservation.exception.NotFoundReservationException;
import roomescape.domain.time.exception.NotFoundTimeException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDao reservationDao;
    private final TimeDao timeDao;

    public List<ReservationResponse> getReservations() {
        return reservationDao.findAll().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest reservationCreateRequest) {
        Time time = timeDao.findById(reservationCreateRequest.time())
            .orElseThrow(NotFoundTimeException::new);
        Reservation reservation = Reservation.builder()
            .name(reservationCreateRequest.name())
            .date(reservationCreateRequest.date())
            .time(time)
            .build();
        return ReservationResponse.from(reservationDao.save(reservation));
    }

    @Transactional
    public void deleteReservation(Long id) {
        if (reservationDao.findById(id).isEmpty()) {
            throw new NotFoundReservationException();
        }
        reservationDao.delete(id);
    }
}
