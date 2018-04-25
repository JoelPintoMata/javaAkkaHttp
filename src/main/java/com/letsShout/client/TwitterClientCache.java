package com.letsShout.client;

import akka.http.scaladsl.model.DateTime;
import twitter4j.Logger;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.HashMap;
import java.util.Map;

/**
 * Shout service twitter client request cache
 */
public class TwitterClientCache {

    private static Map<String, CacheElem> cache = new HashMap<>();
    private static TwitterClient twitterClient = new TwitterClient();

    private static Logger logger = Logger.getLogger(TwitterClientCache.class);

    /**
     *
     * @param key the cache key
     * @param n the number of elements to retrieve
     * @return the cache elements
     */
    public static ResponseList<Status> get(String key, int n) {
        CacheElem cacheElem = cache.get(key);
        if (cacheElem == null) {
            if (cacheElem != null && cacheElem.getResponseList().size() < n) {
                logger.info("key: " + key + " has a shorter local cache, calling the client");
            }
            ResponseList<Status> responseList;
            try {
                responseList = twitterClient.getUserTimeline(key);
            } catch (TwitterException e) {
                logger.error("Unable to get results");
                return null;
            }
            put(key, responseList);

            logger.info("key: " + key + " not found in cache");
            return responseList;
        } else {
            logger.info("key: " + key + " retrieved from cache");
            return cacheElem.getResponseList();
        }
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
     * Twitter cache element
     */
    private static class CacheElem {

        private final ResponseList<Status> responseList;
        private final DateTime dateTime;

        public CacheElem(ResponseList<Status> responseList, DateTime dateTime) {
            this.responseList = responseList;
            this.dateTime = dateTime;
        }

        public ResponseList<Status> getResponseList() {
            return responseList;
        }
    }
}