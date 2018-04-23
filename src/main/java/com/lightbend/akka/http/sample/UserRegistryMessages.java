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
            results = new ArrayList<>(this.getN());
            int n_processed=0;
            Twitter twitter = new TwitterFactory().getInstance();
            try {

//                QueryResult result;
                ResponseList<Status> status;
                do {
                    status = twitter.getUserTimeline(username);
                    for (int i=0; i<status.size() && n_processed<n; i++) {
                        System.out.println("@" + n_processed + " : " + status.get(i).getUser().getScreenName() + " - " + status.get(i).getText());
                        results.add(status.get(i).getText().toUpperCase() + "!");
                        n_processed++;
                    }
                } while (n_processed<n);
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to search tweets: " + te.getMessage());
                System.exit(-1);
            }
        }

        public List<String> getResults() {
            return results;
        }

        public int getN() {
            return n;
        }
    }
}