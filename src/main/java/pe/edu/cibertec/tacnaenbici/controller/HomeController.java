package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(Model model) {
        return "home/index";
    }

    @GetMapping("/nosotros")
    public String mostrarNosotros() {
        return "nosotros";
    }

    @GetMapping("/servicios")
    public String mostrarServicios() {
        return "servicios";
    }

}