package org.example.configuracoes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ConfiguracaoTempo {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
