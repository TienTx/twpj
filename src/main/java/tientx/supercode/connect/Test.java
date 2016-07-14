/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tientx.supercode.connect;

import java.util.Map;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author zOzDarKzOz
 */
public class Test {

    private Twitter twitter;

    public Test() throws TwitterException{
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Var.API_KEY)
                .setOAuthConsumerSecret(Var.API_SECRET)
                .setOAuthAccessToken(Var.ACCES_TOKEN)
                .setOAuthAccessTokenSecret(Var.ACCES_TOKEN_SECRET);
//                .setHttpProxyHost("localhost")
//                .setHttpProxyPort(8080);
        TwitterFactory factory = new TwitterFactory(cb.build());
        this.twitter = factory.getInstance();
        System.out.println(twitter.getScreenName());
    }

    protected void waitUntilICanMakeAnotherCallUserTimeline() throws TwitterException, InterruptedException {
        Map<String, RateLimitStatus> temp = twitter.getRateLimitStatus();
        RateLimitStatus temp2 = temp.get("/favorites/list");
        System.out.println(temp2.getRemaining());
        if (temp2.getRemaining() == 0) {
            Thread.sleep((temp2.getSecondsUntilReset() + 5) * 1000);
            return;
        }
        System.out.println(temp2.getSecondsUntilReset());
        int secondstosleep = 1 + temp2.getSecondsUntilReset() / temp2.getRemaining();
        System.out.println(secondstosleep);
        Thread.sleep(secondstosleep * 1000);
    }

    public void getFavorites(String user) throws TwitterException, InterruptedException {
        Paging pg = new Paging(500);
        waitUntilICanMakeAnotherCallUserTimeline();
//        ResponseList<Status> statuses = twitter.getFavorites(user, 500);
//        statuses.stream().forEach((sts) -> {
//            System.out.println(sts.getText());
//        });
        ResponseList<Status> responseList = twitter.getFavorites(user, pg);
        int i = 0;
        for (Status status : responseList) {
            System.out.println(++i + ":" + status.getText());
        }
    }

    public static void main(String[] args) throws TwitterException, InterruptedException {
        Test test = new Test();
        String userId = "988209746";
        test.getFavorites(userId);
    }
}
