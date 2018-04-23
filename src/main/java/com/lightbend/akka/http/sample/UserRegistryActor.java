package com.lightbend.akka.http.sample;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import twitter4j.*;
import twitter4j.Status;

import java.io.Serializable;
import java.util.*;

public class UserRegistryActor extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static class Users {
    }

    static Props props() {
        return Props.create(UserRegistryActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserRegistryMessages.SearchTweets.class, st -> {
                    st.SearchTweets();
                    getSender().tell(Optional.of(st.getResults()), getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
