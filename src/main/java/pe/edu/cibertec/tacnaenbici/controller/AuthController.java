package pe.edu.cibertec.tacnaenbici.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pe.edu.cibertec.tacnaenbici.dto.LoginDTO;
import pe.edu.cibertec.tacnaenbici.security.JwtUtil;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "auth/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute("login") LoginDTO loginDTO, HttpServletResponse response, Model model) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getCorreo(), loginDTO.getPassword())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities().iterator().next().getAuthority());

            Cookie jwtCookie = new Cookie("jwt_token", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60 * 10);
            response.addCookie(jwtCookie);

            return "redirect:/";

        } catch (Exception e) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            model.addAttribute("login", new LoginDTO());
            return "auth/login";
        }
    }
}