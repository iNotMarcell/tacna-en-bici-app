package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.cibertec.tacnaenbici.model.Bicicleta;
import pe.edu.cibertec.tacnaenbici.model.Usuario;
import pe.edu.cibertec.tacnaenbici.service.BicicletaService;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final BicicletaService bicicletaService;

    public AdminDashboardController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    @GetMapping("/panel")
    public String panel(Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");

        if (usuario == null || (!"ADMIN".equals(usuario.getRol()) && !"ROLE_ADMIN".equals(usuario.getRol()))) {
            return "redirect:/login";
        }

        List<Bicicleta> todas = bicicletaService.listarTodas();

        long reportesHoy = todas.stream()
                .filter(b -> b.getFechaRobo() != null && b.getFechaRobo().isEqual(LocalDate.now()))
                .count();

        long recuperadas = todas.stream()
                .filter(b -> "RECUPERADA".equals(b.getEstado()))
                .count();

        String zonaCritica = todas.stream()
                .filter(b -> b.getDistrito() != null && !b.getDistrito().trim().isEmpty())
                .collect(Collectors.groupingBy(Bicicleta::getDistrito, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin datos");

        List<Bicicleta> ultimosReportes = todas.stream()
                .sorted((b1, b2) -> Long.compare(b2.getId(), b1.getId()))
                .limit(3)
                .toList();

        List<String> dias = new ArrayList<>();
        List<Long> robosPorDia = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate fecha = LocalDate.now().minusDays(i);
            String nombreDia = fecha.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            dias.add(nombreDia.substring(0, 1).toUpperCase() + nombreDia.substring(1));

            long count = todas.stream()
                    .filter(b -> b.getFechaRobo() != null && b.getFechaRobo().isEqual(fecha))
                    .count();
            robosPorDia.add(count);
        }

        model.addAttribute("totalReportes", todas.size());
        model.addAttribute("reportesHoy", reportesHoy);
        model.addAttribute("totalRecuperadas", recuperadas);
        model.addAttribute("zonaCritica", zonaCritica);
        model.addAttribute("ultimosReportes", ultimosReportes);
        model.addAttribute("chartLabels", dias);
        model.addAttribute("chartData", robosPorDia);

        return "admin/panel";
    }

    @GetMapping("/estadisticas")
    public String estadisticas(Model model) {
        Usuario usuario = (Usuario) model.getAttribute("usuarioLogueado");

        if (usuario == null || (!"ADMIN".equals(usuario.getRol()) && !"ROLE_ADMIN".equals(usuario.getRol()))) {
            return "redirect:/login";
        }

        List<Bicicleta> todas = bicicletaService.listarTodas();

        long totalReportes = todas.size();
        long recuperadas = todas.stream().filter(b -> "RECUPERADA".equals(b.getEstado())).count();
        long conDenuncia = todas.stream().filter(b -> Boolean.TRUE.equals(b.getDenunciaPolicial())).count();

        int porcentajeRecuperacion = totalReportes > 0 ? (int) ((recuperadas * 100) / totalReportes) : 0;

        Map<String, Long> porDistrito = todas.stream()
                .filter(b -> b.getDistrito() != null && !b.getDistrito().trim().isEmpty())
                .collect(Collectors.groupingBy(Bicicleta::getDistrito, Collectors.counting()));

        Map<String, Long> porTipo = todas.stream()
                .filter(b -> b.getTipo() != null && !b.getTipo().trim().isEmpty())
                .collect(Collectors.groupingBy(Bicicleta::getTipo, Collectors.counting()));

        Map<String, Long> porEstado = todas.stream()
                .filter(b -> b.getEstado() != null)
                .collect(Collectors.groupingBy(Bicicleta::getEstado, Collectors.counting()));

        model.addAttribute("totalReportes", totalReportes);
        model.addAttribute("recuperadas", recuperadas);
        model.addAttribute("conDenuncia", conDenuncia);
        model.addAttribute("porcentajeRecuperacion", porcentajeRecuperacion);

        model.addAttribute("porDistrito", porDistrito);
        model.addAttribute("porTipo", porTipo);
        model.addAttribute("porEstado", porEstado);

        return "admin/estadisticas";
    }
}