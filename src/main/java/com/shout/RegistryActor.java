package com.shout;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Optional;

public class RegistryActor extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props() {
        return Props.create(RegistryActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ShoutServiceRegistryMessages.SearchTweets.class, st -> {
                    st.SearchTweets();
                    getSender().tell(Optional.of(st.getResults()), getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    public static class Users {
    }
}
