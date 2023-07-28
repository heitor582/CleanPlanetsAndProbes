package com.study.domain.probe;

import com.study.domain.UnitTest;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProbeTest extends UnitTest {
    @Test
    public void givenAValidParams_whenCallsNewProbe_thenInstantiateIt() {
        final String expectedName = "teste";
        final int expectedCordX = 5;
        final int expectedCordY = 3;
        final Direction expectedDirection = Direction.UP;
        final Planet planet = Planet.newPlanet(3, 5, "tes");

        final var probe = Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet.getId());

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(expectedDirection, probe.getDirection());
        assertEquals(planet.getId(), probe.getPlanetId());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());
        assertEquals(probe.getCreatedAt(), probe.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallsNewProbe_shouldReceivedANotificationException(){
        final String expectedName = null;
        final int expectedCordX = 3;
        final int expectedCordY = 3;
        final Planet planet = Planet.newPlanet(5, 5, "tes");

        final var expectedErrorMessage = "name should not be null";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet.getId()));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallsNewProbe_shouldReceivedANotificationException(){
        final String expectedName = "";
        final int expectedCordX = 3;
        final int expectedCordY = 3;
        final Planet planet = Planet.newPlanet(5, 5, "tes");

        final var expectedErrorMessage = "name should not be empty";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet.getId()));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameWithMoreThan255ValidChars_whenCallsNewProbe_shouldReceivedANotificationException(){
        final String expectedName = """
                Lorem ipsum dolor sit amet. Et cumque unde ex sint quis hic aliquid veritatis aut soluta quia et aperiam dignissimos aut error iusto. Est dolor dolore cum asperiores debitis eos voluptatem ipsum eum sint officia qui sunt ducimus. Aut voluptate quia eos laboriosam repellendus et quisquam nulla a doloribus sunt. Ex facere cumque ut laudantium voluptas ut culpa aspernatur nam rerum tenetur.
                                
                Et omnis error At adipisci quia qui velit optio ut repudiandae assumenda aut fugit iure eos numquam eligendi. Ut sint aperiam est praesentium eius aut voluptatem ipsa et enim aliquam a voluptates harum sed Quis quia aut minus eius. Aut quidem sunt qui dolor voluptas sit magnam porro ex blanditiis omnis est sequi voluptatem.
                                
                Qui ullam quas ut ipsa corrupti sed galisum Quis sed rerum placeat non quas nostrum vel illum atque ea animi provident. Sed obcaecati aliquam qui eveniet voluptatem ea enim voluptatem ex similique dolorem. Ut doloremque fugit est quidem eligendi eos voluptatem quos cum corrupti aspernatur aut doloribus odit non commodi saepe sed perferendis eius.
                """;
        final int expectedCordX = 3;
        final int expectedCordY = 3;
        final Planet planet = Planet.newPlanet(5, 5, "tes");

        final var expectedErrorMessage = "name must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet.getId()));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameWith2Chars_whenCallsNewProbe_shouldReceivedANotificationException(){
        final String expectedName = "a2 ";
        final int expectedCordX = 3;
        final int expectedCordY = 3;
        final Planet planet = Planet.newPlanet(5, 5, "tes");

        final var expectedErrorMessage = "name must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet.getId()));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidParams_whenCallsUpdateProbe_shouldReturnItUpdated() {
        final String expectedName = "teste";
        final int expectedCordX = 3;
        final int expectedCordY = 3;
        final Planet planet = Planet.newPlanet(3, 3, "tes");
        final Direction direction = Direction.DOWN;

        final var probe = Probe.newProbe("tes", 1, 2, planet.getId());
        final var expectedCreatedAt = probe.getCreatedAt();
        final var expectedUpdatedAt = probe.getUpdatedAt();

        probe.update(expectedName, expectedCordX, expectedCordY, direction);

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(planet.getId(), probe.getPlanetId());
        assertEquals(direction, probe.getDirection());
        assertEquals(expectedCreatedAt, probe.getCreatedAt());
        assertTrue(probe.getUpdatedAt().isAfter(expectedUpdatedAt));
    }

    @Test
    public void givenAValidProbe_whenCallsUpdateProbeWithNullName_shouldReturnANotificationException() {
        final String expectedName = null;
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final Planet planet = Planet.newPlanet(3, 3, "tes");
        final Direction direction = Direction.DOWN;

        final var expectedErrorMessage =  "name should not be null";
        final var expectedErrorCount = 1;

        final var probe = Probe.newProbe("tes", 1, 2, planet.getId());

        final var exception = assertThrows(NotificationException.class,
                () -> probe.update(expectedName, expectedCordX, expectedCordY, direction));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidProbe_whenCallsUpdateProbeWithNullDirection_shouldReturnANotificationException() {
        final String expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final Planet planet = Planet.newPlanet(3, 3, "tes");
        final Direction direction = null;

        final var expectedErrorMessage =  "direction should not be null";
        final var expectedErrorCount = 1;

        final var probe = Probe.newProbe("tes", 1, 2, planet.getId());

        final var exception = assertThrows(NotificationException.class,
                () -> probe.update(expectedName, expectedCordX, expectedCordY, direction));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidProbeWithNegativeCords_whenCallsCreateProbe_shouldReturnIt() {
        final String expectedName = "teste";
        final var expectedCordX = -3;
        final var expectedCordY = -3;
        final Planet planet = Planet.newPlanet(3, 3, "tes");
        final Direction expectedDirection = Direction.UP;

        final var probe = Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet.getId());

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(expectedDirection, probe.getDirection());
        assertEquals(planet.getId(), probe.getPlanetId());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());
        assertEquals(probe.getCreatedAt(), probe.getUpdatedAt());
    }
}
