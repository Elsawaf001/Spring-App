package com.elsawaf.app.orderApp.repository;


import com.elsawaf.app.orderApp.model.RealStateOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "realStateOrders")
public interface RealStateOrderRepository extends JpaRepository<RealStateOrder, Long> {



}