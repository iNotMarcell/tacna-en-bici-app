package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pe.edu.cibertec.tacnaenbici.model.Usuario;
import pe.edu.cibertec.tacnaenbici.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String registro(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "auth/registro";
    }

    @PostMapping("/registro")
    public String guardar(@ModelAttribute Usuario usuario) {

        usuarioService.guardar(usuario);

        return "redirect:/login";
    }

}