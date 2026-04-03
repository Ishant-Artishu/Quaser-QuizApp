package com.Qauser.Quaser.Repository;

import com.Qauser.Quaser.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface QuizRepo extends JpaRepository<Quiz, Long> {
}
