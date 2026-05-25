package com.trinity.banco.config;

import com.trinity.banco.domain.Service.NumeroCuentaGenerator;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public NumeroCuentaGenerator numeroCuentaGenerator(CuentaRepository cuentaRepository) {
        return new NumeroCuentaGenerator(cuentaRepository);
    }
}
