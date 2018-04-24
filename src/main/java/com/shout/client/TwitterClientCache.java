package com.shout.client;

import akka.http.scaladsl.model.DateTime;
import lombok.Getter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.HashMap;
import java.util.Map;

/**
 * Shout service request cache
 */
public class TwitterClientCache {

    private static Map<String, CacheElem> cache = new HashMap<>();
    private static TwitterClient twitterClient = new TwitterClient();

    /**
     * @param key
     * @param n
     * @return
     */
    public static ResponseList<Status> get(String key, int n) {
        CacheElem cacheElem = cache.get(key);
        if (cacheElem == null || cacheElem.getResponseList().size() < n) {
            ResponseList<Status> responseList;
            try {
                responseList = twitterClient.getUserTimeline(key);
            } catch (TwitterException e) {
                return null;
            }
            put(key, responseList);
            return responseList;
        } else {
            return cacheElem.getResponseList().subList(0, n);
        }
//        if(cacheElem.getDateTime() - DateTime.now() > 5) {
//            cache.remove(key);
//            cacheElem = null;
//        }
//        return cacheElem.getResponseList();
    }

    /**
     * @param key
     * @param responseList
     * @return
     */
    public static CacheElem put(String key, ResponseList<Status> responseList) {
        return cache.putIfAbsent(key, new CacheElem(responseList, DateTime.now()));
    }

    /**
     *
     */
    private static class CacheElem {

        @Getter
        private final ResponseList<Status> responseList;
        @Getter
        private final DateTime dateTime;

        public CacheElem(ResponseList<Status> responseList, DateTime dateTime) {
            this.responseList = responseList;
            this.dateTime = dateTime;
        }
    }
}