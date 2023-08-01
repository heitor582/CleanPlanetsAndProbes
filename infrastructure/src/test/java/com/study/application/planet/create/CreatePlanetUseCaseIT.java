package com.study.application.planet.create;

import com.study.IntegrationTest;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.PlanetGateway;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CreatePlanetUseCaseIT extends IntegrationTest {
    @SpyBean
    private PlanetGateway gateway;
    @Autowired
    private CreatePlanetUseCase useCase;
    @Autowired
    private PlanetRepository repository;

    @Test
    public void givenAValidCommand_whenCallsCreatePlanet_shouldReturnIt() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;

        final var command = CreatePlanetCommand.with(expectedName, expectedCordX, expectedCordY);

        final var output = useCase.execute(command);

        final var planet = repository.findById(output.id()).get();

        assertNotNull(planet.getId());
        assertEquals(expectedName, planet.getName());
        assertEquals(expectedCordX, planet.getCordX());
        assertEquals(expectedCordY, planet.getCordY());
        assertEquals(planet.getCreatedAt(), planet.getUpdatedAt());
        assertNotNull(planet.getCreatedAt());
        assertNotNull(planet.getUpdatedAt());

        verify(gateway).create(any());
    }

    @Test
    public void givenAInValidName_whenCallsCreatePlanet_shouldThrowsNotificationException() {
        final String expectedName = null;
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedErrorMessage = "name should not be null";
        final var expectedErrorCount = 1;

        final var command = CreatePlanetCommand.with(expectedName, expectedCordX, expectedCordY);

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidCordX_whenCallsCreatePlanet_shouldThrowsNotificationException() {
        final var expectedName = "teste";
        final var expectedCordX = 1001;
        final var expectedCordY = 3;
        final var expectedErrorMessage = "coordinate X must be between 1 and 1000";
        final var expectedErrorCount = 1;

        final var command = CreatePlanetCommand.with(expectedName, expectedCordX, expectedCordY);

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidCordY_whenCallsCreatePlanet_shouldThrowsNotificationException() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 0;
        final var expectedErrorMessage = "coordinate Y must be between 1 and 1000";
        final var expectedErrorCount = 1;

        final var command = CreatePlanetCommand.with(expectedName, expectedCordX, expectedCordY);

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway, times(0)).create(any());
    }
}