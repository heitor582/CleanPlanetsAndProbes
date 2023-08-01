package com.study.infrastructure.configuration.usecase;

import com.study.application.probe.create.CreateProbeUseCase;
import com.study.application.probe.create.DefaultCreateProbeUseCase;
import com.study.application.probe.delete.DefaultDeleteProbeUseCase;
import com.study.application.probe.delete.DeleteProbeUseCase;
import com.study.application.probe.move.DefaultMoveProbeUseCase;
import com.study.application.probe.move.MoveProbeUseCase;
import com.study.application.probe.retrieve.get.DefaultGetProbeByIdUseCase;
import com.study.application.probe.retrieve.get.GetProbeByIdUseCase;
import com.study.application.probe.retrieve.list.DefaultListProbeUseCase;
import com.study.application.probe.retrieve.list.ListProbeUseCase;
import com.study.application.probe.update.DefaultUpdateProbeUseCase;
import com.study.application.probe.update.UpdateProbeUseCase;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.probe.ProbeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class ProbeUseCaseConfig {
    private final ProbeGateway gateway;
    private final PlanetGateway planetGateway;

    public ProbeUseCaseConfig(final ProbeGateway probeGateway, final PlanetGateway planetGateway) {
        this.gateway = Objects.requireNonNull(probeGateway);
        this.planetGateway = Objects.requireNonNull(planetGateway);
    }

    @Bean
    public CreateProbeUseCase createProbeUseCase() {
        return new DefaultCreateProbeUseCase(planetGateway, gateway);
    }

    @Bean
    public DeleteProbeUseCase deleteProbeUseCase() {
        return new DefaultDeleteProbeUseCase(gateway);
    }

    @Bean
    public UpdateProbeUseCase updateProbeUseCase() {
        return new DefaultUpdateProbeUseCase(gateway, planetGateway);
    }

    @Bean
    public GetProbeByIdUseCase getProbeByIdUseCase() {
        return new DefaultGetProbeByIdUseCase(gateway);
    }

    @Bean
    public ListProbeUseCase listProbeUseCase() {
        return new DefaultListProbeUseCase(gateway);
    }

    @Bean
    public MoveProbeUseCase moveProbeUseCase() {
        return new DefaultMoveProbeUseCase(gateway, planetGateway);
    }
}
