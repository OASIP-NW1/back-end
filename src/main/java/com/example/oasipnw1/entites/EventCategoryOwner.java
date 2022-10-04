//package com.example.oasipnw1.entites;
//
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "EventCategoryOwner")
//public class EventCategoryOwner {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ownerID", nullable = false)
//    private Integer id;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "eventCategoryID", nullable = false)
//    private EventCategory eventCategoryID;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "userID", nullable = false)
//    private User userID;
//
//    public User getUserID() {
//        return userID;
//    }
//
//    public void setUserID(User userID) {
//        this.userID = userID;
//    }
//
//    public EventCategory getEventCategoryID() {
//        return eventCategoryID;
//    }
//
//    public void setEventCategoryID(EventCategory eventCategoryID) {
//        this.eventCategoryID = eventCategoryID;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//}