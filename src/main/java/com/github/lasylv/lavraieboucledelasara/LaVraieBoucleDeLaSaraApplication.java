package com.github.lasylv.lavraieboucledelasara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
@EnableJdbcHttpSession
public class LaVraieBoucleDeLaSaraApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaVraieBoucleDeLaSaraApplication.class, args);
    }

}
