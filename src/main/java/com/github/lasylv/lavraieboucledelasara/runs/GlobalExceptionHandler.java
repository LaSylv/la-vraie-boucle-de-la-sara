package com.github.lasylv.lavraieboucledelasara.runs;

import com.github.lasylv.lavraieboucledelasara.strava.InvalidStravaActivity;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxReswap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex, WebRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        modelAndView.setViewName("error"); // error.html is your custom error template
        return modelAndView;
    }

    @ExceptionHandler(InvalidStravaActivity.class)
    public HtmxResponse handleSpecificException(InvalidStravaActivity ex, WebRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getReason());
        modelAndView.setViewName("add-run-error"); // specific-error.html is your template for this specific exception

        return HtmxResponse.builder()
                .reswap(HtmxReswap.innerHtml())
                .retarget("#add-run-error")
                .view(modelAndView)
                .build();
    }
}