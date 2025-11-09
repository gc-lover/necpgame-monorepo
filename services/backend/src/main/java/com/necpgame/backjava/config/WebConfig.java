package com.necpgame.backjava.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * РљРѕРЅС„РёРіСѓСЂР°С†РёСЏ Web MVC
 * РќР°СЃС‚СЂРѕР№РєР° CORS РґР»СЏ С„СЂРѕРЅС‚РµРЅРґР°
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",      // React dev server
                "http://localhost:3001",      // Vite dev server (Р°Р»СЊС‚РµСЂРЅР°С‚РёРІРЅС‹Р№ РїРѕСЂС‚)
                "http://localhost:5173",      // Vite dev server
                "http://localhost:5174",      // Vite dev server (Р°Р»СЊС‚РµСЂРЅР°С‚РёРІРЅС‹Р№ РїРѕСЂС‚)
                "http://127.0.0.1:3000",      // React dev server (127.0.0.1)
                "http://127.0.0.1:3001",      // Vite dev server (127.0.0.1)
                "http://127.0.0.1:5173",      // Vite dev server (127.0.0.1)
                "http://127.0.0.1:5174"       // Vite dev server (127.0.0.1)
            )
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);  // РљРµС€ preflight Р·Р°РїСЂРѕСЃРѕРІ РЅР° 1 С‡Р°СЃ
    }
}

