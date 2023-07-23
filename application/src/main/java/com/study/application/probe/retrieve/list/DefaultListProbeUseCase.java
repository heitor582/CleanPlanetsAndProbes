package com.study.application.probe.retrieve.list;

import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.domain.probe.ProbeGateway;

import java.util.Objects;

public class DefaultListProbeUseCase extends ListProbeUseCase{
    private final ProbeGateway gateway;

    public DefaultListProbeUseCase(final ProbeGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<ProbeListOutput> execute(final SearchQuery query) {
        return this.gateway.findAll(query).map(ProbeListOutput::from);
    }
}
