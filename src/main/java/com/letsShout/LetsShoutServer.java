package com.letsShout;


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
import com.letsShout.client.TwitterClient;
import com.letsShout.client.TwitterClientCache;

import java.util.concurrent.CompletionStage;

/**
 * Shout service main class
 */
public class LetsShoutServer extends AllDirectives {

    // set up ActorSystem and other dependencies here
    private final LetsShoutServerRoutes letsShoutServerRoutes;

    public LetsShoutServer(ActorSystem system, ActorRef actorRegistry, TwitterClientCache twitterClientCache) {
        letsShoutServerRoutes = new LetsShoutServerRoutes(system, actorRegistry, twitterClientCache);
    }

    public static void main(String[] args) throws Exception {

        // boot up server using themvn  route as defined below
        ActorSystem system = ActorSystem.create("letsShoutServer");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        ActorRef actorRef = system.actorOf(ActorRegistry.props(), "actorRegistry");

        TwitterClient twitterClient = new TwitterClient();
        TwitterClientCache twitterClientCache = new TwitterClientCache(twitterClient);

        //In order to access all directives we need an instance where the routes are define.
        LetsShoutServer app = new LetsShoutServer(system, actorRef, twitterClientCache);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("LetsShoutServer online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return

        binding
                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    /**
     * Here you can define all the different routes you want to have served by this web server
     * Note that routes might be defined in separated classes like the current case
     */
    protected Route createRoute() {
        return letsShoutServerRoutes.routes();
    }
}


