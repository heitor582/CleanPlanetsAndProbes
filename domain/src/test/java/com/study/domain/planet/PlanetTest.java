package com.study.domain.planet;

import com.study.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlanetTest {

    @Test
    public void givenAValidParams_whenCallsNewPlanet_thenInstantiateIt() {
        final var expectedName = "teste";
        final int expectedCordX = 5;
        final int expectedCordY = 3;

        final var planet = Planet.newPlanet(expectedCordY, expectedCordX, expectedName);

        assertNotNull(planet);
        assertNotNull(planet.getId());

        assertEquals(expectedName, planet.getName());
        assertEquals(expectedCordX, planet.getCordX());
        assertEquals(expectedCordY, planet.getCordY());
        assertNotNull(planet.getCreatedAt());
        assertNotNull(planet.getUpdatedAt());
        assertEquals(planet.getCreatedAt(), planet.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallsNewPlanet_shouldReceivedANotificationException(){
        final String expectedName = null;
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedErrorMessage = "name should not be null";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallsNewPlanet_shouldReceivedANotificationException(){
        final String expectedName = "";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedErrorMessage = "name should not be empty";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameWithMoreThan255ValidChars_whenCallsNewPlanet_shouldReceivedANotificationException(){
        final String expectedName = """
                Lorem ipsum dolor sit amet. Et cumque unde ex sint quis hic aliquid veritatis aut soluta quia et aperiam dignissimos aut error iusto. Est dolor dolore cum asperiores debitis eos voluptatem ipsum eum sint officia qui sunt ducimus. Aut voluptate quia eos laboriosam repellendus et quisquam nulla a doloribus sunt. Ex facere cumque ut laudantium voluptas ut culpa aspernatur nam rerum tenetur.
                                
                Et omnis error At adipisci quia qui velit optio ut repudiandae assumenda aut fugit iure eos numquam eligendi. Ut sint aperiam est praesentium eius aut voluptatem ipsa et enim aliquam a voluptates harum sed Quis quia aut minus eius. Aut quidem sunt qui dolor voluptas sit magnam porro ex blanditiis omnis est sequi voluptatem.
                                
                Qui ullam quas ut ipsa corrupti sed galisum Quis sed rerum placeat non quas nostrum vel illum atque ea animi provident. Sed obcaecati aliquam qui eveniet voluptatem ea enim voluptatem ex similique dolorem. Ut doloremque fugit est quidem eligendi eos voluptatem quos cum corrupti aspernatur aut doloribus odit non commodi saepe sed perferendis eius.
                """;
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedErrorMessage = "name must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameWith2Chars_whenCallsNewPlanet_shouldReceivedANotificationException(){
        final String expectedName = "a2 ";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedErrorMessage = "name must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCoordinateXLowerThan1_whenCallsNewPlanet_shouldReceivedANotificationException(){
        final var expectedName = "teste";
        final var expectedCordX = 0;
        final var expectedCordY = 3;
        final var expectedErrorMessage = "coordinate X must be between 1 and 1000";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCoordinateXGreaterThan1000_whenCallsNewPlanet_shouldReceivedANotificationException(){
        final var expectedName = "teste";
        final var expectedCordX = 1001;
        final var expectedCordY = 3;
        final var expectedErrorMessage = "coordinate X must be between 1 and 1000";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCoordinateYGreaterThan1000_whenCallsNewPlanet_shouldReceivedANotificationException(){
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 1001;
        final var expectedErrorMessage = "coordinate Y must be between 1 and 1000";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCoordinateYLowerThan1_whenCallsNewPlanet_shouldReceivedANotificationException(){
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 0;
        final var expectedErrorMessage = "coordinate Y must be between 1 and 1000";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidParams_whenCallsUpdatePlanet_shouldReturnItUpdated() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;

        final var planet = Planet.newPlanet(1, 1, "tes");
        final var expectedCreatedAt = planet.getCreatedAt();
        final var expectedUpdatedAt = planet.getUpdatedAt();

        planet.update(expectedCordY, expectedCordX, expectedName);

        assertNotNull(planet);
        assertNotNull(planet.getId());

        assertEquals(expectedName, planet.getName());
        assertEquals(expectedCordX, planet.getCordX());
        assertEquals(expectedCordY, planet.getCordY());
        assertEquals(expectedCreatedAt, planet.getCreatedAt());
        assertTrue(planet.getUpdatedAt().isAfter(expectedUpdatedAt));
    }

    @Test
    public void givenAValidPlanet_whenCallsUpdatePlanetWithNullName_shouldReturnANotificationException() {
        final String expectedName = null;
        final var expectedCordX = 3;
        final var expectedCordY = 3;

        final var expectedErrorMessage =  "name should not be null";
        final var expectedErrorCount = 1;

        final var planet = Planet.newPlanet(1, 1, "tes");

        final var exception = assertThrows(NotificationException.class,
                () -> planet.update(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }
}