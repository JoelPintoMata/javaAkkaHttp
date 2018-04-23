package com.lightbend.akka.http.sample;

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
import com.lightbend.akka.http.sample.UserRegistryActor.User;
import com.lightbend.akka.http.sample.UserRegistryMessages.ActionPerformed;
import com.lightbend.akka.http.sample.UserRegistryMessages.CreateUser;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * Routes can be defined in separated classes like shown in here
 */
//#user-routes-class
public class UserRoutes extends AllDirectives {

    //#user-routes-class
    final private ActorRef userRegistryActor;
    final private LoggingAdapter log;

    public UserRoutes(ActorSystem system, ActorRef userRegistryActor) {
        this.userRegistryActor = userRegistryActor;
        log = Logging.getLogger(system, this);
    }

    // Required by the `ask` (?) method below
    Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS)); // usually we'd obtain the timeout from the system's configuration

    /**
     * This method creates one route (of possibly many more that will be part of your Web App)
     */
    //#all-routes
    public Route routes() {
        return route(pathPrefix("searchTweets", () ->
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
                    CompletionStage<Optional<UserRegistryActor.Users>> futureUsers = PatternsCS
                            .ask(userRegistryActor, new UserRegistryMessages.SearchTweets(username, n), timeout)
                            .thenApply(obj -> (Optional<UserRegistryActor.Users>) obj);
                    return onSuccess(() -> futureUsers,
                            searchTweets -> complete(
                                    StatusCodes.OK,
                                    searchTweets.get(),
                                    Jackson.marshaller()));
                });
                //#searchTweets-logic
    }
    //#searchTweets-get
}
