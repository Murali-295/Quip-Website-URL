package com.nextrow.Quip_website_URL.utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;

@Configuration
public class MongoUtils {

    @Autowired
    private MongoTemplate mongoTemplate;

    // getDatabase
    public MongoDatabase getDatabase(){
       return mongoTemplate.getDb();
    }

    // getCollection
    public MongoCollection<Document> getCollection(String collectionName){
        return getDatabase().getCollection(collectionName);
    }

    // createDocument
    public String createDomain(Document document,String collectionName) {

        try{
            mongoTemplate.insert(document,collectionName);
        }
        catch (Exception exception){
            return null;
        }

        return document.get("_id").toString();
    }

    // getAllDomains
    public List<Document> getAllDomains(Query query,String collectionName) {
        return mongoTemplate.find(query, Document.class, collectionName);
    }

    // getDomainById
    public Document getDomainById(String id, String collectionName) {
        return mongoTemplate.findById(new ObjectId(id),Document.class,collectionName);
    }

    // updateDocument
    public void updateDomain(Document document, Document updateDocument, String collectionName) {
        mongoTemplate.getCollection(collectionName).updateOne(document,updateDocument);
    }

    // deleteDomainById
    public boolean deleteDomainById(Document query, String collectionName) {
        DeleteResult result=mongoTemplate.getCollection(collectionName).deleteOne(query);
        return result.getDeletedCount() != 0;
    }

    //getDomainByData
    public Document getDomainByData(Query query,String collectionName) {
        return mongoTemplate.findOne(query, Document.class, collectionName);

    }

}
