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
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle user registration (manual method using @RequestParam)
    @PostMapping("/register")
    public String registerUser(@RequestParam String id, @RequestParam String firstName,
                               @RequestParam String lastName, @RequestParam String phoneNumber,
                               @RequestParam String team, @RequestParam String password) {

        // Create and save user
        User newUser = new User(id, firstName, lastName, phoneNumber, team, password);
        userRepository.save(newUser);

        // No attendance record is created here anymore

        return "redirect:/login"; // Redirect to login page after registration
    }


    // Show login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Handle user login
    @PostMapping("/login")
    public String loginUser(String id, String password, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            model.addAttribute("user", user);
            System.out.println("user : "+user.toString());
            return "attendance";  // Redirect to attendance page after successful login
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";  // Return to login if credentials are invalid
        }
    }
}
