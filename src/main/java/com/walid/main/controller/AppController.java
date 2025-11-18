package com.walid.main.controller;

import com.walid.main.model.Feedback;
import com.walid.main.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class AppController {

    private final FeedbackService feedbackService;

    public AppController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping({"/", "/home"})
    public String homePage(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest httpServletRequest) {
        Pageable pageable = PageRequest.of(page, 3);

        Page<Feedback> feedbackPage = feedbackService.getFeedbacks(pageable);
        model.addAttribute("feedbacks", feedbackPage.getContent());
        model.addAttribute("totalPages", feedbackPage.getTotalPages());
        model.addAttribute("currentPage", page);

        if(httpServletRequest.isUserInRole("ADMIN")){
            model.addAttribute("isAdmin", true);
        }

        return "home-page";
    }

    @GetMapping("/search")
    public String searchFeedback(@RequestParam String searchQuery,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<Feedback> feedbackPage = feedbackService.searchFeedbackByName(searchQuery, pageable);

        model.addAttribute("feedbacks", feedbackPage.getContent());
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("totalPages", feedbackPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "home-page";
    }

    @GetMapping("/feedback/{feedbackId}")
    public String feedbackDetails(@PathVariable UUID feedbackId, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        model.addAttribute("feedback", feedback);
        return "feedback-page";
    }

    @PostMapping("/feedback/{feedbackId}/delete")
    public String deleteFeedback(@PathVariable UUID feedbackId, HttpServletRequest httpServletRequest) {
        feedbackService.deleteFeedbackById(feedbackId);

        if(httpServletRequest.isUserInRole("USER")){
            return "redirect:/user-dashboard";
        } else {
            return "redirect:/home";
        }
    }

    @PreAuthorize("hasRole('USER') and !hasRole('ADMIN')")
    @GetMapping("/feedback/{feedbackId}/edit")
    public String editFeedback(@PathVariable UUID feedbackId, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        model.addAttribute("feedback", feedback);
        model.addAttribute("mode", "edit");
        return "/post-page";
    }

    @PreAuthorize("hasRole('USER') and !hasRole('ADMIN')")
    @PostMapping("/feedback/{feedbackId}/edit")
    public String doEditFeedback(@PathVariable UUID feedbackId, @ModelAttribute Feedback feedback, HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getUserPrincipal().getName();
        LocalDateTime currentDateTime = LocalDateTime.now();
        feedback.setFeedbackId(feedbackId);
        feedback.setFeedbackBy(username);
        feedback.setCreatedAt(currentDateTime);
        feedbackService.saveFeedback(feedback);
        return "redirect:/user-dashboard";
    }

    @PreAuthorize("hasRole('USER') and !hasRole('ADMIN')")
    @GetMapping("/user-dashboard")
    public String userDashboardPage(@RequestParam(defaultValue = "0") int page,
                                    Model model,
                                    HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getUserPrincipal().getName();
        Pageable pageable = PageRequest.of(page, 3);
        Page<Feedback> feedbackPage = feedbackService.searchFeedbackByUser(username, pageable);

        model.addAttribute("feedbacks", feedbackPage.getContent());
        model.addAttribute("totalPages", feedbackPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "user-dashboard-page";
    }

    @PreAuthorize("hasRole('USER') and !hasRole('ADMIN')")
    @GetMapping("/feedback")
    public String postFeedbackPage(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("mode", "add");
        return "post-page";
    }

    @PreAuthorize("hasRole('USER') and !hasRole('ADMIN')")
    @PostMapping("/feedback")
    public String saveFeedback(@ModelAttribute Feedback feedback, HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getUserPrincipal().getName();
        LocalDateTime currentDateTime = LocalDateTime.now();
        feedback.setFeedbackBy(username);
        feedback.setCreatedAt(currentDateTime);
        feedbackService.saveFeedback(feedback);
        return "redirect:/user-dashboard";
    }
}
