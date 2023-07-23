package com.study.infrastructure.api.controllers;

import com.study.application.probe.create.CreateProbeCommand;
import com.study.application.probe.create.CreateProbeOutput;
import com.study.application.probe.create.CreateProbeUseCase;
import com.study.application.probe.delete.DeleteProbeUseCase;
import com.study.application.probe.move.MoveProbeUseCase;
import com.study.application.probe.retrieve.get.GetProbeByIdUseCase;
import com.study.application.probe.retrieve.list.ListProbeUseCase;
import com.study.application.probe.update.UpdateProbeCommand;
import com.study.application.probe.update.UpdateProbeOutput;
import com.study.application.probe.update.UpdateProbeUseCase;
import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.infrastructure.api.ProbeAPI;
import com.study.infrastructure.probe.models.CreateProbeRequest;
import com.study.infrastructure.probe.models.MoveProbeRequest;
import com.study.infrastructure.probe.models.ProbeListResponse;
import com.study.infrastructure.probe.models.ProbeResponse;
import com.study.infrastructure.probe.models.UpdateProbeRequest;
import com.study.infrastructure.probe.presenters.ProbeApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ProbeController implements ProbeAPI {
    private final CreateProbeUseCase createProbeUseCase;
    private final DeleteProbeUseCase deleteProbeUseCase;
    private final GetProbeByIdUseCase getProbeByIdUseCase;
    private final ListProbeUseCase listProbeUseCase;
    private final UpdateProbeUseCase updateProbeUseCase;
    private final MoveProbeUseCase moveProbeUseCase;

    public ProbeController(
            final CreateProbeUseCase createProbeUseCase,
            final DeleteProbeUseCase deleteProbeUseCase,
            final GetProbeByIdUseCase getProbeByIdUseCase,
            final ListProbeUseCase listProbeUseCase,
            final UpdateProbeUseCase updateProbeUseCase,
            final MoveProbeUseCase moveProbeUseCase) {
        this.createProbeUseCase = createProbeUseCase;
        this.deleteProbeUseCase = deleteProbeUseCase;
        this.getProbeByIdUseCase = getProbeByIdUseCase;
        this.listProbeUseCase = listProbeUseCase;
        this.updateProbeUseCase = updateProbeUseCase;
        this.moveProbeUseCase = moveProbeUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateProbeRequest input) {
        final CreateProbeCommand command = CreateProbeCommand.with(
            input.name(), input.cordX(), input.cordY(), input.planetId()
        );

        final CreateProbeOutput output = this.createProbeUseCase.execute(command);

        return ResponseEntity.created(URI.create("/probes/" + output.id())).body(output);
    }

    @Override
    public Pagination<ProbeListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        return this.listProbeUseCase.execute(new SearchQuery(page, perPage, search, sort, direction)).map(ProbeApiPresenter::present);
    }

    @Override
    public ProbeResponse getById(final Long id) {
        return ProbeApiPresenter.present(this.getProbeByIdUseCase.execute(id));
    }

    @Override
    public void deleteById(final Long id) {
        this.deleteProbeUseCase.execute(id);
    }

    @Override
    public ResponseEntity<?> updateById(final Long id, final UpdateProbeRequest input) {
        final UpdateProbeCommand command = UpdateProbeCommand.with(
            id, input.name(), input.cordX(), input.cordY(), input.direction()
        );

        final UpdateProbeOutput output = this.updateProbeUseCase.execute(command);

        return ResponseEntity.ok(output);
    }

    @Override
    public ResponseEntity<?> move(final Long id, final MoveProbeRequest input) {
        return null;
    }
}
