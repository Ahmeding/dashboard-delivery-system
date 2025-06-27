package com.hitecart.deliverysystem.dto.driverstats;

import jakarta.validation.constraints.PositiveOrZero;

public record DriverStatsRequest(
         Integer id,
         @PositiveOrZero(message = "Long trips count cannot be negative")
         int longTrips,
         @PositiveOrZero(message = "Standard  trips count cannot be negative")
         int standardTrips,
         Integer driverId
) {
}
