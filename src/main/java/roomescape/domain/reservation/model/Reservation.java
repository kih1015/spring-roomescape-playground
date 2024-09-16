package roomescape.domain.reservation.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import roomescape.domain.time.model.Time;

@Builder
@Getter
public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private Time time;
}
