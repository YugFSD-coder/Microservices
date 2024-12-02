package com.service.video.services;

import com.service.video.Document.Video;
import com.service.video.dto.VideoDto;
import com.service.video.exception.ResourceNotFoundException;
import com.service.video.repositories.VideoRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService{

    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CourseService courseService;

    @Override
    public VideoDto createVideo(VideoDto videoDto) {
        String uuid  = UUID.randomUUID().toString();
        videoDto.setId(uuid);
        Video video = modelMapper.map(videoDto,Video.class);
        Video save = videoRepo.save(video);
        return modelMapper.map(save,VideoDto.class);
    }


    @Override
    public VideoDto updateVideo(String videoId, VideoDto videoDto) {
        Video video = videoRepo.findById(videoId).orElseThrow(()-> new ResourceNotFoundException("Vedio not found !"));
        video.setTitle(videoDto.getTitle());
        video.setDesc(videoDto.getDesc());
        video.setFilePath(videoDto.getFilePath());
        video.setContentType(videoDto.getContentType());
        video.setCourseId(videoDto.getCourseId());
        videoRepo.save(video);
        return modelMapper.map(video,VideoDto.class);
    }

    @Override
    public VideoDto getVideoById(String videoId) {
        Video video = videoRepo.findById(videoId).orElseThrow(()-> new ResourceNotFoundException("Video Not found !!!"));
        VideoDto videoDto = modelMapper.map(video,VideoDto.class);
        videoDto.setCourseDto(courseService.getCourseById(videoDto.getCourseId()));
        return videoDto;
    }

    @Override
    public Page<VideoDto> getAllVideos(Pageable pageable) {
       Page<Video> allVideo = videoRepo.findAll(pageable);
       List<VideoDto> videoDtos = allVideo.stream()
               .map(video -> {
                   return  modelMapper.map(allVideo, VideoDto.class);
               }).toList();
       return new PageImpl<>(videoDtos,pageable,allVideo.getTotalElements());
    }

    @Override
    public void deleteVideo(String videoId) {
        Video video = videoRepo.findById(videoId).orElseThrow(()-> new ResourceNotFoundException("video not found !"));
        videoRepo.deleteById(videoId);
    }

    @Override
    public List<VideoDto> searchVideos(String keyword) {
        List<Video> video = videoRepo.indByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(keyword);
        List<VideoDto> videoDtos = video.stream().map(vid -> modelMapper.map(vid, VideoDto.class)).collect(Collectors.toList());
        return videoDtos;
    }

    @Override
    public VideoDto saveVideoFile(MultipartFile file, String videoId) {
        return null;
    }

    @Override
    public List<VideoDto> getVideoOfCourse(String courseId) {
        return videoRepo.findByCourseId(courseId).stream().map(video-> modelMapper.map(video,VideoDto.class)).collect(Collectors.toList());
    }

}
