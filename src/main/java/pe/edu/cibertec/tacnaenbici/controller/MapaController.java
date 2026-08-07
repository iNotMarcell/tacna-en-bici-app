package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.cibertec.tacnaenbici.service.AvistamientoService;
import pe.edu.cibertec.tacnaenbici.service.BicicletaService;

@Controller
public class MapaController {

    private final BicicletaService bicicletaService;
    private final AvistamientoService avistamientoService;

    public MapaController(BicicletaService bicicletaService, AvistamientoService avistamientoService) {
        this.bicicletaService = bicicletaService;
        this.avistamientoService = avistamientoService;
    }

    @GetMapping("/mapa")
    public String mostrarMapa(Model model) {
        model.addAttribute("bicicletas", bicicletaService.listarTodas());
        model.addAttribute("avistamientos", avistamientoService.listar());
        return "mapa";
    }
}