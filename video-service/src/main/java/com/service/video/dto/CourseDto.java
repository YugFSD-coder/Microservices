package com.service.video.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
public class CourseDto {
    private String id;

    private String title;

    private String shortDesc;

    //    @JsonIgnore
    @JsonProperty("long_description")
    private String longDesc;

    private double price;

    private boolean live = false;

    private double discount;

    private Date createdDate;

    // add your fields

    private String banner;


    public String getBannerUrl() {
        return "http://localhost:9092/api/v1/courses/" + id + "/banners";

    }

    private String categoryId;
}
