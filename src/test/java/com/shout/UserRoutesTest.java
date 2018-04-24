package com.shout;


//#test-top

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import com.shout.client.TwitterClient;
import org.junit.Before;
import org.junit.Test;


//#set-up
public class UserRoutesTest extends JUnitRouteTest {
    //#test-top
    private TestRoute appRoute;

    //#set-up
    @Before
    public void initClass() {
        ActorSystem system = ActorSystem.create("helloAkkaHttpServer");
        ActorRef userRegistryActor = system.actorOf(RegistryActor.props(), "userRegistryActor");
        ShoutServiceServer server = new ShoutServiceServer(system, userRegistryActor, new TwitterClient());
        appRoute = testRoute(server.createRoute());
    }

    @Test
    public void testGETNoUsernameNoN() {
        appRoute.run(HttpRequest.GET("/shout"))
                .assertStatusCode(StatusCodes.NOT_FOUND)
                .assertMediaType("text/plain")
                .assertEntity("The requested resource could not be found.");
    }

    //#actual-test
    //#testing-put
    @Test
    public void testPUTUsernameNoN() {
        appRoute.run(HttpRequest.PUT("/shout/ggreenwald"))
                .assertStatusCode(StatusCodes.NOT_FOUND)
                .assertMediaType("text/plain")
                .assertEntity("The requested resource could not be found.");
    }
    //#testing-put

    //#actual-test
    //#testing-post
    @Test
    public void testPOSTNoUsernameNoN() {
        appRoute.run(HttpRequest.PUT("/shout"))
                .assertStatusCode(StatusCodes.NOT_FOUND)
                .assertMediaType("text/plain")
                .assertEntity("The requested resource could not be found.");
    }
    //#testing-post

    //#actual-test
    //#testing-delete
    @Test
    public void testDELETENoUsernameNoN() {
        appRoute.run(HttpRequest.PUT("/shout"))
                .assertStatusCode(StatusCodes.NOT_FOUND)
                .assertMediaType("text/plain")
                .assertEntity("The requested resource could not be found.");
    }
    //#testing-delete
}
//#set-up
