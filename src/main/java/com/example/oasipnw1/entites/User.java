package com.example.oasipnw1.entites;

import com.example.oasipnw1.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.student;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @Column(name = "createdOn",nullable = false)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private ZonedDateTime createdOn;
//
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "updatedOn",nullable = false , updatable = false)
//    private ZonedDateTime updatedOn;

    @Column(name = "createdOn",insertable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updatedOn",insertable = false,updatable = false)
    private ZonedDateTime updatedOn;
}