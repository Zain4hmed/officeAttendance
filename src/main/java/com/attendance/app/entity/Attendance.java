package com.attendance.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private AttendanceType type;

    private LocalDate date;

    private String dayOfWeek; // New field for day of the week

    // Default constructor (required by JPA)
    public Attendance() {
    }

    // Parameterized constructor for convenient initialization
    public Attendance(User user, AttendanceType type, LocalDate date) {
        this.user = user;
        this.type = type;
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AttendanceType getType() {
        return type;
    }

    public void setType(AttendanceType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // toString method (optional, for better representation)
    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", user=" + user +
                ", type=" + type +
                ", date=" + date +
                ", day="+dayOfWeek+
                '}';
    }
}
