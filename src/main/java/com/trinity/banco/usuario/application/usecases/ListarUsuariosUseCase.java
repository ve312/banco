package com.trinity.banco.usuario.application.usecases;

import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;

import java.util.List;

public class ListarUsuariosUseCase {
    private final UsuarioRepository usuarioRepository;

    public ListarUsuariosUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> ejecutar() {
        return usuarioRepository.listar();
    }
}
