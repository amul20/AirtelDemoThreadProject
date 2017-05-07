package org.airtel.project;

import org.airtel.project.connector.MySqlConnector;
import org.airtel.project.di.ServerDI;
import org.airtel.project.routes.TEAMRoutes;
import org.restexpress.RestExpress;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by amulyam on 06/05/17.
 */
public class TEAMServer {
    public static void main(String args[]) {
        final AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        try {
            appContext.register(ServerDI.class);
            appContext.refresh();
            MySqlConnector.getInstance();
            final RestExpress server = new RestExpress()
                    .setName("TEAM")
                    .setKeepAlive(true)
                    .setReuseAddress(true)
                    .setIoThreadCount(4)
                    .setExecutorThreadCount(20);
            TEAMRoutes.defineRoutes(server, appContext);
            server.setMaxContentSize(1024 * 1024 * 200);
            server.bind(8080);
            System.out.println("server binded on 8080");
            server.awaitShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
