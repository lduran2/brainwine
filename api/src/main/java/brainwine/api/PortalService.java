package brainwine.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import brainwine.api.handlers.SimpleExceptionHandler;
import brainwine.api.handlers.ZoneSearchHandler;
import brainwine.shared.JsonHelper;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

/**
 * aka Zone Searcher
 */
public class PortalService {
    
    private static final Logger logger = LogManager.getLogger();
    private final Javalin portal;
    
    public PortalService(Api api, int port) {
        logger.info("Starting PortalService @ port {} ...", port);
        DataFetcher dataFetcher = api.getDataFetcher();
        portal = Javalin.create(config -> config.jsonMapper(new JavalinJackson(JsonHelper.MAPPER))).start(port);
        portal.exception(Exception.class, new SimpleExceptionHandler());
        portal.get("/v1/worlds", new ZoneSearchHandler(dataFetcher));
    }
    
    public void stop() {
        portal.stop();
    }
}
