package com.shout.client;

import twitter4j.*;

/**
 * Twitter client
 */
public class TwitterClient {

    private final Twitter twitter;

    public TwitterClient() {
        twitter = new TwitterFactory().getInstance();
    }

    /**
     * @param username
     * @return
     * @throws TwitterException
     */
    public ResponseList<Status> getUserTimeline(String username) throws TwitterException {
        return twitter.getUserTimeline(username);
    }
}