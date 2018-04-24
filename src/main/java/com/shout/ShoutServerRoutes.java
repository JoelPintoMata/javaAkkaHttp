package com.shout;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.shout.client.TwitterClient;
import com.shout.model.MessageRegistry;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * Shout service routes
 */
//#user-routes-class
public class ShoutServerRoutes extends AllDirectives {

    //#user-routes-class
    private final ActorRef actorRef;
    private final LoggingAdapter log;
    private final TwitterClient twitterClient;

    public ShoutServerRoutes(ActorSystem system, ActorRef actorRef, TwitterClient twitterClient) {
        log = Logging.getLogger(system, this);

        this.actorRef = actorRef;
        this.twitterClient = twitterClient;
    }

    // Required by the `ask` (?) method below
    Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS)); // usually we'd obtain the timeout from the system's configuration

    /**
     * This method creates one route (of possibly many more that will be part of your Web App)
     */
    //#all-routes
    public Route routes() {
        return route(pathPrefix("shout", () ->
                route(
                        path(PathMatchers.segments(2), segments ->
                                route(
                                    getSearchTweets(segments.get(0), segments.get(1))
                                )
                        )
                )
        ));
    }
    //#all-routes

    //#searchTweets-get
    private Route getSearchTweets(String username, String n) {
        return
                //#searchTweets-logic
                get(() -> {
                    CompletionStage<Optional<MessageRegistry.SearchTweets>> completionStage = PatternsCS
                            .ask(actorRef, new MessageRegistry.SearchTweets(username, n, twitterClient), timeout)
                            .thenApply(obj -> (Optional<MessageRegistry.SearchTweets>) obj);
                    return onSuccess(() -> completionStage,
                            searchTweets -> complete(
                                    StatusCodes.OK,
                                    searchTweets.get(),
                                    Jackson.marshaller()));
                });
                //#searchTweets-logic
    }
    //#searchTweets-get
}
