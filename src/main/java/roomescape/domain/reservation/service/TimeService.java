package roomescape.domain.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.domain.reservation.dto.TimeCreateRequest;
import roomescape.domain.reservation.dto.TimeResponse;
import roomescape.domain.reservation.dao.TimeDao;
import roomescape.domain.reservation.domain.Time;
import roomescape.domain.reservation.exception.NotFoundTimeException;

@Service
@Transactional(readOnly = true)
public class TimeService {

    private final TimeDao timeDao;

    public TimeService(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<TimeResponse> getTimes() {
        return timeDao.findAll().stream()
            .map(TimeResponse::from)
            .toList();
    }

    @Transactional
    public TimeResponse createTime(TimeCreateRequest timeCreateRequest) {
        Time time = new Time(
            null,
            timeCreateRequest.time()
        );
        return TimeResponse.from(timeDao.save(time));
    }

    @Transactional
    public void deleteTime(Long id) {
        if (timeDao.findById(id).isEmpty()) {
            throw new NotFoundTimeException();
        }
        timeDao.delete(id);
    }
}
