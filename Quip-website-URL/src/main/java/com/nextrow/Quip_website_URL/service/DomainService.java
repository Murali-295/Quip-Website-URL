package com.nextrow.Quip_website_URL.service;

import com.nextrow.Quip_website_URL.utils.ConstantUtils;
import com.nextrow.Quip_website_URL.utils.MongoUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Service
public class DomainService {

    @Autowired
    private MongoUtils mongoUtils;
    private final String collectionName= ConstantUtils.DOMAINS_COLL;

    public String createDomain(Map<String,String> domainData){
        Document document=new Document(domainData);
        return mongoUtils.createDomain(document,collectionName);
    }

    public List<Document> getAllDomains(){
        Query query = new Query();
        query.fields().exclude("_id");
        return mongoUtils.getAllDomains(query,collectionName);
    }

    public Document getDomainById(String id){
        return mongoUtils.getDomainById(id,collectionName);
    }

    public Boolean updateDomain(@RequestBody Map<String,String> newDocument){
        String id=newDocument.get("_id");
        if(id==null){
            return false;
        }
        if (id.length()!=24 && !(ObjectId.isValid(id))) {
            return false;
        }

        newDocument.remove("_id");
        Document document = new Document("_id",new ObjectId(id));
        Document updateDocument=new Document("$set",newDocument);
        mongoUtils.updateDomain(document,updateDocument,collectionName);
        return true;
    }

    public Boolean deleteDomainById(String id){
        Document query=new Document("_id", new ObjectId(id));
        return mongoUtils.deleteDomainById(query,collectionName);
    }

    /*public Document getDomainByData(Map<String,String> data) {
        Query query=new Query();
        if (data.containsKey("id")){
            query.addCriteria(Criteria.where("_id").is(data.get("id")));
        }
        else if (data.containsKey("title")) {
            query.addCriteria(Criteria.where("title").is(data.get("title")));
        }
        else {
            query.addCriteria(Criteria.where("url").is(data.get("url")));
        }
        query.fields().exclude("_id");
        return mongoUtils.getDomainByData(query,collectionName);
    }*/

}