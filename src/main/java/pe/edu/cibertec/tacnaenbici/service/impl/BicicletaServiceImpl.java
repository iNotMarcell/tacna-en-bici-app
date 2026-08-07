package pe.edu.cibertec.tacnaenbici.service.impl;

import org.springframework.stereotype.Service;
import pe.edu.cibertec.tacnaenbici.model.Bicicleta;
import pe.edu.cibertec.tacnaenbici.repository.BicicletaRepository;
import pe.edu.cibertec.tacnaenbici.service.BicicletaService;

import java.util.List;
import java.util.Optional;

@Service
public class BicicletaServiceImpl implements BicicletaService {

    private final BicicletaRepository bicicletaRepository;

    public BicicletaServiceImpl(BicicletaRepository bicicletaRepository) {
        this.bicicletaRepository = bicicletaRepository;
    }

    @Override
    public List<Bicicleta> listarTodas() {
        return bicicletaRepository.findAll();
    }

    @Override
    public List<Bicicleta> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listarTodas();
        }
        return bicicletaRepository.findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(texto, texto);
    }

    @Override
    public List<Bicicleta> listarPorUsuario(Long usuarioId) {
        return bicicletaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Optional<Bicicleta> buscarPorId(Long id) {
        return bicicletaRepository.findById(id);
    }

    @Override
    public Bicicleta guardar(Bicicleta bicicleta) {
        return bicicletaRepository.save(bicicleta);
    }

    @Override
    public List<Bicicleta> listarPendientes() {
        return bicicletaRepository.findByEstadoAprobacion("PENDIENTE");
    }

    @Override
    public void eliminar(Long id) {
        bicicletaRepository.deleteById(id);
    }
}