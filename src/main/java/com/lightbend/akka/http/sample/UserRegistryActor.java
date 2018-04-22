package com.lightbend.akka.http.sample;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import java.util.*;

public class UserRegistryActor extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    //#user-case-classes
    public static class User {

        private final String name;
        private final String searchQuery;
        private final int age;
        private final String countryOfResidence;

        public User() {
            this.name = "";
            this.searchQuery = "";
            this.countryOfResidence = "";
            this.age = 1;
        }

        public User(String name, String searchQuery, int age, String countryOfResidence) {
            this.name = name;
            this.searchQuery = searchQuery;
            this.age = age;
            this.countryOfResidence = countryOfResidence;
        }

        public String getName() {
            return name;
        }

        public String getSearchQuery() {
            return searchQuery;
        }

        public int getAge() {
            return age;
        }

        public String getCountryOfResidence() {
            return countryOfResidence;
        }
    }

    public static class Users {

        private final List<User> users;

        public Users() {
            this.users = new ArrayList<>();
        }

        public Users(List<User> users) {
            this.users = users;
        }

        public List<User> getUsers() {
            return users;
        }
    }
//#user-case-classes

    static Props props() {
        return Props.create(UserRegistryActor.class);
    }

    private final List<User> users = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserRegistryMessages.SearchTweets.class, searchTweets -> {
                    getSender().tell(users.stream()
                            .findFirst(), getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
