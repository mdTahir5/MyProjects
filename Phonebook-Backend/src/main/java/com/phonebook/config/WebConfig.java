package com.phonebook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC tuning.
 *
 * <p>
 * Only JSON/REST content is served from the API; there are no server-rendered
 * views. Error handling is delegated to {@code GlobalExceptionHandler} and the
 * Spring Security entry point, both of which write JSON bodies.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Prove the API owns no HTML views: every non-API path is left to the
     * framework, which returns the JSON 404 produced by the exception handler.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Intentionally empty - the frontend is a separate Vite dev server /
        // static bundle, so the backend serves no views.
    }
}
