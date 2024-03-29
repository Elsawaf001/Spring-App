package com.elsawaf.app.orderApp.repository;


import com.elsawaf.app.orderApp.model.LicenceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "licenceAreas")
public interface LicenceAreaRepository extends JpaRepository<LicenceArea, Long> {
    long deleteByLicenceAreaNameContainsIgnoreCase(String licenceAreaName);
    LicenceArea findByLicenceAreaNameContainsIgnoreCase(String licenceAreaName);


}