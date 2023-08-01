package com.study.e2e;

import com.study.E2ETest;
import com.study.application.probe.create.CreateProbeUseCase;
import com.study.application.probe.delete.DeleteProbeUseCase;
import com.study.application.probe.move.MoveProbeUseCase;
import com.study.application.probe.retrieve.get.GetProbeByIdUseCase;
import com.study.application.probe.retrieve.list.ListProbeUseCase;
import com.study.application.probe.update.UpdateProbeUseCase;
import org.springframework.boot.test.mock.mockito.SpyBean;

public class ProbeE2E extends E2ETest {
    @SpyBean
    private DeleteProbeUseCase deleteProbeUseCase;
    @SpyBean
    private CreateProbeUseCase defaultCreateProbeUseCase;
    @SpyBean
    private MoveProbeUseCase moveProbeUseCase;
    @SpyBean
    private ListProbeUseCase listProbeUseCase;
    @SpyBean
    private GetProbeByIdUseCase getProbeByIdUseCase;
    @SpyBean
    private UpdateProbeUseCase updateProbeUseCase;
}
