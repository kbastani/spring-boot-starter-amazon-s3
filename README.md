# Spring Boot Starter Amazon S3

Example implementation of a Spring Boot starter project that auto-configures an Amazon S3 client.

Projects:

- Auto-Configuration
- Starter Project
- Sample Application

## Usage

The `spring-boot-amazon-s3-sample` is a Spring Boot application that allows users to upload files to an auto-configured Amazon S3 bucket.

To get started, replace the configuration for the `development` profile in the `spring-boot-amazon-s3-sample/src/main/resources/application.yml` file.

    spring:
      profiles: development
      application:
        name: s3-sample-app
    amazon:
      aws:
        access-key-id: replace
        access-key-secret: replace
      s3:
        default-bucket: replace

- `amazon.aws.access-key-id` is the AWS IAM user's access key id
- `amazon.aws.access-key-secret` is the AWS IAM user's access key secret
- `amazon.s3.default-bucket` is a default bucket name used for the `AmazonS3Template`

After replacing the configuration properties with valid IAM user credentials, start the application using `mvn spring-boot:run`. This will start a web application on [http://localhost:8080](), where you can upload files under 5 MB in size. By default, the bucket that you specified in the `application.yml` properties file will be used as the target bucket to upload files to Amazon S3.

After uploading files, you can navigate to [http://localhost:8080/s3]() to see a collection of S3 objects that have been uploaded to the default bucket.

    [
       {
          bucketName:"457f6a37-cbbc-4958-86dc-0821d01deb92",
          key:"profile-photo.jpg",
          size:148527,
          lastModified:1455963592000,
          storageClass:"STANDARD",
          owner:null,
          etag:"76e545dbfaa2b05754f21007d76a6f1a",
          links:[
             {
                rel:"url",
                href:"https://s3.amazonaws.com/457f6a37-cbbc-4958-86dc-0821d01deb92/profile-photo.jpg"
             }
          ]
       },
       {
          bucketName:"457f6a37-cbbc-4958-86dc-0821d01deb92",
          key:"twitter-discovery.png",
          size:398233,
          lastModified:1455963182000,
          storageClass:"STANDARD",
          owner:null,
          etag:"a51d7a07b7aa959f664e5f0b8d53a1ea",
          links:[
             {
                rel:"url",
                href:"https://s3.amazonaws.com/457f6a37-cbbc-4958-86dc-0821d01deb92/twitter-discovery.png"
             }
          ]
       }
    ]

## CF Service Broker Integration

The sample application in this project has been configured to bind to an Amazon S3 Service Broker instance on Cloud Foundry. To deploy this application to Cloud Foundry and use Amazon S3 as an auto-configured service binding, follow the directions in [Amazon S3 Service Broker](https://github.com/cloud-native-java/service-brokers/tree/master/s3-service-broker).