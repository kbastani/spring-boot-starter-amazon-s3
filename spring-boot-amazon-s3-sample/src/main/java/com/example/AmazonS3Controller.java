package com.example;

import amazon.s3.AmazonS3Template;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/s3")
public class AmazonS3Controller {

    private AmazonS3Template amazonS3Template;
    private String bucketName;

    @Autowired
    public AmazonS3Controller(AmazonS3Template amazonS3Template, @Value("${amazon.s3.default-bucket}") String bucketName) {
        this.amazonS3Template = amazonS3Template;
        this.bucketName = bucketName;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Resource<S3ObjectSummary>> getBucketResources() {

        ObjectListing objectListing = amazonS3Template.getAmazonS3Client()
                .listObjects(new ListObjectsRequest()
                        .withBucketName(bucketName));

        return objectListing.getObjectSummaries()
                .stream()
                .map(a -> new Resource<>(a,
                        new Link(String.format("https://s3.amazonaws.com/%s/%s",
                                a.getBucketName(), a.getKey())).withRel("url")))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public
    @ResponseBody
    String handleFileUpload(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(file.getContentType());

                // Upload the file for public read
                amazonS3Template.getAmazonS3Client().putObject(new PutObjectRequest(bucketName, name, file.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
}
