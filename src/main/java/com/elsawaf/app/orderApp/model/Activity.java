package com.elsawaf.app.orderApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_seq")
    @SequenceGenerator(name = "activity_seq", sequenceName = "activity_seq"
            , allocationSize = 100 , initialValue = 200000)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String activityName;
    @NotNull
    private String activityAddress;

    public Activity(String activityName, String activityAddress) {
        this.activityName = activityName;
        this.activityAddress = activityAddress;
    }
}