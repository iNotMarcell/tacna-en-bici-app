package pe.edu.cibertec.tacnaenbici.service;

import pe.edu.cibertec.tacnaenbici.model.Evento;
import java.util.List;
import java.util.Optional;

public interface EventoService {
    List<Evento> listarTodos();
    Optional<Evento> buscarPorId(Long id);
    Evento guardar(Evento evento);
    void eliminar(Long id);
}