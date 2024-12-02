package com.service.category.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {

    @Id
    private String id;

    @Column(nullable = true)
    private String title;

    @Column(name = "description")
    private String desc;

    private Date addedDate;

    @Column(name = "bannerUrl")
    private String bannerUrl;

}
