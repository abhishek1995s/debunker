import tweepy
# assuming twitter_authentication.py contains each of the 4 oauth elements (1 per line)

ACCESS_TOKEN = '2899720447-f2k2VqXFfiWNFsFErm2idMlT5mIoW2AqAJglptm'
ACCESS_SECRET = 'MMygbYLpMCBGBW6LSpMmzFdCsrfu0LubHULxx4TiCSn3Q'
CONSUMER_KEY = 'VLig3habyiGxOIEs5Y7B5cPjD'
CONSUMER_SECRET = 'VdOgLOEUkjni6oFym5YlSAkULfLha31R86tJraR6ekbT3K2v9w'
auth = tweepy.OAuthHandler(CONSUMER_KEY, CONSUMER_SECRET)
auth.set_access_token(ACCESS_TOKEN, ACCESS_SECRET)

api = tweepy.API(auth)

query = 'brexit'
max_tweets = 20
searched_tweets = [status for status in tweepy.Cursor(api.search, q=query).items(max_tweets)]
for i in searched_tweets:
	print(i.text)
