package pe.edu.cibertec.tacnaenbici.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pe.edu.cibertec.tacnaenbici.model.Usuario;
import pe.edu.cibertec.tacnaenbici.repository.UsuarioRepository;
import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @ModelAttribute("usuarioLogueado")
    public Usuario globalUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String correo = auth.getName();
            Object result = usuarioRepository.findByCorreo(correo);
            
            if (result instanceof Optional) {
                return ((Optional<Usuario>) result).orElse(null);
            }
            return (Usuario) result;
        }
        return null;
    }
}