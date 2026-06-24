package com.trinity.banco.shared.infrastructure.config;

import com.trinity.banco.cliente.application.usecases.*;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cuenta.application.usecases.*;
import com.trinity.banco.cuenta.application.util.NumeroCuentaGenerator;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.transaccion.application.usecases.ConsignarUseCase;
import com.trinity.banco.transaccion.application.usecases.ListarTransaccionesPorCuentaUseCase;
import com.trinity.banco.transaccion.application.usecases.RetirarUseCase;
import com.trinity.banco.transaccion.application.usecases.TransferirUseCase;
import com.trinity.banco.transaccion.domain.ports.TransaccionRepository;
import com.trinity.banco.usuario.application.usecases.*;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {
    @Bean
    public NumeroCuentaGenerator numeroCuentaGenerator(CuentaRepository cuentaRepository) {
        return new NumeroCuentaGenerator(cuentaRepository);
    }

    @Bean
    public ActualizarClienteUseCase actualizarClienteUseCase(ClienteRepository clienteRepository) {
        return new ActualizarClienteUseCase(clienteRepository);
    }

    @Bean
    public CrearClienteUseCase crearClienteUseCase(ClienteRepository clienteRepository) {
        return new CrearClienteUseCase(clienteRepository);
    }

    @Bean
    public EliminarClienteUseCase eliminarClienteUseCase(ClienteRepository clienteRepository, CuentaRepository cuentaRepository) {
        return new EliminarClienteUseCase(clienteRepository, cuentaRepository);
    }

    @Bean
    public ObtenerClienteUseCase obtenerClienteUseCase(ClienteRepository clienteRepository) {
        return new ObtenerClienteUseCase(clienteRepository);
    }

    @Bean
    public ListarClientesUseCase listarClientesUseCase(ClienteRepository clienteRepository) {
        return new ListarClientesUseCase(clienteRepository);
    }

    @Bean
    public ActivarCuentaUseCase activarCuentaUseCase(CuentaRepository cuentaRepository) {
        return new ActivarCuentaUseCase(cuentaRepository);
    }

    @Bean
    public CancelarCuentaUseCase cancelarCuentaUseCase(CuentaRepository cuentaRepository) {
        return new CancelarCuentaUseCase(cuentaRepository);
    }

    @Bean
    public CrearCuentaUseCase crearCuentaUseCase (CuentaRepository cuentaRepository, ClienteRepository clienteRepository, NumeroCuentaGenerator numeroCuentaGenerator){
        return new CrearCuentaUseCase(cuentaRepository, clienteRepository, numeroCuentaGenerator);
    }

    @Bean
    public InactivarCuentaUseCase inactivarCuentaUseCase(CuentaRepository cuentaRepository) {
        return new InactivarCuentaUseCase(cuentaRepository);
    }

    @Bean
    public ObtenerCuentaUseCase obtenerCuentaUseCase(CuentaRepository cuentaRepository) {
        return new ObtenerCuentaUseCase(cuentaRepository);
    }

    @Bean
    public ListarTodasLasCuentasUseCase listarTodasLasCuentasUseCase(CuentaRepository cuentaRepository) {
        return new ListarTodasLasCuentasUseCase(cuentaRepository);
    }

    @Bean
    public ListarCuentasPorClienteUseCase listarCuentasPorClienteUseCase (CuentaRepository cuentaRepository, ClienteRepository clienteRepository){
        return new ListarCuentasPorClienteUseCase(cuentaRepository, clienteRepository);
    }

    @Bean
    public ConsignarUseCase ConsignarUseCase(CuentaRepository cuentaRepository, TransaccionRepository transaccionRepository) {
        return new ConsignarUseCase(cuentaRepository, transaccionRepository);
    }

    @Bean
    public ListarTransaccionesPorCuentaUseCase listarTransaccionesPorCuentaUseCase(TransaccionRepository transaccionRepository, CuentaRepository cuentaRepository) {
        return new ListarTransaccionesPorCuentaUseCase(transaccionRepository, cuentaRepository);
    }

    @Bean
    public RetirarUseCase retirarUseCase(CuentaRepository cuentaRepository, TransaccionRepository transaccionRepository) {
        return new RetirarUseCase(cuentaRepository, transaccionRepository);
    }

    @Bean
    public TransferirUseCase transferirUseCase(CuentaRepository cuentaRepository, TransaccionRepository transaccionRepository) {
        return new TransferirUseCase(cuentaRepository, transaccionRepository);
    }

    @Bean
    public CrearUsuarioUseCase crearUsuarioUseCase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return new CrearUsuarioUseCase(usuarioRepository, passwordEncoder);
    }

    @Bean
    public ActualizarUsuarioUseCase actualizarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new ActualizarUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public EliminarUsuarioUseCase eliminarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new EliminarUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public ObtenerUsuarioUseCase obtenerUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new ObtenerUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioRepository usuarioRepository) {
        return new ListarUsuariosUseCase(usuarioRepository);
    }

    @Bean
    public CambiarPasswordUseCase cambiarPasswordUseCase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return new CambiarPasswordUseCase(usuarioRepository, passwordEncoder);
    }

}
