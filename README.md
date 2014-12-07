CoinbaseTrader
===============

This is the source code for rule based Coinbase trader. 

This webapp is hosted and freely available from [here](http://coinbasetraders.com).

### Documentation
This application polls Coinbase after every 5 seconds and executes all the transactions that match a given rules.
If your rule is executed then it is removed and stopped.

Currently you can add up to 1 rule per API key but that is something that is about to change in the future.

Whenever you feel like it you can always remove your rule 
by going [here](http://coinbasetraders.com/#/existing/) and 
searching by your api key and pressing the stop button.

Sell rule
---------------------
You can add a sell rule. 
That means that whenever the price in USD is at least the limit you specified the transaction 
with the given amount in bitcoin is executed.

Buy rule
---------------------
You can add a buy rule. 
That means that whenever the price in USD is at most the limit you specified the transaction 
with the given amount in EUR is executed. 


Where do I get the API key and the API secret?
---------------------
That's easy, just go [here](https://www.coinbase.com/settings/api) and press "New API key" button.

The API key must have a buy and sell permissions and make sure that it is enabled.
