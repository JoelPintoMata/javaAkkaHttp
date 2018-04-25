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

    class SearchTweets implements Serializable {

        private String username;
        private int n;
        private List<String> results;

        public SearchTweets() {
        }

        public SearchTweets(String searchQuery, String n) {
            this.username = searchQuery;
            this.n = Integer.parseInt(n);
        }

        public void SearchTweets() {
            ResponseList<Status> status;

            results = new ArrayList<>(n);
            int n_processed=0;
            do {
                status = TwitterClientCache.get(username, n);
                for (int i = 0; i < status.size() && n_processed < n; i++) {
                    results.add(status.get(i).getText());
                    n_processed++;
                }
            } while (n_processed < n);
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

        public void setResults(List<String> result) {
            this.results = result;
        }
    }
}