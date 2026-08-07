package pe.edu.cibertec.tacnaenbici.service;

import pe.edu.cibertec.tacnaenbici.model.Usuario;

public interface UsuarioService {

    Usuario guardar(Usuario usuario);

    Usuario buscarPorCorreo(String correo);

}