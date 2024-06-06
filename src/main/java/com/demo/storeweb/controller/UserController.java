package com.demo.storeweb.controller;

import com.demo.storeweb.dto.UserRegistrationDTO;
import com.demo.storeweb.model.User;
import com.demo.storeweb.dto.UserLoginDTO;
import com.demo.storeweb.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        model.addAttribute("categories", List.of("Laptop", "PC")); // Example method to get categories
        return "register";
        
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDTO userRegistrationDTO,
                               BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", userService.getCategories());
            return "register";
        }
        userService.registerUser(userRegistrationDTO);
        return "redirect:/login";

    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserLoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("user") UserLoginDTO userLoginDTO,
                            BindingResult bindingResult, Model model, HttpSession session, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        if(userService.findByUsernameAndPassword(userLoginDTO.getUsername(), userLoginDTO.getPassword())) {
            session = request.getSession();
            session.setAttribute("loggedInUser", userService.findByUsername(userLoginDTO.getUsername()));
            return "redirect:/products";
        } else {
            model.addAttribute("loginError", "Sai tài khoản hoặc mật khẩu");
            return "login";
        }

//        User user = userService.findByUsernameAndPassword(userLoginDTO.getUsername(), userLoginDTO.getPassword());
//        if (user == null) {
//            model.addAttribute("loginError", "Invalid username or password");
//            return "login";
//        }
//
//        session.setAttribute("loggedInUser", user);
//        return "redirect:/products";
    }

    @PostMapping("/logout")
    public String logoutUser(HttpSession session, HttpServletRequest request) {
        session = request.getSession();
        session.invalidate();
        return "redirect:/login";
    }
    
    
}

