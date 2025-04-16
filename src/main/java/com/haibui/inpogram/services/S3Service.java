package com.haibui.inpogram.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class S3Service {
    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value("${s3.path.featured-images}")
    private String featuredImagesPath;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = file.getOriginalFilename() + "_" + System.currentTimeMillis();
        String filePathName = featuredImagesPath + fileName;
        log.info("Uploading file to S3: bucket={}, key={}", bucketName, filePathName);

        s3Client.putObject(new PutObjectRequest(bucketName, filePathName, fileObj));
        fileObj.delete();
        return fileName;
    }

    @Async
    public S3ObjectInputStream findFileByName(String fileName) {
        log.info("Downloading file with name {}", fileName);
        return s3Client.getObject(bucketName, fileName).getObjectContent();
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
