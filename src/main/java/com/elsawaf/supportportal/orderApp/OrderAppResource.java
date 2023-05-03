package com.elsawaf.supportportal.orderApp;

import com.elsawaf.supportportal.domain.HttpResponse;
import com.elsawaf.supportportal.exception.domain.UserNotFoundException;
import com.elsawaf.supportportal.orderApp.model.*;
import com.elsawaf.supportportal.orderApp.repository.*;
import com.elsawaf.supportportal.orderApp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/app")
public class OrderAppResource {
    private final OrderSubscriberRepository orderSubscriberRepository;
    private final ActivityRepository activityRepository;
    private final OrderService orderService;

    @GetMapping(path = "/activities")
    public ResponseEntity<List<Activity>> listAllActivities(){
        List<Activity> activities = orderService.findAllActivity();
        return new  ResponseEntity<>(activities, HttpStatus.OK);
    }

    @GetMapping(path = "/activity")
    public ResponseEntity<Activity> getActivity(@RequestParam(value = "id") Long id) throws UserNotFoundException {
        Optional<Activity> activity = activityRepository.findById(id);
        if (activity.isPresent()) {
            return new ResponseEntity<>(activity.get() , HttpStatus.OK);
        }
        else {
            throw new UserNotFoundException("لا يوجد نشاط بهذا الاسم");
        }
    }

    @PostMapping(path = "/createActivity")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<Activity> createActivity(@RequestParam(value = "address" , required = false) String address,
                                                   @RequestParam(value = "name") String name){
        if (address ==null){
            Activity activity1 = new Activity();
            activity1.setActivityName(name);
            activityRepository.save(activity1);
            return new ResponseEntity<>(activity1,HttpStatus.OK);
        }
        else {
        Activity activity = orderService.addActivity(name , address);
        activityRepository.save(activity);
        return new ResponseEntity<>(activity , HttpStatus.OK); }
    }



    @PostMapping(path = "/updateActivity")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<Activity> updateActivity(@RequestParam(value = "currentActivityName") String currentActivityName ,
                                                   @RequestParam(value = "newName")  String newName,
                                                   @RequestParam(value = "newAddress") String newAddress){
    Activity activity = orderService.updateActivity(currentActivityName,newName,newAddress);
            return new ResponseEntity<>(activity , HttpStatus.OK); }




