package com.letsShout;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import com.letsShout.client.TwitterClient;
import com.letsShout.client.TwitterClientCache;
import org.junit.Before;
import org.junit.Test;

public class LetsShoutServerRoutesTest extends JUnitRouteTest {

    private TestRoute appRoute;

    @Before
    public void initClass() {
        ActorSystem system = ActorSystem.create("letsShoutServer");
        ActorRef actorRef = system.actorOf(ActorRegistry.props(), "actorRegistry");
        TwitterClient twitterClient = new TwitterClient();
        TwitterClientCache twitterClientCache = new TwitterClientCache(twitterClient);
        LetsShoutServer server = new LetsShoutServer(system, actorRef, twitterClientCache);
        appRoute = testRoute(server.createRoute());
    }

    @Test
    public void testGETNoUsernameNoN() {
        appRoute.run(HttpRequest.GET("/letsShout"))
                .assertStatusCode(StatusCodes.NOT_FOUND)
                .assertMediaType("text/plain")
                .assertEntity("The requested resource could not be found.");
    }

    @Test
    public void testPUTUsernameNoN() {
        appRoute.run(HttpRequest.PUT("/letsShout/ggreenwald"))
                .assertStatusCode(StatusCodes.NOT_FOUND)
                .assertMediaType("text/plain")
                .assertEntity("The requested resource could not be found.");
    }

    @Test
    public void testPOSTNoUsernameNoN() {
        appRoute.run(HttpRequest.PUT("/letsShout"))
                .assertStatusCode(StatusCodes.NOT_FOUND)
                .assertMediaType("text/plain")
                .assertEntity("The requested resource could not be found.");
    }

    @Test
    public void testDELETENoUsernameNoN() {
        appRoute.run(HttpRequest.PUT("/letsShout"))
                .assertStatusCode(StatusCodes.NOT_FOUND)
                .assertMediaType("text/plain")
                .assertEntity("The requested resource could not be found.");
    }
}
