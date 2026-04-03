package com.Qauser.Quaser.Service;

import com.Qauser.Quaser.Entity.Question;
import com.Qauser.Quaser.Repository.QuestionRepo;
import com.Qauser.Quaser.Repository.QuizRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Service
public class QuizService {

    private final QuestionRepo quizRepo;

    public QuizService(QuestionRepo quizRepo) {
        this.quizRepo = quizRepo;
    }

    public List<Question> generateQuestions(int requestedSize){

        int limit = 14;
        List<Question> allQuestions = quizRepo.findAll();
        Collections.shuffle(allQuestions);
        int size = Math.min(requestedSize, limit);
        size = Math.min(allQuestions.size(), size);
        return allQuestions.subList(0, size);
    }
}
