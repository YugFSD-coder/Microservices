package com.service.course.services;

import com.service.course.config.AppConstants;
import com.service.course.dto.CategoryDto;
import com.service.course.dto.CourseDto;

import com.service.course.dto.ResourceContentType;
import com.service.course.dto.VideoDto;
import com.service.course.entities.Course;
import com.service.course.exception.ResourceNotFoundException;
import com.service.course.repositories.CourseRepo;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService{
    private CourseRepo courseRepository;

    private ModelMapper modelMapper;

    private FileService fileService;

    private RestTemplate restTemplate;

    private WebClient webClient;

    public CourseServiceImpl(WebClient webClient,CourseRepo courseRepository, ModelMapper modelMapper,
                             FileService fileService, RestTemplate restTemplate) {
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {

        courseDto.setId(UUID.randomUUID().toString());
        courseDto.setCreatedDate(new Date());

        Course course = modelMapper.map(courseDto, Course.class);

        Course savedCourse = courseRepository.save(course);
        return modelMapper.map(savedCourse, CourseDto.class);
    }
    @Override
    public CourseDto updateCourse(String id, CourseDto courseDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        modelMapper.map(courseDto, course);
        Course updatedCourse = courseRepository.save(course);
        return modelMapper.map(updatedCourse, CourseDto.class);
    }

    @Override
    public CourseDto getCourseById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        //get category details of course
        CourseDto courseDto = modelMapper.map(course, CourseDto.class);
   //     CategoryDto forObject = restTemplate.getForObject(AppConstants.BASE_URL_CATEGORY+"/categories" + course.getCategoryId(), CategoryDto.class);
        courseDto.setCategoryDto(getCategoryOfCourse(course.getCategoryId()));
        List<VideoDto> videoDto = webClient.get()
                .uri(AppConstants.VIDEO_SERVICE_BASE_URL + "/videos/course/{courseId}", course.getId())
                .retrieve()
                .bodyToFlux(VideoDto.class)
                .collect(Collectors.toList())
                .block();
        courseDto.setVideoDtoList(videoDto);
        return courseDto;
    }

    @Override
    public Page<CourseDto> getAllCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);
        //api call to get category
//        List<CourseDto> dtos = courses.getContent()
//                .stream().map(course ->restTemplate
//                        .getForObject("http://localhost:9092/api/v1/categories"
//                                + course.getCategoryId(), CategoryDto.class))
//                .map(course -> modelMapper.map(course, CourseDto.class))
//                .collect(Collectors.toList());
//    })
        List<CourseDto> dtos = courses.getContent()
                    .stream().map(course -> {
                    CourseDto courseDto = modelMapper.map(course, CourseDto.class);
                    courseDto.setCategoryDto(getCategoryOfCourse(course.getCategoryId()));
                    courseDto.setVideoDtoList(getVideoOfCourse(course.getId()));
                    return courseDto;
                })
                    .collect(Collectors.toList());


        return new PageImpl<>(dtos, pageable, courses.getTotalElements());
    }

    @Override
    public void deleteCourse(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("not found, Cannot be deleted !!"));

        courseRepository.deleteById(id);

    }

    @Override
    public List<CourseDto> searchCourses(String keyword) {
        List<Course> courses = courseRepository.findByTitleContainingIgnoreCaseOrShortDescContainingIgnoreCase(keyword, keyword);
        List<CourseDto> collectDto = courses.stream()
                .map(course -> {
                    CourseDto courseDto = modelMapper.map(course, CourseDto.class);
                    courseDto.setCategoryDto(getCategoryOfCourse(course.getCategoryId()));
                    courseDto.setVideoDtoList(getVideoOfCourse(course.getId()));
                    return courseDto;
                        }
                )
                .collect(Collectors.toList());

        return collectDto;
    }

    @Override
    public CourseDto saveBanner(MultipartFile file, String courseId) throws IOException {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!!"));
        String filePath = fileService.save(file, AppConstants.COURSE_BANNER_UPLOAD_DIR, file.getOriginalFilename());
        course.setBanner(filePath);
        course.setBannerContentType(file.getContentType());
        return modelMapper.map( courseRepository.save(course),CourseDto.class);
    }

    @Override
    public ResourceContentType getCourseBannerById(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found!!"));
        String bannerPath = course.getBanner();
        Path path = Paths.get(bannerPath);
        Resource resource = new FileSystemResource(path);
        ResourceContentType resourceContentType = new ResourceContentType();
        resourceContentType.setResource(resource);
        resourceContentType.setContentType(course.getBannerContentType());
        return resourceContentType;
    }

    public CategoryDto getCategoryOfCourse(String categoryId){
        try{
            ResponseEntity<CategoryDto> exchange = restTemplate.exchange
                    (AppConstants.BASE_URL_CATEGORY+ "/categories/" + categoryId, HttpMethod.GET, null, CategoryDto.class);
            HttpEntity<CategoryDto> categoryDtoHttpEntity = new HttpEntity<>(new CategoryDto());
            return exchange.getBody();
        }
        catch (HttpClientErrorException ex) {
//            ex.printStackTrace();
            return null;
        }
    }

    //call vedio service of the course
    public List<VideoDto> getVideoOfCourse(String courseId) {
        List<VideoDto> videoDtolist = webClient.get()
                .uri(AppConstants.VIDEO_SERVICE_BASE_URL + "/videos/course/{courseId}", courseId)
                .retrieve()
                .bodyToFlux(VideoDto.class)
                .collectList()
                .block();
        return videoDtolist;
    }

}
