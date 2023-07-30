package com.study.application.probe.create;

import com.study.application.UseCaseTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import com.study.domain.probe.Direction;
import com.study.domain.probe.ProbeGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateProbeUseCaseTest extends UseCaseTest {
    @Mock
    private PlanetGateway planetGateway;
    @Mock
    private ProbeGateway probeGateway;
    @InjectMocks
    private DefaultCreateProbeUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(planetGateway, planetGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateProbe_shouldReturnIt() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedPlanet = Planet.newPlanet(5,5,expectedName);
        final var planetId = expectedPlanet.getId();

        final var command = CreateProbeCommand.with(expectedName, expectedCordX, expectedCordY, planetId.getValue());

        when(planetGateway.findBy(planetId)).thenReturn(Optional.of(expectedPlanet));
        when(probeGateway.create(any())).thenAnswer(returnsFirstArg());

        useCase.execute(command);

        verify(probeGateway).create(argThat(probe ->
                Objects.nonNull(probe.getId())
                        && Objects.equals(expectedName, probe.getName())
                        && Objects.equals(expectedCordX, probe.getCordX())
                        && Objects.equals(expectedCordY, probe.getCordY())
                        && Objects.equals(Direction.UP, probe.getDirection())
                        && Objects.equals(expectedPlanet, probe.getPlanet())
                        && Objects.equals(expectedPlanet.getId(), probe.getPlanet().getId())
                        && Objects.nonNull(probe.getCreatedAt())
                        && Objects.nonNull(probe.getUpdatedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithANonExistencePlanetId_whenCallsCreateProbe_shouldReturnNotFoundException() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var planetId = PlanetID.from(1L);
        final var expectedErrorMessage = "Planet with ID 1 was not found";
        final var command = CreateProbeCommand.with(expectedName, expectedCordX, expectedCordY, planetId.getValue());

        when(planetGateway.findBy(planetId)).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(probeGateway, times(0)).create(any());
    }

    @ParameterizedTest
    @CsvSource({
            ",name should not be null",
            "'',name should not be empty",
            "aa,name must be between 3 and 255 characters",
            " a,name must be between 3 and 255 characters",
    })
    public void givenAnInvalidCommandWithInvalidName_whenCallsCreateProbe_shouldReturnNotificationException(
            final String name,
            final String errorMessage
    ) {
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedPlanet = Planet.newPlanet(5,5,"teste");
        final var planetId = expectedPlanet.getId();
        final var expectedErrorCount = 1;
        final var command = CreateProbeCommand.with(name, expectedCordX, expectedCordY, planetId.getValue());

        when(planetGateway.findBy(planetId)).thenReturn(Optional.of(expectedPlanet));

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.getErrors().get(0).message());

        verify(probeGateway, times(0)).create(any());
    }
}