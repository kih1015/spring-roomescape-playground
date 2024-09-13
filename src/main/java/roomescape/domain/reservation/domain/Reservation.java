package roomescape.domain.reservation.domain;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private Time time;
}
