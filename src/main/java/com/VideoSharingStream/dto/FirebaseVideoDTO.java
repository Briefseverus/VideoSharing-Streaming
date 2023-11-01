package com.VideoSharingStream.dto;

import java.util.Date;


import lombok.Data;

@Data
public class FirebaseVideoDTO {
	private String title;
	private String description;
	private String videoUrl;
	private String fileName;
	private Date uploadDate;
	private int duration;
	private String firebaseId;

}