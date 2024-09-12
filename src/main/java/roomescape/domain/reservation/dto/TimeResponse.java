package roomescape.domain.reservation.dto;

import java.time.LocalTime;

import roomescape.domain.reservation.domain.Time;

public record TimeResponse(
    Long id,
    LocalTime time
) {

    static public TimeResponse from(Time time) {
        return new TimeResponse(time.getId(), time.getTime());
    }
}
