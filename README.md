## CodeWordsPartner

This is an attempt to create an artificial player for the game CodeWords.  In it, one play provides clues to another to help them select target words from a set of words.  The partner will use the Google Word2Vec framework and Googles sample News data set to generate candidate clues, weight them by similarity, and select a clue to present to the user.