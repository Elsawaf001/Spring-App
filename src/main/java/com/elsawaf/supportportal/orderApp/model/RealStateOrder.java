package com.elsawaf.supportportal.orderApp.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(name = "real_state_order")
public class RealStateOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "real_state_order_seq")
    @SequenceGenerator(name = "real_state_order_seq", sequenceName = "real_state_order_seq"
            , allocationSize = 100 , initialValue = 300000)
    @Column(name = "id", nullable = false)
    private Long id;

    private Double cost;
    @ManyToOne
    @JoinColumn(name = "order_subscriber_id" ,referencedColumnName = "id")
    private OrderSubscriber orderSubscriber;
    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;

    private String applicantName;
    private String applicantAddress;
    private String applicantPhoneNumber;
    private String applicantNationId;
    private String BusinessHistory;
    @CreationTimestamp
    private LocalDateTime applicationDate;

    private String representativeName;
    private String facilityDirectorName;
    private String notes;
@Builder
    public RealStateOrder(OrderSubscriber orderSubscriber, Activity activity, String applicantName, String applicantAddress, String applicantPhoneNumber, String applicantNationId, String businessHistory, LocalDateTime applicationDate, String representativeName, String facilityDirectorName, String notes) {
        this.orderSubscriber = orderSubscriber;
        this.activity = activity;
        this.applicantName = applicantName;
        this.applicantAddress = applicantAddress;
        this.applicantPhoneNumber = applicantPhoneNumber;
        this.applicantNationId = applicantNationId;
        BusinessHistory = businessHistory;
        this.applicationDate = applicationDate;
        this.representativeName = representativeName;
        this.facilityDirectorName = facilityDirectorName;
        this.notes = notes;
    }
}