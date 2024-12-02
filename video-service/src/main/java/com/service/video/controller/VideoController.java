package com.service.video.controller;

import com.service.video.dto.CourseDto;
import com.service.video.dto.CustomMessage;
import com.service.video.dto.VideoDto;
import com.service.video.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping
    public ResponseEntity<VideoDto> createVideo(@RequestBody VideoDto videoDto){

     //   return ResponseEntity.status(HttpStatus.CREATED).body(videoService.createVideo(videoDto));
        return new ResponseEntity<>(videoService.createVideo(videoDto),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoDto> updateVideo(@PathVariable String id, @RequestBody VideoDto videoDto) {
        return ResponseEntity.ok(videoService.updateVideo(id, videoDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> getVideoById(@PathVariable String id) {
        return ResponseEntity.ok(videoService.getVideoById(id));
    }

    @GetMapping
    public ResponseEntity<Page<VideoDto>> getAllVideos(Pageable pageable) {
        return ResponseEntity.ok(videoService.getAllVideos(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomMessage> deleteVideo(@PathVariable String id){
        videoService.deleteVideo(id);
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("Video is deleted");
        customMessage.setSuccess(true);
        return ResponseEntity.status(HttpStatus.OK).body(customMessage);
    }
    @GetMapping("/search")
    public ResponseEntity<List<VideoDto>> searchVideos(@RequestParam String keyword) {
        return ResponseEntity.ok(videoService.searchVideos(keyword));
    }
    @GetMapping("course/{courseId}")
    public ResponseEntity<VideoDto> getAllVideosOfCourse(@PathVariable String courseId){
        return ResponseEntity.status(HttpStatus.OK).body((VideoDto)videoService.getVideoOfCourse(courseId));
    }

}
