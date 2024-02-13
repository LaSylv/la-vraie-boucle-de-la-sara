package com.github.lasylv.lavraieboucledelasara.strava;

public class InvalidStravaActivity extends RuntimeException {
    private final String reason;

    public InvalidStravaActivity(NumberFormatException e, String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
