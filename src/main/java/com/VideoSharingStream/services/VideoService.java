package com.VideoSharingStream.services;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.VideoSharingStream.dto.FirebaseVideoDTO;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VideoService {

    @Autowired
    private Bucket storageClient;

    public Mono<FirebaseVideoDTO> uploadVideo(MultipartFile file, FirebaseVideoDTO metadata) {
        return Mono.fromCallable(() -> {
            String videoId = UUID.randomUUID().toString();
            Blob blob = storageClient.create(videoId, file.getInputStream(), file.getContentType());
            String videoUrl = blob.signUrl(1, TimeUnit.HOURS).toString();
            metadata.setFirebaseId(videoId);
            metadata.setVideoUrl(videoUrl);
            return metadata;
        });
    }


    public Mono<String> getVideoUrl(String videoId) {
        return Mono.fromCallable(() -> {
            String downloadUrl = storageClient.get(videoId).signUrl(1, TimeUnit.HOURS).toString();
            return downloadUrl;
        });
    }

    public Flux<String> getVideos() {
        return Flux.fromIterable(storageClient.list().iterateAll()).map(blob -> {
            String downloadUrl = blob.signUrl(1, TimeUnit.HOURS).toString();
            return downloadUrl;
        });
    }
}
