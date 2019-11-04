

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
It's hard to know why they don't work well, but possible reasons could be: 
- Their algorithm weights matches oriented around word statistics (like term frequency), but their parsing Thai words incorrectly (note: many text processors, including Google Docs until very recently, counted Thai words incorrectly). Their parsing / normalization might rely on cues like word spaces or punctuation that exist in many Western languages but don't in Thai.
- Because there's no punctuation to show the end of a sentence in Thai, the programs must parse by line break (i.e. at the end of a paragraph), but this ends up creating much larger segments, which for certain ways of measuring similarity (like edit distance), would make Segments seem quite different, when in fact there are substantial common substrings inside
- The above two reasons seem very obvious, so I have no real idea why most software fails to work with Thai well...

# Application Basic Requirements
For any translator who actually wants to use this software, it must be able to:
- Save translations of a file (have persistence)
- Show commonality with prior translations
- Allow editing of the source text (often don't notice typos or line-break errors until translating)
- Import prior translations from other places (i.e. .tmx files from other CAT tools)
- Export the translation to a .txt or some other format

# Other Requirements
- Open a .txt file
- Be able to enter a translation and save it for later searches (“commit”)
- Show previously translated segments that have commonality with file 
- Must have persistence: translator should be able to open program later and find all data the same
- Allow quick and easy editing of source, including merging/splitting segments 
- Be able to export translation
- Import: Prior translation pairs (.txt in source and .txt in target language) and/or .tmx files (the industry standard file for CAT tool software)

# Technical Considerations:
- Search the corpus quickly
- Robustly implement updates to the corpus so searches remain valid
- Allow robust and correct implementation of state change, including redo/undo
- Store data permanently and retrieve in efficient way.
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
* **MutateFileAction** - interface for user actions that directly affect segments in a file. This abstraction makes it so you backup to database and push to UndoManager all in one place
* **UndoManager** - handles undo-redo capability by storing duplicate states 
* **DatabaseManager** - because SQL calls were slow, this module records prior versions of the file and only updates in the database the segments that were changed. 
* **DatabaseOperations** - The actual SQLite database calls. Was made with all static methods (bad!) for convenience sake, and because it was assumed there wouldn’t be multiple databases (bad! need to change this!)

# Immutable Segments
Whole program revolves around Segments.

By making them immutable, all operations (committing segments, editing source text, etc.) can be seen as adding or removing Segments. Behavior such as undo/redo and the database, are therefore much easier to reason about and test. 

This isn’t too expensive because individual Segments are relatively short Strings.

# Search Algorithm 
The translator will not just rely on their own translations for matches, but will will also download corpuses of translations from the web (especially say of legal codes, legislation, etc.) 

Because this might lead to a significant sized corpus, so I wanted to try more "fancy" techniques than simlpe Unix grep.

While perhaps unnecessary, I was really fascinated by the techniques used in the field of “information retrieval” (IR) that are used in many modern search engines. One of the most common techniques, and used by many search engines, is to have inverted indices which are key-value pairs where the key is a term (or terms) and the value is the “postings list” which is a list of all document ids that contain that term. 

By breaking up your search query into these terms or groups of terms, you can provide O(1) lookup time to find list of all documents that may be matches. 

One useful way to create terms is to use ngrams, based on either words or characters. “The king sat on the throne…”
		2-grams with words 		→ “The king”, “king sat”, “sat on”....
		7-grams with characters 	→ “The kin”, “he king”, “e king “, 

What are postings lists (mention positional index)

<Diagram of Postings Lists>

# Why do n-grams based on characters, not words?
Many people working in IR technologies might be used to n-grams referring to words. Below are my reasons for why I chose to use n-grams based on short subsequences of characters. I also subsequently found out that this is not an uncommon choice for many Asian languages (source). Here were my reasons that applied to Thai:
- Thai is not an inflected language, so there are not variations of the same word (e.g. eat, ate, eaten). Thus there is much less need for tokenization (representing eat/ate as the same word in your indices). 
- Thai does not use spaces between words, so deciding what is a word is somewhat arbitrary.
- As a translator I often had to work with OCR to scan documents and current OCR technology does not work as well with Thai. Errors, while sometimes predictable, were not always so and might be hard to tokenize well, especially since there may not be a vast data set to learn from for that particular OCR technology.

For all the above reasons, I found that simply searching for common substrings and ignoring word boundaries actually worked quite well in practice. 

The downside of using n-grams for characters is that there are far more of them and thus consume more memory. 

# Ngram - Postings List Further considerations
When using postings lists, it is sometimes useful to not only have a list of document ids, but also a pointer to where in the document the term is. In IR literature, this is called a “positional index.” If you do this, you can use some cool techniques with multiple iterators going down different postings lists to calculate whether matches for different terms are contiguous. As explained here:
...you would start with the least frequent term and then work to further restrict the list of possible candidates. In the merge operation, the same general technique is used as before, but rather than simply checking that both terms are in a document, you also need to check that their positions of appearance in the document are compatible with the phrase query being evaluated. This requires working out offsets between the words. (source)

I found that for this software, especially because I was using long k-grams of characters and was interested in fuzzy matching, this proved tricky and rather complicated. 

Because individual segments are not very long I found it was sufficient for the postings lists merely to store segments and then to run a second algorithm comparing these candidate segments with the source segment. This also had the added benefit of easily being able to swap out different matching algorithms for the final step. 

However I didn’t find this technique necessary because my postings lists point to individual Segments which are usually quite short and it’s much easier then to use custom search algorithms to then compare the source and corpus segment. 

# Smart (Fuzzy) Match (not yet implemented)
Here I used a more complex match algorithm that considers (1) rarity of ngrams (2) cases where there is significant commonality but all substrings are below min length

“...for the purposes of…” → 19 characters
not useful (because it’s a common phrase the translator is likely familiar with)

“...NHCPA…” → only 5 characters, but user would want to see this because it may be time-consuming to research again what the abbreviation stands for.

Sentence about minimum match length sometimes not catching useful cases

<inset of interesting match with obvious commonality but where no single common substring meets min length requirements.

# Pros / Cons of Search Algorithm
By implementing own postings lists allowed a lot of flexibility in customizing search (such as frequency ranking).

Doing a basic match algorithm that is based simply finding common substrings of a certain length was very important to me, as this seems like the most obvious way to find matches and the complicated algorithms used by other software seems to not work well. Conceptually simple was a high priority for the match algorithm.

The con is that the postings lists actually take up a fair amount of memory. My initial understanding was that Java’s .substring() method for String class did NOT copy whole char array but only copied pointers, however this may have been a misunderstanding as the Postings List are quite memory heavy. However, there are many ways this could be made more efficient.


# Undo / Redo 
Possible undo implementations: (1) an inverse operation for every operation or (2) storing duplicate states. I chose duplicating states.

Duplicating states is easier to test (no need to test edge cases for every kind of action performed) and more robust (one error in an inverse operation would corrupt the whole undo stack). 

Memory can be an issue but it would not be hard to avoid by:
(1) not making undo “infinite”; also could augment by spacing out logarithmically in time, prioritizing undo of certain operations over others, etc..
(2) only record the Segments added/removed for each operation (in other words, the XOR between two consecutive versions of the file). 

# Database
Used SQLite, probably should have just used protobuffs, but had trouble getting them to work with IDE. 

One SQL table stores segments, one table stores list of files.

Separate tables makes file queries more efficient, but could’ve perhaps used SQL ‘indexed views.’

As far as I could tell, there’s no way to show ordered information in SQL tables (like linked lists), so I had to use a “rank” attribute. Didn’t use ORM’s or protobuffs at the time, but perhaps they would have been a better option in retrospect for this reason. 

# UI Design
Used Photoshop to design overall look, created icons in Illustrator.

Used JavaFX because it is a common UI framework for Java. 

As much as possible UI was done through FXML/CSS because it separates the UI from the backend and uses fewer lines of code. However, I found that some dynamically generated content was not possible in pure FXML/CSS, so I did some of that in Java in the controller class. 

# Further Resource / Reading
[Lecture](https://web.stanford.edu/class/cs276/handouts/lecture3-tolerant-retrieval-handout-1-per.pdf) about information retrieval (techniques used for search engines. 

[Statistics](https://prozcomblog.com/2013/03/22/cat-tool-use-by-translators-who-is-using/) on usage of CAT tools by translators. 
