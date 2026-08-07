package pe.edu.cibertec.tacnaenbici.service;

import org.springframework.stereotype.Service;
import pe.edu.cibertec.tacnaenbici.model.Avistamiento;
import pe.edu.cibertec.tacnaenbici.repository.AvistamientoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AvistamientoService {

    private final AvistamientoRepository repository;

    public AvistamientoService(AvistamientoRepository repository) {
        this.repository = repository;
    }

    public List<Avistamiento> listar() {
        return repository.findAll();
    }

    public List<Avistamiento> listarPorBicicleta(Long bicicletaId) {
        return repository.findByBicicletaId(bicicletaId);
    }

    public Optional<Avistamiento> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void guardar(Avistamiento avistamiento) {
        repository.save(avistamiento);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

}