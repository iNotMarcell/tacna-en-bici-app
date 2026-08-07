package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.cibertec.tacnaenbici.model.Avistamiento;
import pe.edu.cibertec.tacnaenbici.model.Bicicleta;
import pe.edu.cibertec.tacnaenbici.model.Usuario;
import pe.edu.cibertec.tacnaenbici.service.AvistamientoService;
import pe.edu.cibertec.tacnaenbici.service.BicicletaService;

import java.util.Base64;

@Controller
@RequestMapping("/avistamientos")
public class AvistamientoController {

    private final AvistamientoService avistamientoService;
    private final BicicletaService bicicletaService;

    public AvistamientoController(
            AvistamientoService avistamientoService,
            BicicletaService bicicletaService) {

        this.avistamientoService = avistamientoService;
        this.bicicletaService = bicicletaService;
    }

    @GetMapping("/nuevo/{id}")
    public String nuevo(
            @PathVariable Long id,
            Model model) {

        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Bicicleta bicicleta = bicicletaService.buscarPorId(id).orElseThrow();

        Avistamiento avistamiento = new Avistamiento();
        avistamiento.setBicicleta(bicicleta);

        model.addAttribute("avistamiento", avistamiento);
        model.addAttribute("bicicleta", bicicleta);

        return "avistamiento/registrar";
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Avistamiento avistamiento,
            @RequestParam("bicicletaId") Long bicicletaId,
            @RequestParam(value = "imagen1", required = false) MultipartFile imagen1,
            @RequestParam(value = "imagen2", required = false) MultipartFile imagen2,
            Model model) {

        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            Bicicleta bicicleta = bicicletaService.buscarPorId(bicicletaId).orElse(null);
            avistamiento.setBicicleta(bicicleta);

            if (imagen1 != null && !imagen1.isEmpty()) {
                avistamiento.setFoto(java.util.Base64.getEncoder().encodeToString(imagen1.getBytes()));
            }
            if (imagen2 != null && !imagen2.isEmpty()) {
                avistamiento.setFoto2(java.util.Base64.getEncoder().encodeToString(imagen2.getBytes()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        avistamientoService.guardar(avistamiento);

        return "redirect:/bicicletas/" + bicicletaId + "?avistamiento=exito";
    }

    @GetMapping
    public String listar(Model model) {

        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");

        if (usuario == null || (!"ADMIN".equals(usuario.getRol()) && !"ROLE_ADMIN".equals(usuario.getRol()))) {
            return "redirect:/login";
        }

        model.addAttribute("lista", avistamientoService.listar());

        return "avistamiento/listar";
    }
}