package com.shout.model;

import com.shout.client.TwitterClient;
import com.shout.client.TwitterClientCache;
import lombok.Setter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Shout service message registry
 * This interface extensible container of this service message/models/classes
 */
public interface MessageRegistry {

    class SearchTweets implements Serializable {

        private String username;
        private int n;
        private TwitterClient twitterClient;
        @Setter
        private List<String> results;

        public SearchTweets() {
        }

        public SearchTweets(String searchQuery, String n, TwitterClient twitterClient) {
            this.username = searchQuery;
            this.n = Integer.parseInt(n);
            this.twitterClient = twitterClient;
        }

        public void SearchTweets() {
            ResponseList<Status> status;

            results = new ArrayList<>(n);
            int n_processed=0;
            try {
                do {
                    status = TwitterClientCache.get(username, n);
                    for (int i=0; i<status.size() && n_processed<n; i++) {
                        results.add(status.get(i).getText());
                        n_processed++;
                    }
                } while (n_processed<n);
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to search tweets: " + te.getMessage());
            }
        }

        /**
         * Formats a tweet message
         * (upper case + "!")
         * @param s a tweet message
         * @return a formatted tweet message
         */
        private String format(String s) {
            return s.toUpperCase() + "!";
        }

        public List<String> getResults() {
            return this.results.stream().map(x -> format(x)).collect(Collectors.toList());
        }
    }
}