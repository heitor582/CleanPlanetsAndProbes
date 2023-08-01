package com.study.application.planet.create;

import com.study.application.UseCaseTest;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.PlanetGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreatePlanetUseCaseTest extends UseCaseTest {

    @Mock
    private PlanetGateway gateway;

    @InjectMocks
    private DefaultCreatePlanetUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreatePlanet_shouldReturnIt() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;

        final var command = CreatePlanetCommand.with(expectedName, expectedCordX, expectedCordY);

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        useCase.execute(command);

        verify(gateway).create(argThat(planet ->
                Objects.nonNull(planet.getId())
                        && Objects.equals(expectedName, planet.getName())
                        && Objects.equals(expectedCordX, planet.getCordX())
                        && Objects.equals(expectedCordY, planet.getCordY())
                        && Objects.nonNull(planet.getCreatedAt())
                        && Objects.nonNull(planet.getUpdatedAt())
        ));
    }

    @ParameterizedTest
    @CsvSource({
            ",3,3,name should not be null",
            "'',3,3,name should not be empty",
            "aa,3,3,name must be between 3 and 255 characters",
            " a,3,3,name must be between 3 and 255 characters",
            "tes,0,3,coordinate X must be between 1 and 1000",
            "tes,3,0,coordinate Y must be between 1 and 1000",
            "tes,1001,3,coordinate X must be between 1 and 1000",
            "tes,3,1001,coordinate Y must be between 1 and 1000",
    })
    public void givenAInValidName_whenCallsCreatePlanet_shouldThrowsNotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ) {
        final var expectedErrorCount = 1;

        final var command = CreatePlanetCommand.with(expectedName, expectedCordX, expectedCordY);

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway, times(0)).create(any());
    }
}