package com.letsShout.client;

import akka.http.javadsl.testkit.JUnitRouteTest;
import org.junit.Test;
import twitter4j.TwitterException;

import static org.mockito.Mockito.*;

public class TwitterClientCacheTest extends JUnitRouteTest {

    @Test
    public void testCacheGet() throws TwitterException {
        TwitterClient twitterClient = mock(TwitterClient.class);
        TwitterClientCache twitterClientCache = new TwitterClientCache(twitterClient);

        twitterClientCache.get(anyString(), 1);
        verify(twitterClient, times(1)).getUserTimeline(anyString());
    }
}