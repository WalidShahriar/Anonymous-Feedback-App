package com.walid.main.service;

import com.walid.main.model.Feedback;
import com.walid.main.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public Page<Feedback> getFeedbacks(Pageable pageable) {
        Page<Feedback> feedbacks = feedbackRepository.findAll(pageable);
        feedbacks.forEach(feedback -> feedback.setMessage(truncateMessage(feedback.getMessage())));
        return feedbacks;
    }

    public Page<Feedback> searchFeedbackByName(String feedbackFor, Pageable pageable) {
        Page<Feedback> feedbacks = feedbackRepository.findByFeedbackForContainingIgnoreCase(feedbackFor, pageable);
        feedbacks.forEach(feedback -> feedback.setMessage(truncateMessage(feedback.getMessage())));
        return feedbacks;
    }

    public Page<Feedback> searchFeedbackByUser(String feedbackBy, Pageable pageable) {
        Page<Feedback> feedbacks = feedbackRepository.findByFeedbackBy(feedbackBy, pageable);
        feedbacks.forEach(feedback -> feedback.setMessage(truncateMessage(feedback.getMessage())));
        return feedbacks;
    }

    public Feedback getFeedbackById(UUID feedbackId) {
        return feedbackRepository.findFeedbackByFeedbackId(feedbackId);
    }

    public void saveFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    public void deleteFeedbackById(UUID feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }

    private String truncateMessage(String message) {
        if (message != null && message.length() > 200) {
            return message.substring(0, 200) + " …";
        } else {
            return message;
        }
    }
}
