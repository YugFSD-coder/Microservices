package com.service.video;

import com.service.video.Document.Video;
import com.service.video.entities.video;
import com.service.video.repositories.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VideoServiceApplication {
	@Autowired
	private VideoRepo videoRepo;



	public static void main(String[] args) {
		SpringApplication.run(VideoServiceApplication.class, args);

	}

//	@Override
//	public void run(String... args) throws Exception {
//		Video video1 = new Video();
//		video1.setTitle("Learin code ti dhbfjje");
//		video1.setPrice(100.00);
//		video1.setShortdesc("shott tikemdwe");
//
//		video videosv= videoRepo.save(video1);
//
//		System.out.println("video saved id  : "+videosv.getId() );
//
//	}
}
