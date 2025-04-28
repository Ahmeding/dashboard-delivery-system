package com.hitecart.deliverysystem.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    private LocalDateTime start;
    private LocalDateTime end;


    public boolean overlapsWith(TimeSlot other) {
        return !this.start.isAfter(other.end) &&
                !this.end.isBefore(other.start);
    }
    public boolean covers(TimeSlot other) {
        return !this.start.isAfter(other.start) &&
                !this.end.isBefore(other.end);
    }

    public List<TimeSlot> subtract(TimeSlot other) {
        List<TimeSlot> result = new ArrayList<>();
        // No overlap: keep original
        if (this.end.isBefore(other.start) || this.start.isAfter(other.end)) {
            result.add(this);
            return result;
        }
        // Split into non-overlapping segments
        if (this.start.isBefore(other.start)) {
            result.add(new TimeSlot(this.start, other.start));
        }
        if (this.end.isAfter(other.end)) {
            result.add(new TimeSlot(other.end, this.end));
        }
        return result;
    }

    private boolean overlaps(TimeSlot other) {
        return !end.isBefore(other.start) && !start.isAfter(other.end);
    }


    @Override
    public String toString() {
        return start + " to " + end;
    }
}
