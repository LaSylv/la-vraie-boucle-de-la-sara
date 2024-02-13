package com.github.lasylv.lavraieboucledelasara.strava;

import com.github.lasylv.lavraieboucledelasara.runs.RunDto;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class StravaApiService {

    public static final String WWW_STRAVA_COM_ACTIVITIES = "https://www.strava.com/activities/";
    private final OAuth2AuthorizedClientService authorizedClientService;

    public StravaApiService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public RunDto getActivityFromRemoteService(String urlOrId) {

        String activityId = extractId(urlOrId);
        // Get the OAuth2AuthorizedClient for the user
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), principal.getName());
        // Make an authenticated request to the Strava API
        RestTemplate restTemplate = new RestTemplate();


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ActivityRecord> response = restTemplate.exchange(
                "https://www.strava.com/api/v3/activities/" + activityId + "?include_all_efforts=true",
                HttpMethod.GET,
                entity,
                ActivityRecord.class);

        if( response.getStatusCode()!= HttpStatus.OK) {
            // TODO exception
        }

        // TODO Save more informations

        ActivityRecord body = response.getBody();

        // TODO validate start point
        // TODO Find a way to validate bro didnt stop too early
        return new RunDto(body.startDate().toLocalDate().toString(), getNumberOfHours(body), "https://www.strava.com/activities/" + activityId, countLaps(body.segmentEfforts()), principal.getAttribute("firstname"), principal.getAttribute("lastname"), principal.getAttribute("profile_medium"));
    }

    private static String getNumberOfHours(ActivityRecord body) {
        // TODO Should be round
        return (body.elapsedTime() + 1000) / 3600 + "h";
    }

    private String extractId(String urlOrId) {
        // TODO handle errors
        // https://www.strava.com/activities/10733645107
        String activityId = null;
        try {
            activityId = Long.parseLong(urlOrId.replaceAll(WWW_STRAVA_COM_ACTIVITIES, "")) + "";
            if (urlOrId.startsWith(WWW_STRAVA_COM_ACTIVITIES)) {
                return activityId;
            }
        } catch (NumberFormatException e) {
            throw new InvalidStravaActivity(e, "Impossible de récupérer l'activité Strava à partir de l'url, est-elle bien configuré");
        }
        return activityId;
    }

    private long countLaps(List<ActivityRecord.SegmentEffort> segmentEfforts) {
        // TODO Should be one
        return segmentEfforts.stream()
                .filter(segmentEffort -> segmentEffort.segment().id() == 32572047)
                .count();
    }
}