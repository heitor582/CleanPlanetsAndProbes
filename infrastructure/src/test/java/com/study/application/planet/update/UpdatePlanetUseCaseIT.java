package com.study.application.planet.update;

import com.study.IntegrationTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
class UpdatePlanetUseCaseIT {
    @SpyBean
    private PlanetGateway gateway;
    @Autowired
    private UpdatePlanetUseCase useCase;
    @Autowired
    private PlanetRepository repository;

    @Test
    public void givenAValidCommand_whenCallsUpdatePlanet_shouldUReturnIt(){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = PlanetID.from(repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId());
        final var expectedName = "teste1";
        final var expectedCordX = 2;
        final var expectedCordY = 2;
        final var expectedCreatedAt = planet.getCreatedAt();
        final var updatedAt = planet.getUpdatedAt();

        final var command = UpdatePlanetCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY);

        final var output = useCase.execute(command);
        final var planetFound = repository.findById(output.id()).get();

        assertEquals(output.id(), expectedId.getValue());

        assertEquals(expectedId.getValue(), planetFound.getId());
        assertEquals(expectedName, planetFound.getName());
        assertEquals(expectedCordX, planetFound.getCordX());
        assertEquals(expectedCordY, planetFound.getCordY());
        assertEquals(expectedCreatedAt, planetFound.getCreatedAt());
        assertTrue(planetFound.getUpdatedAt().isAfter(updatedAt));

        verify(gateway).update(any());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdatePlanet_shouldReturnNotification(){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = PlanetID.from(repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId());
        final String expectedName = null;
        final var expectedCordX = 2;
        final var expectedCordY = 2;
        final var expectedErrorMessage = "name should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdatePlanetCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY);

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway).findBy(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidCordX_whenCallsUpdatePlanet_shouldReturnNotification(){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = PlanetID.from(repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId());
        final var expectedName = "teste1";
        final var expectedCordX = 0;
        final var expectedCordY = 2;

        final var expectedErrorMessage = "coordinate X must be between 1 and 1000";
        final var expectedErrorCount = 1;

        final var command = UpdatePlanetCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY);

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway).findBy(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidCordY_whenCallsUpdatePlanet_shouldReturnNotification(){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = PlanetID.from(repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId());
        final var expectedName = "teste1";
        final var expectedCordX = 2;
        final var expectedCordY = 1002;

        final var expectedErrorMessage = "coordinate Y must be between 1 and 1000";
        final var expectedErrorCount = 1;

        final var command = UpdatePlanetCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY);

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway).findBy(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }


    @Test
    public void givenAValidCommand_whenCallsUpdatePlanetWithNonexistentId_shouldReturnNotFoundException (){
        final var expectedId = PlanetID.from(123L);
        final var expectedName = "teste1";
        final var expectedCordX = 2;
        final var expectedCordY = 2;

        final var expectedErrorMessage = "Planet with ID 123 was not found";

        final var command = UpdatePlanetCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY);

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(gateway).findBy(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }
}