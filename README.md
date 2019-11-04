Home Tab:
![Image of homescreen](https://github.com/acloudmovingby/Translation-Assistance-Tool/blob/master/Screenshots/Home%20Screen.png)

Translation Tab:
![Image of Translation screen](https://github.com/acloudmovingby/Translation-Assistance-Tool/blob/master/Screenshots/Translation%20Screen.png)

# Overview

This tool assists translators by reminding them of other translations that resemble the document they are currently translating. 

<Picture>

These might include prior assignments the user has worked on or translations done by others that they have downloaded and added to the corpus.

I used this tool myself for translation work in Thai, and included translations of my own as well as designing a parser to download large Thai legal texts and add them to my corpus. 

# Purpose

The tasks that consume the most time translating are often researching domain-specific vocabulary and thinking of how to phrase something in the target language. A tool that speeds up this process is invaluable. Such software, called CAT tools, have existed since the 1990s:

<Picture>

Brands like Trados and Wordfast are widely used by translators <statistic about percent of translators that use CAT tools>

However in my experience these tools often worked very poorly with Thai.

# Why other software does not work well with Thai
It's hard to know why they don't work well, but it may have to do with incorrectly parsing words. Many major text editors (including Google Docs until around 2018), incorrectly counted Thai words, showing that they're not parsing them correctly. Another possibility is that because in Thai there's no punctuation to indicate the end of a sentence, you have no choice but to break segments up by line break (i.e. end of paragraphs). If their algorithms do something very simple, like look for edit distance (Levenshtein distance), then these longer segments might appear less similar to other segments. But honestly, I have no idea why most CAT tools work so badly with Thai...


# Application Basic Requirements
For any translator who actually wants to use this software, it must be able to:
- Save translations of a file (have persistence)
- Show commonality with prior translations in an intelligent/useful way
- Allow editing of the source text (sometimes the translator won't notice typos or line-break errors until translating)
- Import prior translations from other places (i.e. .tmx files from other CAT tools)
- Export the translation to a .txt or some other format

# Technical Considerations:
- Search the corpus quickly
- Robustly implement updates to the corpus so searches remain valid
- Allow robust and correct implementation of state change, including redo/undo
- Store data permanently on hard-drive and retrieve in efficient way.
- Make it look pretty

# Overall structure
Designed so database / UI could be detached. 

UI relies on JavaFX which binds components to properties set in UIState. 

<class diagram>

# Important classes
* **Segment** - Immutable, represents a pairing between a source language and the translation in the target language, if it exists. Has unique id.
* **File** - A list of Segments, with a file name and unique id.
* **State** - Stores all the files in the corpus as well as the MatchManager whose data (the postings lists) depends on the corpus.
* **Controller** - Primarily a wiring between the UI code and the business logic. Sends all user interaction to the Dispatcher.  
* **Dispatcher** - Either interacts with the state directly to perform user actions or delegates the task to a specialized module (e.g. Undo Manager)
* **UserAction** - interface for user actions that directly affect segments in a file. This abstraction makes it so you backup to database and push to UndoManager all in one place
* **UndoManager** - handles undo-redo capability by storing duplicate states 
* **DatabaseManager** - because SQL calls were slow, this module records prior versions of the file and only updates the database for the segments that were changed. 
* **DatabaseOperations** - The actual SQLite database calls. Was made with all static methods (bad!) for convenience sake, and because it was assumed there wouldn’t be multiple databases (bad! need to change this!)

# Making Segments Immutable
Whole program revolves around Segments.By making them immutable, all operations (committing segments, editing source text, etc.) can be seen as adding or removing Segments. Behavior such as undo/redo and the database, are therefore much easier to reason about and test. 

This isn’t too expensive because individual Segments are relatively short Strings.

# Search Algorithm 
The translator will not just rely on their own translations for matches, but will will also download corpuses of translations from the web (especially say of legal codes, legislation, etc.) 

I tried to implement my own inverted index (essentially a hashmap between terms and the documents they appear in). By breaking up your search query into these terms or groups of terms, you can provide O(1) lookup time to find list of all documents that may be matches. 

Often you use ngrams, based on either words or characters. “The king sat on the throne…”
		2-grams with words 		→ “The king”, “king sat”, “sat on”....
		7-grams with characters 	→ “The kin”, “he king”, “e king “, 

# Why do n-grams based on characters, not words?
It's common to use n-grams based on words. Below are my reasons for why I chose to use n-grams based on subsequences of characters. Here were my reasons that applied to Thai:
- Thai is not an inflected language, so there are not variations of the same word (e.g. eat, ate, eaten). Thus there is much less need for normalization (representing eat/ate as the same word in your indices). 
- Thai does not use spaces between words, so deciding what is a word is somewhat arbitrary.
- As a translator I often had to work with OCR to scan documents and current OCR technology does not work as well with Thai. Errors, while sometimes predictable, were not always so and might be hard to normalize/tokenize well, especially since there may not be a vast data set to learn from for that particular OCR technology.

For all the above reasons, I found that simply searching for common substrings and ignoring word boundaries actually worked quite well in practice. However there were two major downsides to this choice:
- Uses much more memory.
- Minor issue, but the "rarity" of any particular ngram in the corpus doesn't mean much, as it could be crossing word boundaries. Both words might be common, but it might be uncommon for them to be next to each other, so if I wanted to rank matches by term rarity, it would be better to parse the words separately then use some publicly available term frequency list for Thai.


# Positional Indices in Postings Lists
When using postings lists, it is can be useful to not only have a list of document ids, but also a pointer to where in the document the term is (a "positional index"). You can then use multiple iterators going down different postings lists to calculate, based on the offsets of these positional indices, whether matches for those different n-grams are contiguous in the same document. 

I didn’t find this technique necessary because my postings lists point to individual Segments which are usually quite short and it’s much easier to then use custom search algorithms to compare the source and corpus segments. I found that for this software, especially because I was using long n-grams of characters and was interested in fuzzy matching, iterating through these postings lists simultaneously using positional indices proved tricky and rather complicated. It was simpler to just use the n-grams to find candidate segments, then compare the segments themselves using another algorithm.
This also had the added benefit of easily being able to swap out different matching algorithms for the final step. 


# Pros / Cons of Search Algorithm
By implementing own postings lists allowed a lot of flexibility in customizing search.

The con is that the postings lists actually take up a fair amount of memory, due to an initial misunderstanding of how Java's .substring() method works. My initial understanding was that Java’s .substring() method for String class did NOT copy whole char array but only copied pointers, however this may have been a misunderstanding as the Postings List are quite memory heavy. 

# Undo / Redo 
Possible undo implementations: (1) an inverse operation for every operation or (2) storing duplicate states. I chose duplicating states.

Duplicating states is easier to test (no need to test edge cases for every kind of action performed) and more robust (one error in an inverse operation would corrupt the whole undo stack). 

If memory became an issue, then you could:
(1) not make undo “infinite”; also could augment by spacing out logarithmically in time, prioritizing undo of certain operations over others, and so on...
(2) Simply record the Segments added/removed for each operation, not the whole file.

# Database
Used SQLite, probably should have just used protobuffs, but had trouble getting them to work with IDE. 

One SQL table stores segments, one table stores list of files.

Separate tables makes file queries more efficient, but could’ve perhaps used SQL indexed views.

As far as I could tell, there’s no way to show ordered information in SQL tables (like linked lists), so I had to use a “rank” attribute. Didn’t use ORM’s or protobuffs at the time, but perhaps they would have been a better option in retrospect for this reason. 

# UI Design
Used Photoshop to design overall look, created icons in Illustrator.

Used JavaFX because it is a common UI framework for Java. 

As much as possible UI was done through FXML/CSS because it separates the UI from the backend and uses fewer lines of code. However, I found that some dynamically generated content was not possible in pure FXML/CSS, so I did some of that in Java in the controller class. 

# Further Resource / Reading
[Lecture](https://web.stanford.edu/class/cs276/handouts/lecture3-tolerant-retrieval-handout-1-per.pdf) about information retrieval (techniques used for search engines. 

[Statistics](https://prozcomblog.com/2013/03/22/cat-tool-use-by-translators-who-is-using/) on usage of CAT tools by translators. 
