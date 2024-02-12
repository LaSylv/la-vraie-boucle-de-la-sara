package com.github.lasylv.lavraieboucledelasara.runs;

import com.github.lasylv.lavraieboucledelasara.strava.StravaApiService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RunsController {

    private final RunsService runsService;

    private final StravaApiService stravaApiService;

    public RunsController(RunsService runsService, StravaApiService stravaApiService) {
        this.runsService = runsService;
        this.stravaApiService = stravaApiService;
    }

    @HxRequest
    @GetMapping("/runs")
    public HtmxResponse getRuns()  {
        return buildRunsResponse();
    }

    private HtmxResponse buildRunsResponse() {
        return HtmxResponse.builder()
                .view(new ModelAndView("runs", "runs", runsService.getRuns()))
                .build();
    }

    @HxRequest
    @PostMapping(value = "/runs", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public HtmxResponse addRun(HtmxRequest htmxRequest, @RequestBody MultiValueMap<String, String> input)  {
        runsService.addRun(stravaApiService.getActivityFromRemoteService(input.getFirst("link")));
        return buildRunsResponse();
    }


}
