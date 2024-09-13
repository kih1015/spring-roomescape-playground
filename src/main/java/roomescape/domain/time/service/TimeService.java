package roomescape.domain.time.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import roomescape.domain.time.dto.TimeCreateRequest;
import roomescape.domain.time.dto.TimeResponse;
import roomescape.domain.time.dao.TimeDao;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.exception.NotFoundTimeException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeService {

    private final TimeDao timeDao;

    public List<TimeResponse> getTimes() {
        return timeDao.findAll().stream()
            .map(TimeResponse::from)
            .toList();
    }

    @Transactional
    public TimeResponse createTime(TimeCreateRequest timeCreateRequest) {
        Time time = Time.builder()
            .time(timeCreateRequest.time())
            .build();
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
