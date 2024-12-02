package com.service.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageResponse<T> {

    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean inLast;
    private List<T> content;


}

