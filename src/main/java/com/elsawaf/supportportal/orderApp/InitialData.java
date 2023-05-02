package com.elsawaf.supportportal.orderApp;

import com.elsawaf.supportportal.orderApp.model.*;
import com.elsawaf.supportportal.orderApp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialData implements CommandLineRunner {
    public static  Activity activity1 = new Activity("مخابز" , "القاهرة");
    public static Activity activity2 = new Activity("مطاعم" , "القاهرة");
    public static Activity activity3 = new Activity("بوفيه" , "القاهرة");
    public static Activity activity4 = new Activity("عقارات" , "القاهرة");
    public static LicenceArea l1 = new LicenceArea("منطقة تراخيص القاهرة");
    public static LicenceArea l2 = new LicenceArea("منطقة تراخيص الاسكندرية");
    public static LicenceArea l3 = new LicenceArea("منطقة تراخيص المنوفية");
    public static OrderSubscriber s1 = new OrderSubscriber("أحمد ولاء عبد الرازق محمد","شارع ميدان عرابى المنشية",
            "01552809409","1425-59874-156247","قطاع شمال القاهرة" , "فرع مصر الجديدة");
    public static OrderSubscriber s2 = new OrderSubscriber("اسلام محمد سعيد","شارع ميدان عرابى المنشية",
            "01052809409","1425-59874-156247","قطاع شمال القاهرة" , "فرع مصر الجديدة");
    public static OrderSubscriber s3 = new OrderSubscriber("عصام محمد الخزلى","شارع قصر التبين الجمرك",
            "01252809009","1425-59874-156247","قطاع شمال القاهرة" , "فرع مصر الجديدة");
    public static OrderSubscriber s4 = new OrderSubscriber("عماد حسن العزازى ","شارع ميدان عرابى المنشية",
            "01152809409","1425-59874-156247","قطاع شمال القاهرة" , "فرع مصر الجديدة");
    public static OrderSubscriber s5 = new OrderSubscriber("ابراهيم ابراهيم رجب","شارع صفر - الجمرك",
            "01002800009","1425-59874-156247","قطاع شمال القاهرة" , "فرع مصر الجديدة");

    private final ActivityRepository activityRepository;

    private final LicenceAreaRepository licenceAreaRepository;

    private final OrderSubscriberRepository orderSubscriberRepository;

    private final IndustrialOrderRepository industrialOrderRepository;

    private final RealStateOrderRepository realStateOrderRepository;
    private void activityData(ActivityRepository activityRepository){
        final List<Activity> entities = Arrays.asList(activity1, activity2, activity3, activity4);
        activityRepository.saveAll(entities);

    }
    private void licenceAreaData(LicenceAreaRepository licenceAreaRepository){

        final List<LicenceArea> licenceAreas = Arrays.asList(l1,l2,l3);
        licenceAreaRepository.saveAll(licenceAreas);
    }

    private void orderSubscriberData(OrderSubscriberRepository orderSubscriberRepository){

        final List<OrderSubscriber> orderSubscribers = Arrays.asList(s1,s2,s3,s4,s5);
        orderSubscriberRepository.saveAll(orderSubscribers);
    }

    private void industrialOrderData(IndustrialOrderRepository industrialOrderRepository , OrderSubscriberRepository orderSubscriberRepository, LicenceAreaRepository licenceAreaRepository , ActivityRepository activityRepository){

        IndustrialOrder industrialOrder = new IndustrialOrder(s1 , activity1 , l1
                ,  "احمد مصطفى الصواف" , "ahmed Elsawaf " , "هلاحظات 1");
        industrialOrderRepository.save(industrialOrder);


    }

    private void realStateOrderData(RealStateOrderRepository realStateOrderRepository , OrderSubscriberRepository orderSubscriberRepository , ActivityRepository activityRepository){

        RealStateOrder realStateOrder = RealStateOrder.builder().orderSubscriber(s1).activity(activity3).
                applicantName("اسلام محمد احمد").applicantAddress("شارع المعز").applicantNationId("123456-9874-1236")
                .applicationDate(LocalDateTime.now()).applicantPhoneNumber("01552849647").businessHistory("سابقة اعمال واحدة")
                .notes("ملاحظات هنا").representativeName("اسلام احمد محمود").facilityDirectorName("محمد مصطفى الصواف").build();
        realStateOrderRepository.save(realStateOrder);


    }
    @Override
    public void run(String... args) throws Exception {
        activityData(activityRepository);
        licenceAreaData(licenceAreaRepository);
        orderSubscriberData(orderSubscriberRepository);
        industrialOrderData(industrialOrderRepository,orderSubscriberRepository,licenceAreaRepository,activityRepository);
        realStateOrderData(realStateOrderRepository,orderSubscriberRepository,activityRepository);
    }
}
