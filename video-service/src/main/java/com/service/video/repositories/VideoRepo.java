package com.service.video.repositories;

import com.service.video.Document.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Arrays;
import java.util.List;

public interface VideoRepo extends MongoRepository<Video,String> {


    List<Video> indByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(String keyword);

    List<Video> findByCourseId(String courseId);
}
