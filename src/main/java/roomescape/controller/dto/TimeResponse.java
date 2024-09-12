package roomescape.controller.dto;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.Time;

import java.time.LocalTime;

public record TimeResponse(
    Long id,
    LocalTime time
) {

    static public TimeResponse from(Time time) {
        return new TimeResponse(time.getId(), time.getTime());
    }
}
