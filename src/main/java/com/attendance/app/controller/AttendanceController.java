package com.attendance.app.controller;

import com.attendance.app.entity.Attendance;
import com.attendance.app.entity.AttendanceType;
import com.attendance.app.entity.User;
import com.attendance.app.repository.AttendanceRepository;
import com.attendance.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class AttendanceController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    // Show the attendance form to the user (assumes the user object is already available in the session)
    @GetMapping("/attendance")
    public String showAttendanceForm(Model model) {
        // Get the logged-in user directly from the session or any other method
        User user = getLoggedInUser(); // Replace with your actual method to get the logged-in user

        model.addAttribute("user", user);  // Add user object to the model for use in the form
        return "attendance";  // Return to the attendance page
    }

    // Handle the form submission to store the attendance
    @PostMapping("/attendance")
    public String submitAttendance(@RequestParam String attendanceType, @RequestParam String userId, Model model) {
        System.out.println("Received attendance type: " + attendanceType);
        System.out.println("Received user ID: " + userId);

        // Find the user by the provided user ID
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            model.addAttribute("error", "User not found!");
            return "login";  // Redirect to login page if user is not found
        }

        // Check if the user already has an attendance record for today
        Attendance attendance = attendanceRepository.findByUserAndDate(user, LocalDate.now())
                .orElse(null);

        if (attendance == null) {
            // If there's no attendance record for today, create a new one
            attendance = new Attendance(user, AttendanceType.valueOf(attendanceType), LocalDate.now());
            attendanceRepository.save(attendance);
        } else {
            // If a record already exists, update the type (optional)
            attendance.setType(AttendanceType.valueOf(attendanceType));
            attendanceRepository.save(attendance);
        }

        // After submission, redirect to the login page
        return "redirect:/login";  // Redirect to login page
    }


    // This method should return the logged-in user. You can adapt it based on your logic.
    private User getLoggedInUser() {
        // Replace with your logic to retrieve the logged-in user.
        // This could be from the session, or based on other context management.
        // For this example, we assume the user ID is passed through the URL or session.
        return userRepository.findById("V1016111").orElse(null);  // Replace with real user lookup
    }
}
