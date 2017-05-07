package org.airtel.project.routes;

import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.RestExpress;
import org.springframework.context.ApplicationContext;

/**
 * Created by amulyam on 06/05/17.
 */

/**
 * Routes class which defines URI of the APIs
 * Additionally the API may contain parameters and Request headers
 */

public class TEAMRoutes {

    public static void defineRoutes(RestExpress server, ApplicationContext context){
        server.uri("/team/v1",context.getBean("teamController"))
                .noSerialization()
                .action("analyzeText", HttpMethod.POST)
                .action("getReport", HttpMethod.GET);
    }
}
