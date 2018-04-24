package com.shout;

import com.shout.client.TwitterClient;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Message registry for the Shout Service
 */
public interface ShoutServiceRegistryMessages {

    class SearchTweets implements Serializable {

        private String username;
        private int n;
        private TwitterClient twitterClient;

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
                    status = twitterClient.getUserTimeline(username);
                    for (int i=0; i<status.size() && n_processed<n; i++) {
//                        System.out.println("@" + n_processed + " : " + status.get(i).getUser().getScreenName() + " - " + status.get(i).getText());
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
         * Formats a tweet test
         * (uppper case + "!")
         * @param s a string
         * @return a formatter tweet test
         */
        private String format(String s) {
            return s.toUpperCase() + "!";
        }

        public List<String> getResults() {
            return this.results.stream().map(x -> format(x)).collect(Collectors.toList());
        }

        protected void setResults(List<String> results) {
            this.results = results;
        }
    }
}