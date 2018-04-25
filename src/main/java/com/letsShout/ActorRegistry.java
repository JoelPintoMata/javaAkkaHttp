package com.letsShout;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.letsShout.model.MessageRegistry;

import java.util.Optional;

/**
 * Shout service actor registry
 */
public class ActorRegistry extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static Props props() {
        return Props.create(ActorRegistry.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageRegistry.SearchTweets.class, st -> {
                    st.SearchTweets();
                    getSender().tell(Optional.of(st.getResults()), getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