    @DeleteMapping(path = "/deleteActivity")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public HttpResponse deleteActivity(@RequestParam(value = "name") String activityName){
        orderService.deleteActivity(activityName);
        return new HttpResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT ,
                HttpStatus.NO_CONTENT.getReasonPhrase().toUpperCase(),"تم حذف النشاط" );
    }



    @GetMapping(path = "licences")
    public ResponseEntity<List<LicenceArea>> getAll(){
        return new ResponseEntity<>(orderService.findAllLicenceAreas(),HttpStatus.OK);
    }



    @PostMapping(path = "licence")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<LicenceArea> addLicenceArea(@RequestParam(value = "name") String name){
        LicenceArea licenceArea = orderService.addLicenceArea(name);
        return new ResponseEntity<>(licenceArea , HttpStatus.OK);
    }



    @GetMapping(path = "findLicence")
    public ResponseEntity<LicenceArea> findLicenceArea(@RequestParam(value = "name") String name){
        LicenceArea licenceArea = orderService.findLicenceArea(name);
        return new ResponseEntity<>(licenceArea , HttpStatus.OK);
    }



    @DeleteMapping(path = "/deleteLicence")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public HttpResponse deleteLicenceArea(@RequestParam(value = "name") String name){
        orderService.deleteLicenceArea(name);
        return new HttpResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT ,
                HttpStatus.NO_CONTENT.getReasonPhrase().toUpperCase(),"تم حذف منطقة التراخيص" );
    }


    @GetMapping(path = "subscribers")
    public ResponseEntity<List<OrderSubscriber>> findAllSubscribers(){
        return new ResponseEntity<>(orderSubscriberRepository.findAll(),HttpStatus.OK);
    }


    @PostMapping(path = "/subscriber")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<OrderSubscriber> createOrderSubscriber(@RequestParam(value = "name") String name ,
                                                                 @RequestParam(value = "address" , required = false) String address ,
                                                                 @RequestParam(value = "sector" , required = false)  String sector ,
                                                                 @RequestParam(value = "group" , required = false) String group ,
                                                                 @RequestParam(value = "area" , required = false) String area ,
                                                                 @RequestParam(value = "block" , required = false) String block ,
                                                                 @RequestParam(value = "state") String state ,
                                                                 @RequestParam(value = "branch" , required = false) String branch ,
                                                                 @RequestParam(value = "nationalId" , required = false) String nationalId ,
                                                                 @RequestParam(value = "phoneNumber") String phoneNumber ,
                                                                 @RequestParam(value = "facilityName" , required = false) String facilityName ,
                                                                 @RequestParam(value = "balance" , required = false) Double balance ,
                                                                 @RequestParam(value = "secondPhoneNumber" , required = false) String secondPhoneNumber)
    {

        OrderSubscriber subscriber = orderService.createNewSubscriber(name,address,
                sector,group,area,block,state,branch,nationalId,phoneNumber,facilityName,balance,secondPhoneNumber);
        return new ResponseEntity<>(subscriber,HttpStatus.OK);
    }

    @GetMapping(path = "/findSubscriber")
    public ResponseEntity<OrderSubscriber> findSubscriber(@RequestParam(value = "name") String name){
        OrderSubscriber orderSubscriber = orderService.findSubscriberByName(name);
        return new ResponseEntity<>(orderSubscriber,HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteSubscriber")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public HttpResponse deleteSubscriber(@RequestParam(value = "name") String name){
        orderService.deleteSubscriber(name);
        return new HttpResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT ,
                HttpStatus.NO_CONTENT.getReasonPhrase(),"تم حذف المشترك" );
    }

    @PostMapping(path = "/subscriberBalance")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public HttpResponse addBalance(@RequestParam(value = "balance") Double balance ,
                                                      @RequestParam(value = "subscriberName") String subscriberName){
       orderService.addBalanceToSubscriber(balance,subscriberName);
        return new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK ,
                HttpStatus.OK.getReasonPhrase() , "تم اضافة الرصيد بنجاح");
    }

    @PostMapping(path = "industrial")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<IndustrialOrder> createIndustrialOrder(@RequestParam(value = "subscriberId") Long subscriberId ,
                                                                 @RequestParam(value = "activityTypeId") Long activityTypeId ,
                                                                 @RequestParam(value = "licenceAreaId")  Long licenceAreaId ,
                                                                 @RequestParam(value = "representativeName" , required = false) String representativeName ,
                                                                 @RequestParam(value = "facilityDirectorName" , required = false) String facilityDirectorName ,
                                                                 @RequestParam(value = "notes" , required = false) String notes ,
                                                                 @RequestParam(value = "cost", required = false) Double cost ){
        IndustrialOrder industrialOrder = orderService.createIndustrialOrder(subscriberId ,
                activityTypeId , licenceAreaId , representativeName , facilityDirectorName ,notes , cost);

        return new ResponseEntity<>(industrialOrder, HttpStatus.OK);
    }


    @PostMapping(path = "realState")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<RealStateOrder> createRealStateOrder(@RequestParam(value = "subscriberId") Long subscriberId ,
                                                               @RequestParam(value = "activityTypeId") Long activityTypeId ,
                                                               @RequestParam(value = "applicantName") String applicantName,
                                                               @RequestParam(value = "applicantAddress") String applicantAddress,
                                                               @RequestParam(value = "applicantPhoneNumber", required = false) String applicantPhoneNumber,
                                                               @RequestParam(value = "applicantNationId", required = false) String applicantNationId,
                                                               @RequestParam(value = "businessHistory", required = false) String businessHistory,
                                                               @RequestParam(value = "representativeName", required = false) String representativeName,
                                                               @RequestParam(value = "facilityDirectorName", required = false) String facilityDirectorName,
                                                               @RequestParam(value = "notes", required = false) String notes){


    RealStateOrder realStateOrder = orderService.createRealStateOrder(subscriberId ,
            activityTypeId , applicantName , applicantAddress, applicantPhoneNumber
    , applicantNationId , businessHistory , representativeName , facilityDirectorName , notes);

    return new ResponseEntity<>(realStateOrder , HttpStatus.OK);}
}
