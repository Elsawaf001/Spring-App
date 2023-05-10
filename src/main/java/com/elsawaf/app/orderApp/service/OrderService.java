package com.elsawaf.app.orderApp.service;

import com.elsawaf.app.orderApp.model.*;
import com.elsawaf.app.orderApp.repository.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
private final ActivityRepository activityRepository;
private final IndustrialOrderRepository industrialOrderRepository ;
private final RealStateOrderRepository realStateOrderRepository;
private final LicenceAreaRepository licenceAreaRepository ;
private final OrderSubscriberRepository orderSubscriberRepository;




public Activity addActivity(String activityName , String activityAddress){
    Activity activity = new Activity(activityName,activityAddress);
    activityRepository.save(activity);
    return activity;
}

public List<Activity> findAllActivity(){
    return activityRepository.findAll();
}

public Activity updateActivity(String currentActivityName , String newName,String newAddress){
    Activity activity = activityRepository.findByActivityNameIgnoreCase(currentActivityName);
    activity.setActivityName(newName);
    activity.setActivityAddress(newAddress);
    return activity;
}
public void deleteActivity(String activityName){
    Activity activity = activityRepository.findByActivityNameIgnoreCase(activityName);
    activityRepository.deleteByActivityNameIgnoreCase(activityName);
}

public List<LicenceArea> findAllLicenceAreas(){
    return licenceAreaRepository.findAll();
}

public LicenceArea findLicenceArea(String name){
    return licenceAreaRepository.findByLicenceAreaNameContainsIgnoreCase(name);
}

public void deleteLicenceArea(String name){
    licenceAreaRepository.deleteByLicenceAreaNameContainsIgnoreCase(name);
}

public LicenceArea addLicenceArea(String licenceAreaName){
LicenceArea licenceArea = new LicenceArea(licenceAreaName);
    licenceAreaRepository.save(licenceArea);
    return licenceArea;
}

public OrderSubscriber createNewSubscriber(@NotNull String name ,
                                           String address ,
                                           String sector ,
                                           String group ,
                                           String area ,
                                           String block ,
                                           @NotNull String state ,
                                           String branch ,
                                           String nationalId ,
                                           @NotNull String phoneNumber ,
                                           String facilityName ,
                                           Double balance ,
                                           String secondPhoneNumber){
    OrderSubscriber orderSubscriber = new OrderSubscriber();
    orderSubscriber.setName(name);
    if (address== null){
        orderSubscriber.setAddress("العنوان غير محدد ");
    }else {orderSubscriber.setAddress(address);}

    if ( sector == null) {
        orderSubscriber.setSector("القطاع غير محدد");
    }else {orderSubscriber.setSector(sector);}

    if (group== null ) {
        orderSubscriber.setGroup("المجموعة غير معرفة");
    }else {orderSubscriber.setGroup(group);}

    if (area== null) {
        orderSubscriber.setArea("القطاع غير محدد");
    } else {orderSubscriber.setArea(area);}

    if (block== null) {
        orderSubscriber.setBlock("رقم البلوك غير معرف");
    }else{orderSubscriber.setBlock(block);}

    if (branch== null) {
        orderSubscriber.setBranch("الفرع غير محدد");
    } else {orderSubscriber.setBranch(branch);}

    if (nationalId== null) {
        orderSubscriber.setNationalId("بطاقة الرقم القومى غير معرفة");
    } else {orderSubscriber.setNationalId(nationalId);}

    if (facilityName== null) {
        orderSubscriber.setFacilityName("اسم المنشأة غير معرف");
    } else {orderSubscriber.setFacilityName(facilityName);}

    if (balance==null) {
        orderSubscriber.setBalance(0d);
    } else {orderSubscriber.setBalance(balance);}

    if (secondPhoneNumber== null) {
        orderSubscriber.setSecondPhoneNumber("لايوجد ارقام اخرى معرفة");
    } else {orderSubscriber.setSecondPhoneNumber(secondPhoneNumber);}

    orderSubscriber.setState(state);
    orderSubscriber.setPhoneNumber(phoneNumber);
    orderSubscriberRepository.save(orderSubscriber);
    return orderSubscriber;

}

public OrderSubscriber findSubscriberByName(String name){
    return orderSubscriberRepository.findByNameContainsIgnoreCase(name);
}


public void deleteSubscriber(String name){
    orderSubscriberRepository.deleteByNameIgnoreCase(name);
}

