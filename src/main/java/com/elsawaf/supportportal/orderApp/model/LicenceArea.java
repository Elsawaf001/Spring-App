package com.elsawaf.supportportal.orderApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "licence_area")
@NoArgsConstructor
public class LicenceArea {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "licence_area_seq")
    @SequenceGenerator(name = "licence_area_seq" , sequenceName = "licence_area_seq"
    , allocationSize = 100 , initialValue = 500000)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String licenceAreaName;

    public LicenceArea(String licenceAreaName) {
        this.licenceAreaName = licenceAreaName;
    }
}