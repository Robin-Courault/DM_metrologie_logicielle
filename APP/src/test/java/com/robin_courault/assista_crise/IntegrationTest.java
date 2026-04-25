package com.robin_courault.assista_crise;

import com.robin_courault.assista_crise.config.AsyncSyncConfiguration;
import com.robin_courault.assista_crise.config.EmbeddedSQL;
import com.robin_courault.assista_crise.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        AssistaCriseApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.robin_courault.assista_crise.config.JacksonHibernateConfiguration.class,
    }
)
@EmbeddedSQL
public @interface IntegrationTest {}
