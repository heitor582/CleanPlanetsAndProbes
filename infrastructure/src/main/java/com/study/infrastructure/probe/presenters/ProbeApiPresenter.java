package com.study.infrastructure.probe.presenters;

import com.study.application.probe.retrieve.get.ProbeOutput;
import com.study.application.probe.retrieve.list.ProbeListOutput;
import com.study.infrastructure.probe.models.ProbeListResponse;
import com.study.infrastructure.probe.models.ProbeResponse;

public interface ProbeApiPresenter {
    static ProbeResponse present(final ProbeOutput output) {
        return new ProbeResponse(
                output.id(),
                output.name(),
                output.cordX(),
                output.cordY(),
                output.direction(),
                output.planetId(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    static ProbeListResponse present(final ProbeListOutput output){
        return new ProbeListResponse(
                output.id(),
                output.name(),
                output.cordX(),
                output.cordY(),
                output.direction(),
                output.planetId(),
                output.createdAt(),
                output.updatedAt()
        );
    }
}
