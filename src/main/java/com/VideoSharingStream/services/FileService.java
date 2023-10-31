package com.VideoSharingStream.services;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;

@Service

public class FileService {

    private final Bucket storageClient;

    public FileService(Bucket storageClient) {
        this.storageClient = storageClient;
    }

    public Boolean upload(MultipartFile multipartFile, String contentType) {
        try {
            String fileName = UUID.randomUUID().toString().concat(getExtension(multipartFile.getOriginalFilename()));
            File file = convertToFile(multipartFile, fileName);
            uploadFile(file, fileName, contentType);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private void uploadFile(File file, String fileName, String contentType) {
        try{
            storageClient.create(fileName, Files.readAllBytes(file.toPath()), contentType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String download(String fileName) {
        try {
            Blob blob = storageClient.getStorage().get(BlobId.of(storageClient.getName(),fileName));
            blob.downloadTo(Paths.get(blob.getName()));
            return blob.getName();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String getContentType(MultipartFile multipartFile) {
        return multipartFile.getContentType();
    }

}