public void addBalanceToSubscriber(Double balance , String subscriberName){
    OrderSubscriber orderSubscriber= orderSubscriberRepository.findByNameContainsIgnoreCase(subscriberName);
    orderSubscriber.setBalance(balance);
    orderSubscriberRepository.save(orderSubscriber);
}

public IndustrialOrder createIndustrialOrder(Long subscriberId , Long activityTypeId , Long licenceAreaId ,
                                             String representativeName , String facilityDirectorName ,
                                             String notes , Double cost){
    Optional<OrderSubscriber> orderSubscriber = orderSubscriberRepository.findById(subscriberId);
    Optional<Activity> activity = activityRepository.findById(activityTypeId);
    Optional<LicenceArea> licenceArea = licenceAreaRepository.findById(licenceAreaId);
    IndustrialOrder industrialOrder = new IndustrialOrder();
    if (orderSubscriber.isPresent()){
        industrialOrder.setOrderSubscriber(orderSubscriber.get());
    }
    if (activity.isPresent()){
        industrialOrder.setActivity(activity.get());
    }
    if (licenceArea.isPresent()){
        industrialOrder.setLicenceArea(licenceArea.get());
    }
    if (representativeName == null){
       industrialOrder.setRepresentativeName("لم يتم تسجيل اسم المندوب");
    } else {industrialOrder.setRepresentativeName(representativeName);}

    if (facilityDirectorName == null){
        industrialOrder.setFacilityDirectorName("لم يتم تسجيل اسم مدير المنشأة");
    } else {industrialOrder.setFacilityDirectorName(facilityDirectorName);}

    if (notes== null){
        industrialOrder.setNotes("لا توجد ملاحظات");
    } {industrialOrder.setNotes(notes);}

    if (cost==null){
        industrialOrder.setCost(0d);
    }else {industrialOrder.setCost(cost);}

    industrialOrderRepository.save(industrialOrder);
    return industrialOrder;

}

    public RealStateOrder createRealStateOrder(Long subscriberId , Long activityTypeId ,
                                                String applicantName, String applicantAddress,
                                                String applicantPhoneNumber, String applicantNationId,
                                                String businessHistory,
                                                String representativeName, String facilityDirectorName,
                                                String notes){
        Optional<OrderSubscriber> orderSubscriber = orderSubscriberRepository.findById(subscriberId);
        Optional<Activity> activity = activityRepository.findById(activityTypeId);
        RealStateOrder realStateOrder = new RealStateOrder();

        orderSubscriber.ifPresent(realStateOrder::setOrderSubscriber);
        activity.ifPresent(realStateOrder::setActivity);

        if (applicantName== null){
            realStateOrder.setApplicantName("لم يتم تسجيل اسم مقدم الطلب");
        } else {realStateOrder.setApplicantName(applicantName);}


        if (applicantPhoneNumber== null){
            realStateOrder.setApplicantPhoneNumber("لم يتم تسجيل رقم هاتف مقدم الطلب");
        } else {realStateOrder.setApplicantPhoneNumber(applicantPhoneNumber);}


        if (applicantAddress== null){
            realStateOrder.setApplicantAddress("لم يتم تسجيل عنوان مقدم الطلب");
        } else {realStateOrder.setApplicantAddress(applicantAddress);}


        if (applicantNationId== null){
            realStateOrder.setApplicantNationId("لم يتم تسجيل رقم بطاقة مقدم الطلب");
        } else {realStateOrder.setApplicantNationId(applicantNationId);}


        if ( businessHistory== null){
            realStateOrder.setBusinessHistory("لا توجد سابقة اعمال مسجلة");
        } else {realStateOrder.setBusinessHistory(businessHistory);}



       realStateOrder.setApplicationDate(new Date());


        if ( representativeName== null){
            realStateOrder.setRepresentativeName("لم يتم تسجيل اسم المندوب");
        } else {realStateOrder.setRepresentativeName(representativeName);}



        if ( facilityDirectorName== null){
            realStateOrder.setFacilityDirectorName("لم يتم تسجيل اسم مدير المنشأة");
        } else {realStateOrder.setFacilityDirectorName(facilityDirectorName);}


        if (notes== null){
            realStateOrder.setNotes("لا توجد ملاحظات");
        } else {realStateOrder.setNotes(notes);}


        realStateOrderRepository.save(realStateOrder);
        return realStateOrder;
    }

}
