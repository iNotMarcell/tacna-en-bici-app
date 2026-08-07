package pe.edu.cibertec.tacnaenbici.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.tacnaenbici.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}