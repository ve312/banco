package com.trinity.banco.shared.config;

import com.trinity.banco.cuenta.application.NumeroCuentaGenerator;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public NumeroCuentaGenerator numeroCuentaGenerator(CuentaRepository cuentaRepository) {
        return new NumeroCuentaGenerator(cuentaRepository);
    }
}
