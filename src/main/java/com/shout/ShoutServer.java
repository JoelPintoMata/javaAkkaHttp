package com.shout;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.shout.client.TwitterClient;

import java.util.concurrent.CompletionStage;

/**
 * Shout service main class
 */
//#main-class
public class ShoutServer extends AllDirectives {

    // set up ActorSystem and other dependencies here
    private final ShoutServerRoutes shoutServerRoutes;

    public ShoutServer(ActorSystem system, ActorRef actorRegistry, TwitterClient twitterClient) {
        shoutServerRoutes = new ShoutServerRoutes(system, actorRegistry, twitterClient);
    }
    //#main-class

    public static void main(String[] args) throws Exception {

        //#server-bootstrapping
        // boot up server using themvn  route as defined below
        ActorSystem system = ActorSystem.create("shoutServer");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        //#server-bootstrapping

        ActorRef userRegistryActor = system.actorOf(ActorRegistry.props(), "actorRegistry");

        //#http-server
        //In order to access all directives we need an instance where the routes are define.
        ShoutServer app = new ShoutServer(system, userRegistryActor, new TwitterClient());

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("ShoutServer online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return

        binding
                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
        //#http-server
    }

    //#main-class

    /**
     * Here you can define all the different routes you want to have served by this web server
     * Note that routes might be defined in separated classes like the current case
     */
    protected Route createRoute() {
        return shoutServerRoutes.routes();
    }
}
//#main-class


