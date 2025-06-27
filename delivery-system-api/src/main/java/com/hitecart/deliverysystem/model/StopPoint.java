package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table(name = "stop_point")
@SuperBuilder
@ToString(callSuper=true)
public class StopPoint extends AbstractEntity{

    @Column(name = "title")
    private String title;
    @Column(name = "comment")
    private String comment;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime scheduledDeliveryTime ;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime actualDeliveryTime;
    @Column(name = "departure_date")
    private LocalDate departureDate;
    @Column(name = "arrival_date")
    private LocalDate arrivalDate;
    @Column(name = "departure_location")
    private String departureLocation;
    @Column(name = "arrival_location")
    private String arrivalLocation;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;


}
