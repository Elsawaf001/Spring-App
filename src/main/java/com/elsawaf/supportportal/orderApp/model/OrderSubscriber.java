package com.elsawaf.supportportal.orderApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_subscriber")
@NoArgsConstructor
public class OrderSubscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_subscriber_seq")
    @SequenceGenerator(name = "order_subscriber_seq", sequenceName = "order_subscriber_seq"
            , allocationSize = 100 , initialValue = 100000)
    @Column(name = "id", nullable = false)
    private Long id;
@Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "sector")
    private String sector;

    @Column(name = "group_name")
    private String group;

    @Column(name = "area")
    private String area;

    @Column(name = "block")
    private String block;

    @Column(name = "state")
    private String state;

    @Column(name = "branch")
    private String branch ;

    @Column(name = "national_id")
    private String nationalId;
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "facility_name")
    private String facilityName;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "second_phone_number")
    private String secondPhoneNumber;

    public OrderSubscriber(String name ,
                           String address ,
                           String phoneNumber ,
                           String nationalId ,
                           String sector ,
                           String branch){
        this.name=name ;
        this.address = address;
        this.phoneNumber = phoneNumber ;
        this.nationalId= nationalId;
        this.sector = sector;
        this.branch = branch;
    }

    public OrderSubscriber(String name ,
                           String address ,
                           String sector ,
                           String group ,
                           String area ,
                           String block ,
                           String state ,
                           String branch ,
                           String nationalId ,
                           String phoneNumber ,
                           String facilityName ,
                           Double balance ,
                           String secondPhoneNumber){
        this.name = name;
        this.address = address;
        this.sector = sector;
        this.group = group ;
        this.area = area ;
        this.block = block;
        this.state = state ;
        this.branch = branch ;
        this.nationalId = nationalId ;
        this.phoneNumber = phoneNumber ;
        this.facilityName = facilityName ;
        this.balance = balance ;
        this.secondPhoneNumber = secondPhoneNumber ;
    }
}