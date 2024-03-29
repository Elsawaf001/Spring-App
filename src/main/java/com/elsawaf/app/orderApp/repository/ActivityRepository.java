package com.elsawaf.app.orderApp.repository;


import com.elsawaf.app.orderApp.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "activities")
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    long deleteByActivityNameIgnoreCase(String activityName);
    Activity findByActivityNameIgnoreCase(String activityName);


}