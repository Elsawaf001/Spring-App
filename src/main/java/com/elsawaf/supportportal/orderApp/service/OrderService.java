package com.elsawaf.supportportal.orderApp.service;

import com.elsawaf.supportportal.orderApp.model.*;
import com.elsawaf.supportportal.orderApp.repository.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    if (address.isBlank() || address.isEmpty()){
        orderSubscriber.setAddress("العنوان غير محدد ");
    }
    orderSubscriber.setAddress(address);
    if (sector.isEmpty() || sector.isEmpty()) {
        orderSubscriber.setSector("القطاع غير محدد");
    }
    orderSubscriber.setSector(sector);
    if (group.isEmpty() || group.isEmpty()) {
        orderSubscriber.setGroup("المجموعة غير معرفة");
    }
    orderSubscriber.setGroup(group);
    if (area.isEmpty() || area.isEmpty()) {
        orderSubscriber.setArea("القطاع غير محدد");
    }
    orderSubscriber.setArea(area);
    if (block.isEmpty() || block.isEmpty()) {
        orderSubscriber.setBlock("رقم البلوك غير معرف");
    }
    orderSubscriber.setBlock(block);
    if (branch.isEmpty() || branch.isEmpty()) {
        orderSubscriber.setBranch("الفرع غير محدد");
    }
    orderSubscriber.setBranch(branch);
    if (nationalId.isEmpty() || nationalId.isEmpty()) {
        orderSubscriber.setNationalId("بطاقة الرقم القومى غير معرفة");
    }
    orderSubscriber.setNationalId(nationalId);
    if (facilityName.isEmpty() || facilityName.isEmpty()) {
        orderSubscriber.setFacilityName("اسم المنشأة غير معرف");
    }
    orderSubscriber.setFacilityName(facilityName);
    if (balance.isNaN() || balance.isInfinite()) {
        orderSubscriber.setBalance(0d);
    }
    orderSubscriber.setBalance(balance);
    if (secondPhoneNumber.isEmpty() || secondPhoneNumber.isEmpty()) {
        orderSubscriber.setSecondPhoneNumber("لايوجد ارقام اخرى معرفة");
    }
    orderSubscriber.setSecondPhoneNumber(secondPhoneNumber);
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
    if (representativeName.isEmpty() || representativeName.isBlank()){
       industrialOrder.setRepresentativeName("لم يتم تسجيل اسم المندوب");
    }
    industrialOrder.setRepresentativeName(representativeName);
    if (facilityDirectorName.isBlank() || facilityDirectorName.isEmpty()){
        industrialOrder.setFacilityDirectorName("لم يتم تسجيل اسم مدير المنشأة");
    }
    industrialOrder.setFacilityDirectorName(facilityDirectorName);
    if (notes.isBlank() || notes.isEmpty()){
        industrialOrder.setNotes("لا توجد ملاحظات");
    }
    industrialOrder.setNotes(notes);
    if (cost.isNaN()){
        industrialOrder.setCost(0d);
    }
    industrialOrder.setCost(cost);
    industrialOrderRepository.save(industrialOrder);
    return industrialOrder;

}

    public RealStateOrder createIndustrialOrder(Long subscriberId , Long activityTypeId ,
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

        if (applicantName.isEmpty() || applicantName.isBlank()){
            realStateOrder.setApplicantName("لم يتم تسجيل اسم مقدم الطلب");
        }
        realStateOrder.setApplicantName(applicantName);

        if (applicantPhoneNumber.isEmpty() || applicantPhoneNumber.isBlank()){
            realStateOrder.setApplicantPhoneNumber("لم يتم تسجيل رقم هاتف مقدم الطلب");
        }
        realStateOrder.setApplicantPhoneNumber(applicantPhoneNumber);

        if (applicantAddress.isEmpty() || applicantAddress.isBlank()){
            realStateOrder.setApplicantAddress("لم يتم تسجيل عنوان مقدم الطلب");
        }
        realStateOrder.setApplicantAddress(applicantAddress);

        if (applicantNationId.isEmpty() || applicantNationId.isBlank()){
            realStateOrder.setApplicantNationId("لم يتم تسجيل رقم بطاقة مقدم الطلب");
        }
        realStateOrder.setApplicantNationId(applicantNationId);

        if (businessHistory.isEmpty() || businessHistory.isBlank()){
            realStateOrder.setBusinessHistory("لا توجد سابقة اعمال مسجلة");
        }
        realStateOrder.setBusinessHistory(businessHistory);


       realStateOrder.setApplicationDate(new Date());


        if (representativeName.isEmpty() || representativeName.isBlank()){
            realStateOrder.setRepresentativeName("لم يتم تسجيل اسم المندوب");
        }
        realStateOrder.setRepresentativeName(representativeName);


        if (facilityDirectorName.isEmpty() || facilityDirectorName.isBlank()){
            realStateOrder.setFacilityDirectorName("لم يتم تسجيل اسم مدير المنشأة");
        }
        realStateOrder.setFacilityDirectorName(facilityDirectorName);

        if (notes.isEmpty() || notes.isBlank()){
            realStateOrder.setNotes("لا توجد ملاحظات");
        }
        realStateOrder.setNotes(notes);

        realStateOrderRepository.save(realStateOrder);
        return realStateOrder;
    }

}
