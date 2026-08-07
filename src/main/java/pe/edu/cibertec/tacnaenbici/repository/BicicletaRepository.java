package pe.edu.cibertec.tacnaenbici.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.tacnaenbici.model.Bicicleta;

import java.util.List;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {

    List<Bicicleta> findByEstadoAprobacion(String estadoAprobacion);

    List<Bicicleta> findByEstadoAprobacionAndMarcaContainingIgnoreCaseOrEstadoAprobacionAndModeloContainingIgnoreCase(
            String estado1,
            String marca,
            String estado2,
            String modelo
    );

    List<Bicicleta> findByUsuarioId(Long usuarioId);

    List<Bicicleta> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(String marca, String modelo);
}