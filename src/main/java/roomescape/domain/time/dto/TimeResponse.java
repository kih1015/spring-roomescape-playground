package roomescape.domain.time.dto;

import java.time.LocalTime;

import roomescape.domain.time.domain.Time;

public record TimeResponse(
    Long id,
    LocalTime time
) {

    public static TimeResponse from(Time time) {
        return new TimeResponse(time.getId(), time.getTime());
    }
}
