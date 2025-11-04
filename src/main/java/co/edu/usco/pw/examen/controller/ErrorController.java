package co.edu.usco.pw.examen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    
    @GetMapping("/error-acceso")
    public String accessDenied() {
        return "forward:/error-acceso.html";
    }
}