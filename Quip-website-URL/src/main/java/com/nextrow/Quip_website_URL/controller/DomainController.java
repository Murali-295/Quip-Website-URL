package com.nextrow.Quip_website_URL.controller;

import com.nextrow.Quip_website_URL.service.DomainService;
import org.apache.commons.validator.routines.UrlValidator;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class DomainController {

    @Autowired
    private DomainService urlService;

    private final Logger logger= LoggerFactory.getLogger(DomainController.class);

    // create a domain
    @PostMapping("/quip/v2/createDomain")
    public ResponseEntity<String> createDomain(@RequestBody Map<String,String> domainData){

        //Validation
        logger.info("createDomain(): Validation of the given data is started.");
        UrlValidator urlValidator = new UrlValidator();

        if(domainData.get("title")==null || domainData.get("url")==null || !(urlValidator.isValid(domainData.get("url"))) ){
            logger.error("createDomain(): Data given is not valid.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("createDomain(): Validation of the given data is done.");

        String id=urlService.createDomain(domainData);

        if (id == null){
            logger.info("createDomain(): Domain creation is unsuccessful as document with Title or URL already exists.");
            return new ResponseEntity<>("Domain creation is unsuccessful as document with Title or URL already exists.Try using another values.",HttpStatus.BAD_REQUEST);
        }

        logger.info("createDomain(): Domain is created successfully, and user is returned with Id.");
        return new ResponseEntity<>("Domain created Successfully. Domain can be accessed by Id: "+id,HttpStatus.CREATED);
    }

    // fetch all the domains from the database
    @GetMapping("/quip/v2/getAllDomains")
    public ResponseEntity<Document> getAllDomains(){
        logger.info("getAllDomains(): Fetching all the domains for the database.");
        List<Document> data= urlService.getAllDomains();

        if ((data.isEmpty())){
            logger.warn("getAllDomains(): No domains in the database.");
            return new ResponseEntity<>(new Document("Message","No domains available in the collection. "),HttpStatus.NO_CONTENT);
        }

        logger.info("getAllDomains(): All the domains from the database are fetched and return to the user.");
        return ResponseEntity.ok(new Document("Domains",data));
    }

    // fetch a specific domain based on ID
    @GetMapping("/quip/v2/getDomainByID/{id}")
    public ResponseEntity<Document> getDomainByID(@PathVariable String id){

        // validation
        logger.info("getDomainByID(): Validation is initiated for the given Id.");
        if (id==null){
            logger.warn("getDomainByID(): Id is null.");
            return new ResponseEntity<>(new Document("Message","Failed, Id is null"), HttpStatus.NO_CONTENT);
        }

        if (id.length() != 24 && !(ObjectId.isValid(id))) {
            logger.warn("getDomainByID(): Given Id is Invalid.");
            return new ResponseEntity<>(new Document("Message","Failed. Invalid Id"), HttpStatus.BAD_REQUEST);
        }

        logger.info("getDomainByID(): Validation is done successfully.");

        Document domain=urlService.getDomainById(id);

        if(domain==null){
            logger.warn("getDomainByID(): No Domain found with the specified Id.");
            return new ResponseEntity<>(new Document("Message", "No Domain found with the specified Id."), HttpStatus.NOT_FOUND);

        }
        domain.remove("_id");
        logger.info("getDomainByID(): Domain found and return to user");
        return new ResponseEntity<>(domain, HttpStatus.OK);

    }

    // update a domain based on a specific ID
    @PutMapping("/quip/v2/updateDomain")
    public ResponseEntity<Document> updateDomain(@RequestBody Map<String,String> newDocument){
        // validation
        logger.info("updateDomain(): Validation of new document is started.(Update Domain)");
        UrlValidator urlValidator = new UrlValidator();

        String id=newDocument.get("_id");

        if (id==null){
            logger.warn("updateDomain(): Id is null.(Update Domain)");
            return new ResponseEntity<>(new Document("Message", "Failed. Id is null"), HttpStatus.NO_CONTENT);
        }

        if (id.length() != 24 && !(ObjectId.isValid(id))) {
            logger.warn("updateDomain(): Id is Invalid.(Update Domain)");
            return new ResponseEntity<>(new Document("Message","Failed. Invalid Id"), HttpStatus.BAD_REQUEST);
        }

        if (newDocument.containsKey("url")){
            String url=newDocument.get("url");
            if (url.isEmpty() || (!(urlValidator.isValid(newDocument.get("url"))))){
                logger.warn("updateDomain(): Given URL is Invalid.");
                return new ResponseEntity<>(new Document("Message","Failed, pass valid url"), HttpStatus.BAD_REQUEST);
            }
        }

        if (newDocument.containsKey("title")){
            String title=newDocument.get("title");
            if (title.isEmpty()){
                logger.warn("updateDomain(): Given Title is Invalid.");
                return new ResponseEntity<>(new Document("Message","Failed, pass valid Title"), HttpStatus.BAD_REQUEST);
            }
        }
        logger.info("updateDomain(): Validation is done successfully.");

        if(urlService.updateDomain(newDocument)){
            logger.info("updateDomain(): Domain is updated.");
            return new ResponseEntity<>(new Document("Message","Domain successfully updated!"), HttpStatus.OK);
        }
        logger.warn("updateDomain(): Domain couldn't get update as no domain found by given Id.");
        return new ResponseEntity<>(new Document("Message","Domain update failed. No domain found by given Id."), HttpStatus.EXPECTATION_FAILED);
    }

    // delete a domain based on a specific ID
    @DeleteMapping("/quip/v2/deleteDomain/{id}")
    public ResponseEntity<Document> deleteDomain(@PathVariable String id){
        // validation
        logger.info("deleteDomain(): Validation is initiated for the given Id.");
        if (id==null){
            logger.warn("deleteDomain(): Id is null.");
            return new ResponseEntity<>(new Document("Message", "Failed. Id is null"), HttpStatus.NO_CONTENT);
        }

        if (id.length() != 24 && !(ObjectId.isValid(id))) {
            logger.warn("deleteDomain(): Id is Invalid.");
            return new ResponseEntity<>(new Document("Message","Failed, Invalid Id"), HttpStatus.BAD_REQUEST);
        }

        logger.info("deleteDomain(): Validation is done Successfully.");

        if (urlService.deleteDomainById(id)){
            logger.info("deleteDomain(): domain is deleted");
            return new ResponseEntity<>(new Document("Message","Domain found and deleted successfully."), HttpStatus.OK);
        }
        logger.warn("deleteDomain(): Domain couldn't get deleted as no domain found by given Id.");
        return new ResponseEntity<>(new Document("Message","Domain not found with the specified Id."), HttpStatus.BAD_REQUEST);
    }

}