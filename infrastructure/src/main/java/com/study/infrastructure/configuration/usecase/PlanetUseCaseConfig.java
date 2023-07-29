package com.study.infrastructure.configuration.usecase;

import com.study.application.planet.create.CreatePlanetUseCase;
import com.study.application.planet.create.DefaultCreatePlanetUseCase;
import com.study.application.planet.delete.DefaultDeletePlanetUseCase;
import com.study.application.planet.delete.DeletePlanetUseCase;
import com.study.application.planet.retrieve.get.DefaultGetPlanetByIdUseCase;
import com.study.application.planet.retrieve.get.GetPlanetByIdUseCase;
import com.study.application.planet.retrieve.list.DefaultListPlanetUseCase;
import com.study.application.planet.retrieve.list.ListPlanetUseCase;
import com.study.application.planet.update.DefaultUpdatePlanetUseCase;
import com.study.application.planet.update.UpdatePlanetUseCase;
import com.study.domain.planet.PlanetGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class PlanetUseCaseConfig {
    private final PlanetGateway gateway;

    public PlanetUseCaseConfig(final PlanetGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Bean
    public CreatePlanetUseCase createPlanetUseCase() {
        return new DefaultCreatePlanetUseCase(gateway);
    }

    @Bean
    public DeletePlanetUseCase deletePlanetUseCase() {
        return new DefaultDeletePlanetUseCase(gateway);
    }

    @Bean
    public UpdatePlanetUseCase updatePlanetUseCase() {
        return new DefaultUpdatePlanetUseCase(gateway);
    }

    @Bean
    public GetPlanetByIdUseCase getPlanetByIdUseCase() {
        return new DefaultGetPlanetByIdUseCase(gateway);
    }

    @Bean
    public ListPlanetUseCase listPlanetUseCase() {
        return new DefaultListPlanetUseCase(gateway);
    }
}
