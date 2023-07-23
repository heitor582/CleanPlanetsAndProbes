package com.study.infrastructure.planet.presenters;

import com.study.application.planet.retrieve.get.PlanetOutput;
import com.study.application.planet.retrieve.list.PlanetListOutput;
import com.study.infrastructure.planet.models.PlanetListResponse;
import com.study.infrastructure.planet.models.PlanetResponse;

public interface PlanetApiPresenter {
    static PlanetResponse present(final PlanetOutput output){
        return new PlanetResponse(
                output.id(),
                output.name(),
                output.cordX(),
                output.cordY(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    static PlanetListResponse present(final PlanetListOutput output){
        return new PlanetListResponse(
                output.id(),
                output.name(),
                output.cordX(),
                output.cordY(),
                output.createdAt()
        );
    }
}
