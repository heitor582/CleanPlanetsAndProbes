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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UpdatePlanetUseCaseIT extends IntegrationTest {
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
    public void givenAnInvalidName_whenCallsUpdatePlanet_shouldReturnNotification(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = PlanetID.from(repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId());
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