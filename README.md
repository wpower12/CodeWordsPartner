## CodeWordsPartner

This is an attempt to create an artificial player for the game CodeWords.  In it, one play provides clues to another to help them select target words from a set of words.  The partner will use the Google Word2Vec framework and Googles sample News data set to generate candidate clues, weight them by similarity, and select a clue to present to the user.

# Examples

The player currently outputs some 'ok' clues.  Most are just giberish though.  Work could be done on the hueristics and triviality size.  Also, the scaling between clues of different sizes could be investigated.

```
[Shirt, Bleach, Pajamas, Gallop, Freight, Opaque, Mistake, Skate, Pomp, Cliff]
Clue: VicHealth, 2
Targets:  [Bleach, Pajamas, Gallop]
Assassin: [Shirt]

[Leak, Desk, Plaid, Skating, Office, Green, Cow, Swimming, Sweater, School]
Clue: www.baystateskatingschool.org, 2
Targets:  [Desk, Plaid, Skating]
Assassin: [Leak]

[Plaid, Thunder, Mop, Tool, Applause, Basketball, Ovation, Riddle, Portfolio, Winter]
Clue: Expediter, 1
Targets:  [Thunder, Mop, Tool]
Assassin: [Plaid]

[Fix, Mop, Ergonomic, Flu, Doll, Hat, Dictionary, Standing, Finger, Conversation]
Clue: undergarment, 1
Targets:  [Mop, Ergonomic, Flu]
Assassin: [Fix]

[Outside, Princess, Wheelchair, Song, Phone, Campsite, Dart, Brand, Blunt, Leak]
Clue: Sing, 3
Targets:  [Princess, Wheelchair, Song]
Assassin: [Dart]
```
