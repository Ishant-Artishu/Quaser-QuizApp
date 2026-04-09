package com.Qauser.Quaser.Controller;

import com.Qauser.Quaser.Service.resultService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/result")
public class resultController {

    private final resultService res;

    public resultController(resultService res) {
        this.res = res;
    }

    @GetMapping("/score")
    public int getMyScore(@RequestParam Long quizId,
                          @RequestParam String answers){
        List<String> answerList = Arrays.asList(answers.split(","));

        return res.calculations(quizId, answerList);
    }
}
