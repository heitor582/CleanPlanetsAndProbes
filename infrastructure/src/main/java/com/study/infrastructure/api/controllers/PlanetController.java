package com.study.infrastructure.api.controllers;

import com.study.application.planet.create.CreatePlanetCommand;
import com.study.application.planet.create.CreatePlanetOutput;
import com.study.application.planet.create.CreatePlanetUseCase;
import com.study.application.planet.delete.DeletePlanetUseCase;
import com.study.application.planet.retrieve.get.GetPlanetByIdUseCase;
import com.study.application.planet.retrieve.list.ListPlanetUseCase;
import com.study.application.planet.update.UpdatePlanetCommand;
import com.study.application.planet.update.UpdatePlanetUseCase;
import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.infrastructure.api.PlanetAPI;
import com.study.infrastructure.planet.models.CreatePlanetRequest;
import com.study.infrastructure.planet.models.PlanetListResponse;
import com.study.infrastructure.planet.models.PlanetResponse;
import com.study.infrastructure.planet.models.UpdatePlanetRequest;
import com.study.infrastructure.planet.presenters.PlanetApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class PlanetController implements PlanetAPI {
    private final CreatePlanetUseCase createPlanetUseCase;
    private final DeletePlanetUseCase deletePlanetUseCase;
    private final GetPlanetByIdUseCase getPlanetByIdUseCase;
    private final ListPlanetUseCase listPlanetUseCase;
    private final UpdatePlanetUseCase updatePlanetUseCase;

    public PlanetController(
            final CreatePlanetUseCase createPlanetUseCase,
            final DeletePlanetUseCase deletePlanetUseCase,
            final GetPlanetByIdUseCase getPlanetByIdUseCase,
            final ListPlanetUseCase listPlanetUseCase,
            final UpdatePlanetUseCase updatePlanetUseCase) {
        this.createPlanetUseCase = Objects.requireNonNull(createPlanetUseCase);
        this.deletePlanetUseCase = Objects.requireNonNull(deletePlanetUseCase);
        this.getPlanetByIdUseCase = Objects.requireNonNull(getPlanetByIdUseCase);
        this.listPlanetUseCase = Objects.requireNonNull(listPlanetUseCase);
        this.updatePlanetUseCase = Objects.requireNonNull(updatePlanetUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreatePlanetRequest input) {
        final CreatePlanetCommand command = CreatePlanetCommand.with(
                input.name(),
                input.cordX(),
                input.cordY()
        );

        final CreatePlanetOutput output = this.createPlanetUseCase.execute(command);

        return ResponseEntity.created(URI.create("/planets/" + output.id())).body(output);
    }

    @Override
    public Pagination<PlanetListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        return this.listPlanetUseCase.execute(new SearchQuery(page, perPage, search, sort, direction)).map(PlanetApiPresenter::present);
    }

    @Override
    public PlanetResponse getById(final Long id) {
        return PlanetApiPresenter.present(this.getPlanetByIdUseCase.execute(id));
    }

    @Override
    public void deleteById(final Long id) {
        this.deletePlanetUseCase.execute(id);
    }

    @Override
    public ResponseEntity<?> updateById(final Long id, final UpdatePlanetRequest input) {
        final UpdatePlanetCommand command = UpdatePlanetCommand.with(
                id,
                input.name(),
                input.cordX(),
                input.cordY()
        );

        final var output = this.updatePlanetUseCase.execute(command);

        return ResponseEntity.ok(output);
    }
}
