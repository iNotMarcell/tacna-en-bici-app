package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.tacnaenbici.model.Evento;
import pe.edu.cibertec.tacnaenbici.service.EventoService;

@Controller
@RequestMapping("/admin/eventos")
public class AdminEventoController {

    private final EventoService eventoService;

    public AdminEventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public String listarEventos(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        return "admin/eventos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("evento", new Evento());
        return "admin/evento-form";
    }

    @PostMapping("/guardar")
    public String guardarEvento(@ModelAttribute Evento evento) {
        eventoService.guardar(evento);
        return "redirect:/admin/eventos?exito";
    }




    @GetMapping("/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model) {
        Evento evento = eventoService.buscarPorId(id).orElse(null);

        if (evento == null) {
            return "redirect:/admin/eventos?error";
        }

        model.addAttribute("evento", evento);

        return "admin/evento-form";
    }
}