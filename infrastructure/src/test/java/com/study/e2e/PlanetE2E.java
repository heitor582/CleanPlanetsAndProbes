package com.study.e2e;

import com.study.E2ETest;
import com.study.application.planet.create.CreatePlanetUseCase;
import com.study.application.planet.delete.DeletePlanetUseCase;
import com.study.application.planet.retrieve.get.GetPlanetByIdUseCase;
import com.study.application.planet.retrieve.list.ListPlanetUseCase;
import com.study.application.planet.update.UpdatePlanetUseCase;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetID;
import com.study.infrastructure.planet.models.CreatePlanetRequest;
import com.study.infrastructure.planet.models.UpdatePlanetRequest;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlanetE2E extends E2ETest {
    @Autowired
    private PlanetRepository repository;
    @SpyBean
    private CreatePlanetUseCase createPlanetUseCase;
    @SpyBean
    private GetPlanetByIdUseCase getPlanetByIdUseCase;
    @SpyBean
    private ListPlanetUseCase listPlanetUseCase;
    @SpyBean
    private UpdatePlanetUseCase updatePlanetUseCase;
    @SpyBean
    private DeletePlanetUseCase deletePlanetUseCase;

    private final static String API_URL = "/planets";

    @Test
    public void givenAValidCommand_whenCallsCreatePlanet_shouldReturnItsIdentifier() throws Exception {
        //given
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;

        final var command = new CreatePlanetRequest(expectedName, expectedCordX, expectedCordY);

        //then
        final var request = MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        final var expectedId = repository.findAll().get(0).getId();

        //then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(header().string("Location", "%s/%s".formatted(API_URL,expectedId)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.intValue())));

        verify(createPlanetUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCordX, cmd.cordX())
                        && Objects.equals(expectedCordY, cmd.cordY())
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
    public void givenAnInvalidRequest_whenCallsCreatePlanet_shouldReturnNotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ) throws Exception {
        //given
        final var command = new CreatePlanetRequest(expectedName, expectedCordX, expectedCordY);

        //then
        final var request = MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        //then
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createPlanetUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCordX, cmd.cordX())
                        && Objects.equals(expectedCordY, cmd.cordY())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetPlanetById_shouldReturnIt() throws Exception{
        //given
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;

        final var planet = Planet.newPlanet(expectedCordY, expectedCordX, expectedName);
        final var expectedId = repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId();

        //when
        final var request = MockMvcRequestBuilders.get("%s/{id}".formatted(API_URL), expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.intValue())))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.cord_x", equalTo(expectedCordX)))
                .andExpect(jsonPath("$.cord_y", equalTo(expectedCordY)))
                .andExpect(jsonPath("$.created_at", equalTo(planet.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(planet.getUpdatedAt().toString())));

        verify(getPlanetByIdUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetPlanetById_shouldReturnNotFound() throws Exception{
        //given
        final var expectedId = PlanetID.from(1L);
        final var expectedErrorMessage = "Planet with ID %s was not found".formatted(expectedId.getValue());

         //when
        final var request = MockMvcRequestBuilders.get("%s/{id}".formatted(API_URL), expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        //then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getPlanetByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdatePlanet_shouldReturnPlanetId() throws Exception {
        //given
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;

        final var planet = Planet.newPlanet(expectedCordY, expectedCordX, expectedName);
        final var expectedId = repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId();

        final var command = new UpdatePlanetRequest(expectedName, expectedCordX, expectedCordY);

        // when
        final var request = put("%s/{id}".formatted(API_URL), expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        //then
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.intValue())));

        verify(updatePlanetUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCordX, cmd.cordX())
                        && Objects.equals(expectedCordY, cmd.cordY())
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
    public void givenAInvalidRequest_whenCallsUpdatePlanet_NotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ) throws Exception{
        //given
        final var planet = Planet.newPlanet(3, 3, "teste");
        final var expectedId = repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId();

        final var command = new UpdatePlanetRequest(expectedName, expectedCordX, expectedCordY);

        // when
        final var request = put("%s/{id}".formatted(API_URL), expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        //then
        response
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updatePlanetUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCordX, cmd.cordX())
                        && Objects.equals(expectedCordY, cmd.cordY())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeletePlanetById_shouldBeOk() throws Exception {
        //given
        final var planet = Planet.newPlanet(3, 3, "teste");
        final var expectedId = repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId();

        //when
        final var request = delete("%s/{id}".formatted(API_URL), expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        // then

        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(deletePlanetUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListPlanets_shouldReturnCategories() throws Exception {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "tes";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;
        final var planet = repository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(3, 3, "teste")));

        final var request = MockMvcRequestBuilders.get(API_URL)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedTotal)))
                .andExpect(jsonPath("$.items[0].id", equalTo(planet.getId().intValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(planet.getName())))
                .andExpect(jsonPath("$.items[0].cord_x", equalTo(planet.getCordX())))
                .andExpect(jsonPath("$.items[0].cord_y", equalTo(planet.getCordY())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(planet.getCreatedAt().toString())));

        verify(listPlanetUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
