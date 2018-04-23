package com.lightbend.akka.http.sample;

//#test-top

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import com.client.TwitterClient;
import org.junit.Before;
import org.junit.Test;

//#set-up
public class SearchTweetTest extends JUnitRouteTest {
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

    }
}
//#set-up
