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

    private Map<String, CacheElem> cache = new HashMap<>();
    private TwitterClient twitterClient;

    private Logger logger = Logger.getLogger(TwitterClientCache.class);

    /**
     * Default constructor
     *
     * @param twitterClient twitter client request cache
     */
    public TwitterClientCache(TwitterClient twitterClient) {
        this.twitterClient = twitterClient;
    }

    /**
     *
     * @param key the cache key
     * @param n the number of elements to retrieve
     * @return the cached element
     */
    public ResponseList<Status> get(String key, int n) {
        CacheElem cacheElem = cache.get(key);
        ResponseList<Status> responseList = null;

        if (cacheElem == null) {
//            if the cached results are less than what we want we must fectch new results
            if (cacheElem != null && cacheElem.getResponseList().size() < n) {
                logger.info("key: " + key + " has a shorter local cache, calling the client");
            }

//            if the cached results are too old we must invalidate the cache and fetch new results
//            TODO
            try {
                responseList = twitterClient.getUserTimeline(key);
                put(key, responseList);

                logger.info("key: " + key + " not found in cache");
            } catch (TwitterException e) {
                logger.error("Unable to get results");
            }
        } else {
            logger.info("key: " + key + " retrieved from cache");
            responseList = cacheElem.getResponseList();
        }
        return responseList;
    }

    /**
     * Atomically inserts a new cache entry if there's not yet element with a given key
     * @param key the cache key
     * @param responseList the cached result
     * @return the cached element
     */
    public CacheElem put(String key, ResponseList<Status> responseList) {
        return cache.putIfAbsent(key, new CacheElem(responseList, DateTime.now()));
    }

    /**
     * Shout service twitter client request cache element
     */
    private class CacheElem {

        private final ResponseList<Status> responseList;
        private final DateTime dateTime;    // used for cache entry invalidation

        public CacheElem(ResponseList<Status> responseList, DateTime dateTime) {
            this.responseList = responseList;
            this.dateTime = dateTime;
        }

        public ResponseList<Status> getResponseList() {
            return responseList;
        }
    }
}