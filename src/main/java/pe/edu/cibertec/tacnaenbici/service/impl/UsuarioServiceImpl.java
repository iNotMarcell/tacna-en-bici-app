package pe.edu.cibertec.tacnaenbici.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.tacnaenbici.model.Usuario;
import pe.edu.cibertec.tacnaenbici.repository.UsuarioRepository;
import pe.edu.cibertec.tacnaenbici.service.UsuarioService;

import java.time.LocalDateTime;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario guardar(Usuario usuario) {
        usuario.setEstado(true);
        usuario.setRol("USUARIO");
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return repository.save(usuario);
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {

        return repository.findByCorreo(correo).orElse(null);

    }
}