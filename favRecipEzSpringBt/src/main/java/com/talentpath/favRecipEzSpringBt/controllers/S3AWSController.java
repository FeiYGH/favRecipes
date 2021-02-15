package com.talentpath.favRecipEzSpringBt.controllers;

//import com.talentpath.favRecipEzSpringBt.models.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//could also be called a BucketController class
//@RestController
//@RequestMapping("/storage")
public class S3AWSController {
//    private AmazonClient amazonClient;
//
//    @Autowired
//    S3AWSController(AmazonClient amazonClient) {
//        this.amazonClient = amazonClient;
//    }
//
//    @PostMapping("/uploadFile")
//    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
//        return this.amazonClient.uploadFile(file);
//    }
//
//    @DeleteMapping("/deleteFile")
//    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
//        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
//    }
}
