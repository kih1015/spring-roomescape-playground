package roomescape.domain.time.domain;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Time {

    private Long id;
    private LocalTime time;
}
