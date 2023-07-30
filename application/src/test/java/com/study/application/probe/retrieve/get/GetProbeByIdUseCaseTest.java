package com.study.application.probe.retrieve.get;

import com.study.application.UseCaseTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.planet.Planet;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetProbeByIdUseCaseTest extends UseCaseTest {
    @Mock
    private ProbeGateway gateway;
    @InjectMocks
    private DefaultGetProbeByIdUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidId_whenCallsGetProbe_shouldReturnIt() {
        final var planet = Planet.newPlanet(1,1, "teste");
        final var probe = Probe.newProbe("teste",1,1, planet);
        final var id = probe.getId();

        when(gateway.findBy(id)).thenReturn(Optional.of(probe));

        final var output = useCase.execute(id.getValue());

        assertEquals(probe.getId().getValue(), output.id());
        assertEquals(probe.getName(), output.name());
        assertEquals(probe.getCordX(), output.cordX());
        assertEquals(probe.getCordY(), output.cordY());
        assertEquals(probe.getDirection(), output.direction());
        assertEquals(probe.getPlanet().getId().getValue(), output.planetId());
        assertEquals(probe.getPlanet(), planet);
        assertEquals(probe.getCreatedAt(), output.createdAt());
        assertEquals(probe.getUpdatedAt(), output.updatedAt());

        verify(gateway).findBy(eq(id));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetPlanet_shouldThrowsNotFoundException() {
        final var id = ProbeID.from(123L);
        final var expectedErrorMessage = "Probe with ID 123 was not found";

        when(gateway.findBy(id)).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(id.getValue()));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(gateway).findBy(eq(id));
    }
}