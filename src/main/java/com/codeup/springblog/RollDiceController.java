package com.codeup.springblog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ThreadLocalRandom;

@Controller
public class RollDiceController {
    @GetMapping("/roll-dice")
    public String rollDiceView() {
        return "roll-dice";
    }

    @GetMapping("/roll-dice/{n}")
    public String rollDiceN(@PathVariable int n, Model model) {
        String message;

        int randomNumber = ThreadLocalRandom.current().nextInt(1,6 + 1);

        if(n == randomNumber) {
            message = "Good Guess";
        } else {
            message ="Wrong! Try again!";
        }

        model.addAttribute("randomNumber", randomNumber);
        model.addAttribute("n", n);
        model.addAttribute("message", message);

        return "roll-results";
    }

}
