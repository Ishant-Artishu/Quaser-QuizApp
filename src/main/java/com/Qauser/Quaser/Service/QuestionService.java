package com.Qauser.Quaser.Service;

import com.Qauser.Quaser.Entity.Question;
import com.Qauser.Quaser.Repository.QuestionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepo questionRepo;

    public QuestionService(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    public List<Question> getAllQuestions(){
        return questionRepo.findAll();
    }

    public Question addQuestion(Question question){
        return questionRepo.save(question);
    }
    public Question getQuestionById(long id){
        return questionRepo.findById(id).orElseThrow();
    }
}
