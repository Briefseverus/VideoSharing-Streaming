package com.VideoSharingStream.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.VideoSharingStream.dto.FirebaseVideoDTO;
import com.VideoSharingStream.services.VideoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private VideoService videoService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<String> uploadVideo(@RequestPart("file") MultipartFile file,
			@RequestPart("videoDTO") FirebaseVideoDTO videoDTO) throws IOException {
		return videoService.uploadVideo(file, videoDTO).flatMap(updatedMetadata -> sendMetadataToAppA(updatedMetadata));
	}

	private Mono<String> sendMetadataToAppA(FirebaseVideoDTO videoDTO) {
		return webClientBuilder.baseUrl("http://localhost:8080") 
				.build().post().uri("/api/videos").bodyValue(videoDTO).retrieve().bodyToMono(String.class); 
	}

	@GetMapping
	public Mono<String> getVideoUrl(@RequestParam("videoId") String videoId) {
		return videoService.getVideoUrl(videoId);
	}

	@GetMapping("/all")
	public Flux<String> getVideos() {
		return videoService.getVideos();
	}
}