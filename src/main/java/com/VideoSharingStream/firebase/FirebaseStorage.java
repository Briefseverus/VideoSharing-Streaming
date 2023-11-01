package com.VideoSharingStream.firebase;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;

import com.google.cloud.storage.Bucket;

import com.google.firebase.FirebaseApp;

import com.google.firebase.FirebaseOptions;

import com.google.firebase.cloud.StorageClient;

@Configuration

public class FirebaseStorage {

	@Value("classpath:videosharing-9c5f9-firebase-adminsdk-sqzec-fa7e828c6d.json")

	Resource serviceAccount;

	private final static String bucketName = "videosharing-9c5f9.appspot.com";

	@Bean
	public Bucket storageClient() throws IOException {

		System.out.println("Initializing Firebase storage...");

		// Khởi tạo Firebase
		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
				.setStorageBucket(bucketName).build();

		FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);

		System.out.println("Firebase initialized");

		// Lấy Bucket từ StorageClient
		Bucket bucket = StorageClient.getInstance(firebaseApp).bucket();

		System.out.println("Storage client initialized");

		return bucket;

	}
}