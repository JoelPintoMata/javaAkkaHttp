package com.lightbend.akka.http.sample;

import com.lightbend.akka.http.sample.UserRegistryActor.User;
import twitter4j.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface UserRegistryMessages {

    class GetUsers implements Serializable {
    }

    class ActionPerformed implements Serializable {
        private final String description;

        public ActionPerformed(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    class CreateUser implements Serializable {
        private final User user;

        public CreateUser(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    class GetUser implements Serializable {
        private final String name;

        public GetUser(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    class DeleteUser implements Serializable {
        private final String name;

        public DeleteUser(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    class SearchTweets implements Serializable {

        private final String username;
        private final int n;
        private List<String> results;

        public SearchTweets(String searchQuery, int n) {
            this.username = searchQuery;
            this.n = n;
        }

        public void SearchTweets() {
            results = new ArrayList<>(this.getN());
            int n_processed=0;
            Twitter twitter = new TwitterFactory().getInstance();
            try {
                Query query = new Query(username);

//                QueryResult result;
                ResponseList<Status> status;
                do {
                    status = twitter.getUserTimeline(username);
                    for (int i=0; i<status.size() && n_processed<n; i++) {
                        System.out.println("@" + n_processed + " : " + status.get(i).getUser().getScreenName() + " - " + status.get(i).getText());
                        results.add(status.get(i).getText().toUpperCase() + "!");
                        n_processed++;
                    }
//                    result = twitter.search(query);
//                    List<Status> tweets = result.getTweets();
//                    for (int i=0; i<tweets.size() && n_processed<n; i++) {
//                        System.out.println("@" + n_processed + " : " + tweets.get(i).getUser().getScreenName() + " - " + tweets.get(i).getText());
//                        results.add(tweets.get(i).getText().toUpperCase() + "!");
//                        n_processed++;
//                    }
//                } while ((query = result.nextQuery()) != null && n_processed<=n);
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