# Shout Service

## Run
$ mvn compile exec:java -Dtwitter4j.oauth.consumerKey=<comsumer_key> -Dtwitter4j.oauth.consumerSecret=<consumer_secret> -Dtwitter4j.oauth.accessToken=<access_token> -Dtwitter4j.oauth.accessTokenSecret=<access_token_secrete> -Dexec.mainClass="<main_class>"

## Test
$ mvn test

## Application properties

### Application properties set via System Properties

## Further reading
[https://developer.twitter.com/en/docs](https://developer.twitter.com/en/docs]
[https://medium.com/@ssola/playing-with-twitter-streaming-api-b1f8912e50b0](https://medium.com/@ssola/playing-with-twitter-streaming-api-b1f8912e50b0)

mvn compile exec:java -Dtwitter4j.oauth.consumerKey=ENqDdAGUF7YdUaCAJvltoVD5A -Dtwitter4j.oauth.consumerSecret=GdxbTo0WMbaaCri4T7FCyLYGooRAzTATdzCOjnyu1HoC092VUi -Dtwitter4j.oauth.accessToken=1372228232-EbN5CIITosN4wSC7nD0nq8q43j3rLTMNJfTolWs -Dtwitter4j.oauth.accessTokenSecret=4UfnqkgllSPzpulK77RJLacIjPA69liv9FuLj8Mam97RP -Dexec.mainClass="ShoutServer"


