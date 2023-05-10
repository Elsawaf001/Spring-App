package com.elsawaf.app.orderApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(name = "industrial_order")
public class IndustrialOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "industrial_order_seq")
    @SequenceGenerator(name = "industrial_order_seq" ,  sequenceName = "industrial_order_seq"
            , allocationSize = 100 , initialValue = 400000)
    @Column(name = "id", nullable = false)
    private Long id;

    private Double cost;
    @ManyToOne
    @JoinColumn(name = "order_subscriber_id" ,referencedColumnName = "id")
    private OrderSubscriber orderSubscriber;
    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "licence_area_id", referencedColumnName = "id")
    private LicenceArea licenceArea;

    private String representativeName;
    private String facilityDirectorName;
    private String notes;

    public IndustrialOrder(OrderSubscriber orderSubscriber, Activity activity, LicenceArea licenceArea, String representativeName, String facilityDirectorName, String notes) {
        this.orderSubscriber = orderSubscriber;
        this.activity = activity;
        this.licenceArea = licenceArea;
        this.representativeName = representativeName;
        this.facilityDirectorName = facilityDirectorName;
        this.notes = notes;
    }
}