package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pe.edu.cibertec.tacnaenbici.model.Bicicleta;
import pe.edu.cibertec.tacnaenbici.model.Usuario;
import pe.edu.cibertec.tacnaenbici.service.AvistamientoService;
import pe.edu.cibertec.tacnaenbici.service.BicicletaService;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/bicicletas")
public class BicicletaController {

    private final BicicletaService bicicletaService;
    private final AvistamientoService avistamientoService;

    public BicicletaController(
            BicicletaService bicicletaService,
            AvistamientoService avistamientoService) {

        this.bicicletaService = bicicletaService;
        this.avistamientoService = avistamientoService;
    }

    @GetMapping
    public String listar(@RequestParam(value = "buscar", required = false) String buscar, Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");
        List<Bicicleta> todas = bicicletaService.listarTodas();

        if (buscar != null && !buscar.isEmpty()) {
            String q = buscar.toLowerCase();
            todas = todas.stream()
                    .filter(b -> (b.getMarca() != null && b.getMarca().toLowerCase().contains(q)) ||
                            (b.getModelo() != null && b.getModelo().toLowerCase().contains(q)) ||
                            (b.getDistrito() != null && b.getDistrito().toLowerCase().contains(q)))
                    .toList();
        }

        List<Bicicleta> publicasAprobadas = todas.stream()
                .filter(b -> "APROBADA".equals(b.getEstadoAprobacion()))
                .toList();

        model.addAttribute("bicicletas", publicasAprobadas);
        model.addAttribute("buscar", buscar);

        if (usuario != null) {
            if ("ADMIN".equals(usuario.getRol()) || "ROLE_ADMIN".equals(usuario.getRol())) {
                model.addAttribute("tablaAbajo", todas);
            } else {
                List<Bicicleta> misReportes = todas.stream()
                        .filter(b -> b.getUsuario() != null && b.getUsuario().getId().equals(usuario.getId()))
                        .toList();
                model.addAttribute("tablaAbajo", misReportes);
            }
        }

        return "bicicleta/listar";
    }

    @GetMapping("/encontrada/{id}")
    public String marcarEncontrada(@PathVariable Long id, Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        Bicicleta bicicleta = bicicletaService.buscarPorId(id).orElse(null);
        if (bicicleta != null) {
            if ("ADMIN".equals(usuario.getRol()) || "ROLE_ADMIN".equals(usuario.getRol()) || bicicleta.getUsuario().getId().equals(usuario.getId())) {
                bicicleta.setEstado("RECUPERADA");
                bicicletaService.guardar(bicicleta);
                return "redirect:/bicicletas?encontrada=true";
            }
        }
        return "redirect:/bicicletas?error=true";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("bicicleta", new Bicicleta());
        return "bicicleta/registro";
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute("bicicleta") Bicicleta bicicleta,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(value = "imagen2", required = false) MultipartFile imagen2,
            @RequestParam(value = "imagen3", required = false) MultipartFile imagen3,
            @RequestParam(value = "imagen4", required = false) MultipartFile imagen4,
            Model model) {

        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            if (bicicleta.getId() != null) {
                Bicicleta biciOriginal = bicicletaService.buscarPorId(bicicleta.getId()).orElse(null);

                if (biciOriginal != null) {
                    if (imagen == null || imagen.isEmpty()) bicicleta.setFoto(biciOriginal.getFoto());
                    if (imagen2 == null || imagen2.isEmpty()) bicicleta.setFoto2(biciOriginal.getFoto2());
                    if (imagen3 == null || imagen3.isEmpty()) bicicleta.setFoto3(biciOriginal.getFoto3());
                    if (imagen4 == null || imagen4.isEmpty()) bicicleta.setFoto4(biciOriginal.getFoto4());

                    bicicleta.setEstado(biciOriginal.getEstado());
                    bicicleta.setEstadoAprobacion(biciOriginal.getEstadoAprobacion());
                    bicicleta.setUsuario(biciOriginal.getUsuario());
                }
            } else {
                bicicleta.setEstado("ROBADA");
                bicicleta.setEstadoAprobacion("PENDIENTE");
                bicicleta.setUsuario(usuario);
            }

            if (imagen != null && !imagen.isEmpty()) bicicleta.setFoto(java.util.Base64.getEncoder().encodeToString(imagen.getBytes()));
            if (imagen2 != null && !imagen2.isEmpty()) bicicleta.setFoto2(java.util.Base64.getEncoder().encodeToString(imagen2.getBytes()));
            if (imagen3 != null && !imagen3.isEmpty()) bicicleta.setFoto3(java.util.Base64.getEncoder().encodeToString(imagen3.getBytes()));
            if (imagen4 != null && !imagen4.isEmpty()) bicicleta.setFoto4(java.util.Base64.getEncoder().encodeToString(imagen4.getBytes()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        bicicletaService.guardar(bicicleta);
        return "redirect:/bicicletas?guardado";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        bicicletaService.eliminar(id);
        return "redirect:/bicicletas?eliminado";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Bicicleta bicicleta = bicicletaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta no encontrada"));

        model.addAttribute("bicicleta", bicicleta);
        model.addAttribute("avistamientos", avistamientoService.listarPorBicicleta(id));

        return "bicicleta/detalle";
    }

    @GetMapping("/aprobar/{id}")
    public String aprobar(@PathVariable Long id, Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");

        if (usuario == null || (!"ADMIN".equals(usuario.getRol()) && !"ROLE_ADMIN".equals(usuario.getRol()))) {
            return "redirect:/bicicletas";
        }

        Bicicleta bicicleta = bicicletaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Bicicleta no encontrada"));

        bicicleta.setEstadoAprobacion("APROBADA");
        bicicletaService.guardar(bicicleta);

        return "redirect:/bicicletas";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        Bicicleta bicicleta = bicicletaService.buscarPorId(id).orElse(null);
        if (bicicleta == null) {
            return "redirect:/bicicletas";
        }

        boolean esDueño = bicicleta.getUsuario() != null && bicicleta.getUsuario().getId().equals(usuario.getId());
        boolean esAdmin = "ADMIN".equals(usuario.getRol()) || "ROLE_ADMIN".equals(usuario.getRol());

        if (!esDueño && !esAdmin) {
            return "redirect:/bicicletas";
        }

        model.addAttribute("bicicleta", bicicleta);
        return "bicicleta/editar";
    }
}