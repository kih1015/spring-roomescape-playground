package roomescape.domain.reservation.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record TimeCreateRequest(
    @NotNull
    LocalTime time
) {
}
