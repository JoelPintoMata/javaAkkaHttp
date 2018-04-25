package com.letsShout.model;

import com.letsShout.client.TwitterClientCache;
import twitter4j.ResponseList;
import twitter4j.Status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Shout service message registry
 * This interface extensible container of this service message/models/classes
 */
public interface MessageRegistry {

    /**
     * Search tweets type message and model
     */
    class SearchTweets implements Serializable {

        private String username;
        private int n;
        private TwitterClientCache twitterClientCache;

        private List<String> results;

        /**
         * Default constructor for unit test purposes
         */
        public SearchTweets() {
        }

        /**
         * Constructor
         *
         * @param searchQuery        the search query
         * @param n                  the number of results expected
         * @param twitterClientCache a twitter client cache instance
         */
        public SearchTweets(String searchQuery, String n, TwitterClientCache twitterClientCache) {
            this.username = searchQuery;
            this.n = Integer.parseInt(n);
            this.twitterClientCache = twitterClientCache;
        }

        public void SearchTweets() {
            ResponseList<Status> responseList;

            results = new ArrayList<>(n);
            int n_processed=0;
            do {
                responseList = twitterClientCache.get(username, n);
                for (int i = 0; i < responseList.size() && n_processed < n; i++) {
                    results.add(responseList.get(i).getText());
                    n_processed++;
                }
            } while (n_processed < n);
        }

        /**
         * Formats a tweet message
         * (upper case + "!")
         * @return shouts a list of tweets
         */
        public List<String> getResults() {
            return this.results.stream().map(x -> x.toUpperCase() + "!").collect(Collectors.toList());
        }

        public void setResults(List<String> result) {
            this.results = result;
        }
    }
}