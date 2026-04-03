package com.Qauser.Quaser.Controller;

import com.Qauser.Quaser.Entity.Question;
import com.Qauser.Quaser.Service.QuizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {


    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }


    @GetMapping("/generate")
    private List<Question> generateQuestions(@RequestParam int reqSize){
        return quizService.generateQuestions(reqSize);
    }
}
