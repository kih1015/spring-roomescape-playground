package roomescape.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(
    @NotNull
    Long id,

    @NotBlank
    String name,

    @NotNull
    LocalDate date,

    @NotNull
    InnerTime time
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getName(),
            reservation.getDate(),
            new InnerTime(
                reservation.getTime().getId(),
                reservation.getTime().getTime()
            )
        );
    }

    record InnerTime(
        Long id,
        LocalTime time
    ) {
    }
}
