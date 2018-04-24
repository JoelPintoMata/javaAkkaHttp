package com.shout.model;

import akka.http.javadsl.testkit.JUnitRouteTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

public class ShoutServiceRegistryMessagesTest extends JUnitRouteTest {

    @Test
    public void testShout() {
        List<String> list = new LinkedList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
        MessageRegistry.SearchTweets searchTweets = spy(MessageRegistry.SearchTweets.class);
        doNothing()
                .when(searchTweets).SearchTweets();
        searchTweets.setResults(list);
        Assert.assertEquals(searchTweets.getResults().size(), 3);
        Assert.assertEquals(searchTweets.getResults().get(0), "TEST1!");
        Assert.assertEquals(searchTweets.getResults().get(1), "TEST2!");
        Assert.assertEquals(searchTweets.getResults().get(2), "TEST3!");
    }
}