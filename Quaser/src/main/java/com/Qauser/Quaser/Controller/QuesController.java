package com.Qauser.Quaser.Controller;


import com.Qauser.Quaser.Entity.Question;
import com.Qauser.Quaser.Service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ques")
public class QuesController {

    private final QuestionService questionService;

    public QuesController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    private List<Question> getQuestions(){
        return questionService.getAllQuestions();
    }

    @PostMapping("/addQues")
    private Question newQuestion(@RequestBody Question question){
        return questionService.addQuestion(question);
    }

    @GetMapping("/questions/{id}")
    public Question getQuestionById(long id){
        return questionService.getQuestionById(id);
    }
}
