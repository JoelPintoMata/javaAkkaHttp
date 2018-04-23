package com.lightbend.akka.http.sample;

import twitter4j.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface UserRegistryMessages {

    class SearchTweets implements Serializable {

        private final String username;
        private final int n;
        private List<String> results;

        public SearchTweets(String searchQuery, String n) {
            this.username = searchQuery;
            this.n = Integer.parseInt(n);
        }

        public void SearchTweets() {
            results = new ArrayList<>(n);
            int n_processed=0;
            Twitter twitter = new TwitterFactory().getInstance();
            try {
                ResponseList<Status> status;
                do {
                    status = twitter.getUserTimeline(username);
                    for (int i=0; i<status.size() && n_processed<n; i++) {
//                        System.out.println("@" + n_processed + " : " + status.get(i).getUser().getScreenName() + " - " + status.get(i).getText());
                        results.add(format(status.get(i).getText()));
                        n_processed++;
                    }
                } while (n_processed<n);
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to search tweets: " + te.getMessage());
                System.exit(-1);
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
            return results;
        }
    }
}