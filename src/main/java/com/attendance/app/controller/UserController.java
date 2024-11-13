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
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

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
//    @PostMapping("/login")
//    public String loginUser(String id, String password, Model model) {
//        User user = userRepository.findById(id).orElse(null);
//        System.out.println("user : "+user.toString());
//        System.out.println("user status : ");
//        List<Attendance> list = user.getAttendanceRecords();
//        for(Attendance attendance : list){
//            System.out.println(attendance.toString());
//        }
//        if (user != null && user.getPassword().equals(password)) {
//            model.addAttribute("user", user);
//            System.out.println("user : "+user.toString());
//            return "attendance";  // Redirect to attendance page after successful login
//        } else {
//            model.addAttribute("error", "Invalid credentials");
//            return "login";  // Return to login if credentials are invalid
//        }
//    }

//    @PostMapping("/login")
//    public String loginUser(String id, String password, Model model) {
//        User user = userRepository.findById(id).orElse(null);
//        if (user != null && user.getPassword().equals(password)) {
//            // Add user data and their attendance records to the model
//            model.addAttribute("user", user);
//            model.addAttribute("attendanceRecords", user.getAttendanceRecords()); // Add attendance data
//            return "attendance";  // Redirect to the attendance page
//        } else {
//            model.addAttribute("error", "Invalid credentials");
//            return "login";  // Return to login if credentials are invalid
//        }
//    }

    @PostMapping("/login")
    public String loginUser(String id, String password, Model model) {
        User user = userRepository.findById(id).orElse(null);
        int leaveCounter = 0;
        int compOffCounter = 0;
        if (user != null && user.getPassword().equals(password)) {
            // Add user data and their attendance records to the model
            model.addAttribute("user", user);

            // Calculate the day of the week for each attendance record
            List<Attendance> attendanceRecords = user.getAttendanceRecords();
            for (Attendance attendance : attendanceRecords) {
                // Assume attendance.getDate() returns a LocalDate or String
                // If attendance.getDate() returns a String, we need to parse it into LocalDate
                String dateString = attendance.getDate().toString(); // attendance.getDate() should return a LocalDate or a String in the format "yyyy-MM-dd"
                LocalDate date = LocalDate.parse(dateString);  // Parse the string to a LocalDate
                String dayOfWeek = date.getDayOfWeek().toString();  // Get the day of the week (e.g., MONDAY, TUESDAY, etc.)

                System.out.println("type : "+attendance.getType());
                if(attendance.getType().toString().equals("LEAVE")){
                    leaveCounter++;
                }
                if(attendance.getType().toString().equals("COMP_OFF")){
                    compOffCounter++;
                }

//                System.out.println("leaveCount : "+leaveCounter+" , compoff : "+compOffCounter);
                attendance.setDayOfWeek(dayOfWeek); // Add a field to your Attendance class if necessary
//                System.out.println(attendance.toString());
            }

            model.addAttribute("attendanceRecords", attendanceRecords); // Add attendance data
            model.addAttribute("leaveCounter",leaveCounter);
            model.addAttribute("compOffCounter",compOffCounter);
            return "attendance";  // Redirect to the attendance page
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";  // Return to login if credentials are invalid
        }
    }



    // Utility method to convert date to day of the week
    private String getDayOfWeek(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

}
