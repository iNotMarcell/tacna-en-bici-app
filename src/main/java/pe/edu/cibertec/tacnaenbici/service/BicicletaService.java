package pe.edu.cibertec.tacnaenbici.service;

import pe.edu.cibertec.tacnaenbici.model.Bicicleta;

import java.util.List;
import java.util.Optional;

public interface BicicletaService {

    List<Bicicleta> listarTodas();

    List<Bicicleta> buscar(String texto);

    List<Bicicleta> listarPorUsuario(Long usuarioId);

    Optional<Bicicleta> buscarPorId(Long id);

    Bicicleta guardar(Bicicleta bicicleta);

    void eliminar(Long id);

    List<Bicicleta> listarPendientes();
}