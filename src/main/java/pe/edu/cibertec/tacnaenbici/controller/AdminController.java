package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pe.edu.cibertec.tacnaenbici.model.Bicicleta;
import pe.edu.cibertec.tacnaenbici.model.Usuario;
import pe.edu.cibertec.tacnaenbici.service.BicicletaService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BicicletaService bicicletaService;

    public AdminController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }


    @GetMapping("/pendientes")
    public String pendientes(Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");

        if (usuario == null || (!"ADMIN".equals(usuario.getRol()) && !"ROLE_ADMIN".equals(usuario.getRol()))) {
            return "redirect:/login";
        }

        List<Bicicleta> todas = bicicletaService.listarTodas();
        List<Bicicleta> ordenadas = todas.stream()
                .sorted((b1, b2) -> Long.compare(b2.getId(), b1.getId()))
                .toList();

        model.addAttribute("reportes", ordenadas);

        return "admin/pendientes";
    }

    @GetMapping("/aprobar/{id}")
    public String aprobar(@PathVariable Long id, Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");

        if (usuario == null || (!"ADMIN".equals(usuario.getRol()) && !"ROLE_ADMIN".equals(usuario.getRol()))) {
            return "redirect:/bicicletas";
        }

        try {
            Bicicleta bicicleta = bicicletaService.buscarPorId(id).orElseThrow();
            bicicleta.setEstadoAprobacion("APROBADA");
            bicicletaService.guardar(bicicleta);

            return "redirect:/admin/pendientes?exito=true";

        } catch (Exception e) {
            return "redirect:/admin/pendientes?error=true";
        }
    }

    @GetMapping("/reportes/nuevo")
    public String nuevoReporteAdmin(Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");

        if (usuario == null || (!"ADMIN".equals(usuario.getRol()) && !"ROLE_ADMIN".equals(usuario.getRol()))) {
            return "redirect:/login";
        }

        model.addAttribute("bicicleta", new Bicicleta());

        return "admin/reporte-form";
    }
}