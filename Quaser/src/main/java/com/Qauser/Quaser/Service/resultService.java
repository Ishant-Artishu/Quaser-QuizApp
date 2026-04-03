package com.Qauser.Quaser.Service;

import com.Qauser.Quaser.Entity.Question;
import com.Qauser.Quaser.Entity.Quiz;
import com.Qauser.Quaser.Repository.QuizRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class resultService {

    private final QuizRepo quizRepo;

    public resultService(QuizRepo quizRepo) {
        this.quizRepo = quizRepo;
    }


    public int calculations(Long quizId, List<String> answers){
        Quiz quiz = quizRepo.findById(quizId).orElseThrow();

        List<Question> ques = quiz.getQuestions();
        int score = 0;
        for(int i = 0; i < ques.size(); i++){
            if(ques.get(i).getCorrectAnswer().equals(answers.get(i))){
            score++;
            }
        }
        return score;
    }
}
