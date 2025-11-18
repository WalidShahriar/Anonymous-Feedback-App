package com.walid.main.controller;

import com.walid.main.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam @Nullable String error, @RequestParam @Nullable String logout, RedirectAttributes redirectAttributes) {
        if (error != null && error.equals("true")) {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        } else if (logout != null && logout.equals("true")) {
            redirectAttributes.addFlashAttribute("message", "Logged out successfully");
            return "redirect:/login";
        }

        return "login-page";
    }

    @GetMapping("/register")
    public String registrationPage() {
        return "registration-page";
    }

    @PostMapping("/register")
    public String doRegisterUser(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(username, password);
            redirectAttributes.addFlashAttribute("message", "Registration Successful!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/register";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.isUserInRole("ADMIN")) {
            return "redirect:/";
        } else if (httpServletRequest.isUserInRole("USER")) {
            return "redirect:/user-dashboard";
        }

        return "redirect:/";
    }
}

