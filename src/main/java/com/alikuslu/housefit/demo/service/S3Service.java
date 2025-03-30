package com.alikuslu.housefit.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String directory) throws IOException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        String key = directory + "/" + filename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(new PutObjectRequest(
                bucketName,
                key,
                file.getInputStream(),
                metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3.getUrl(bucketName, key).toString();
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            String key = fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);

            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            System.err.println("Error deleting file from S3: " + e.getMessage());
        }
    }

    public String updateAvatar(String oldAvatarUrl, MultipartFile newAvatarFile) throws IOException {
        if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty() && oldAvatarUrl.contains(bucketName)) {
            deleteFile(oldAvatarUrl);
        }

        return uploadFile(newAvatarFile, "avatars");
    }
}
