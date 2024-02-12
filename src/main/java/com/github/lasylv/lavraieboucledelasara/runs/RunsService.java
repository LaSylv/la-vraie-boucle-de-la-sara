package com.github.lasylv.lavraieboucledelasara.runs;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RunsService {

    private final List<RunDto> runs = new ArrayList<>();

    public RunsService() {
    }

    public List<RunDto> getRuns() {
        return runs;
    }

    public void addRun(RunDto newRun) {
        // TODO check if existing
        // TODO database
        runs.add(newRun);

    }
}
