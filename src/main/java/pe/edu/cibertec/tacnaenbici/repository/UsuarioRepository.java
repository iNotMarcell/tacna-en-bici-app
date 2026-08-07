package pe.edu.cibertec.tacnaenbici.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.tacnaenbici.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

}