package com.service.course.exception;


public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(){
        super("Resource Not Found !!");
    }

    public ResourceNotFoundException(String courseNotFound){
        super(courseNotFound);
    }
}
