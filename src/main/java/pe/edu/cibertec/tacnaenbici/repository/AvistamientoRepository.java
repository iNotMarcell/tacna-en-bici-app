package pe.edu.cibertec.tacnaenbici.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.tacnaenbici.model.Avistamiento;

import java.util.List;

public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {

    List<Avistamiento> findByBicicletaId(Long bicicletaId);

}