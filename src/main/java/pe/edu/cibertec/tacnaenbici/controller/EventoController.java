package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.cibertec.tacnaenbici.model.Evento;
import pe.edu.cibertec.tacnaenbici.service.EventoService;
import java.util.List;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public String eventos(Model model) {
        List<Evento> eventos = eventoService.listarTodos();
        
        if (!eventos.isEmpty()) {
            model.addAttribute("destacado", eventos.get(0));
            
            if (eventos.size() > 1) {
                model.addAttribute("lista", eventos.subList(1, eventos.size()));
            } else {
                model.addAttribute("lista", null);
            }
        }
        
        return "eventos";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleNoticia(@PathVariable Long id, Model model) {
        Evento evento = eventoService.buscarPorId(id).orElse(null);

        if (evento == null) {
            return "redirect:/eventos";
        }

        model.addAttribute("evento", evento);
        return "evento-detalle";
    }
}
