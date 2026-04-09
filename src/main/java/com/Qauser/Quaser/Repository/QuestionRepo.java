package com.Qauser.Quaser.Repository;

import com.Qauser.Quaser.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long> {
}
