package pe.edu.cibertec.tacnaenbici.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.tacnaenbici.model.Evento;
import pe.edu.cibertec.tacnaenbici.repository.EventoRepository;
import pe.edu.cibertec.tacnaenbici.service.EventoService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository repository;

    @Override
    public List<Evento> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<Evento> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Evento guardar(Evento evento) {
        if (evento.getFechaPublicacion() == null) {
            evento.setFechaPublicacion(LocalDateTime.now());
        }
        return repository.save(evento);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}