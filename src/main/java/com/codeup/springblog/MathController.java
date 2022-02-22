package com.codeup.springblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MathController {

    //REQUEST MAPPING
    @RequestMapping(path = "/add/{number}/and/{number2}", method = RequestMethod.GET)
    @ResponseBody
    public String add(@PathVariable int number, @PathVariable int number2) {
        return number + " plus " + number2 + " is " + (number + number2);
//        return "The answer is: " + (number + number2);
    }


    @GetMapping(path = "/subtract/{number}/from/{number2}")
    @ResponseBody
    public int subtraction(@PathVariable int number, @PathVariable int number2) {
        return number - number2;
    }


    @GetMapping(path = "/multiply/{number}/and/{number2}")
    @ResponseBody
    public int multiply(@PathVariable int number, @PathVariable int number2) {
        return number * number2;
    }

    //REQUEST MAPPING
    @RequestMapping(path = "/divide/{number}/by/{number2}", method = RequestMethod.GET)
    @ResponseBody
    public String divide(@PathVariable int number, @PathVariable int number2) {
        return number + " divided by " + number2 + " is equal to " + (number / number2) + "!";
    }

    //Using double
    @GetMapping("/divide/{num1}/by/{num2}")
    @ResponseBody
    public double division(@PathVariable double num1, @PathVariable double num2) {
        return num1 / num2;
    }


}
