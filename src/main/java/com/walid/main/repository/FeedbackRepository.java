package com.walid.main.repository;

import com.walid.main.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    Page<Feedback> findByFeedbackForContainingIgnoreCase(String feedbackFor, Pageable pageable);
    Page<Feedback> findByFeedbackBy(String feedbackBy, Pageable pageable);
    Feedback findFeedbackByFeedbackId(UUID feedbackId);
}