# Design
## Use Spring Boot
Spring Boot Initializr takes care of generating the skeleton maven project.  We use Gradle at E*Trade Advisor Services and I wanted to show at least an intermediate understanding of both build tools.  Any project that uses Make or Ant can be migrated to Gradle or Maven.  

Another advantage of Spring Boot is that the Tomcat server is built into the application.  The developer does not have to configure a server and create the shell scripts to run the server.

Spring Boot generates the HELP.md documentation which provides reference guides and documentation links.

But wait - there's more! The app is a fully contained service:

- Rest services are contained in one app
- Web pages are contained in one app
- Business logic and database access are consolidated in one app
- QA can test and validate one app instead of a huge monolithic application where many times a change in one portion of the application breaks another portion of the monolith.  AKA - "I'm sorry, Dave. I'm afraid I can't do that."
- Changes to the app can be pushed to production without having to push all portions of the monolith - so many 2001 Space Odyssey references.
- The JUnit tests are clearly separated in the src/test directory while the application is found in the src/main directory.

## Use a Database
Since this is a demo use the H2 in-memory database.  A real application would use MySQL, DB2 or Oracle.  Millions of rows per hour is not a concern so I would suggest deploying to Amazon Web Services using MySQL.

### Database Schema

The database schema is found in /anagram/src/main/resources/schema.sql.

The English words are stored in ENGLISH_WORD.  The table has the word and the anagram key which is the count of each letter in the word.

The ANAGRAM\_GRP view contains the anagram key and how many ENGLISH_WORD rows have that key.  The view provides metric information concerned with grouping anagrams.

The word metric information is created by the Java code because only one API call wants that information.

### Keys
An anagram is based on presenting the same letters in new and interesting ways.  According to [WikiPedia](https://en.wikipedia.org/wiki/Anagram)
an anagram "is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once. For example, the word anagram can be rearranged into 'nag a ram', or the word binary into brainy or the word adobe into abode."

The database key is the number of times a character is used in a phrase. The key works for languages that use 26 character letters.  The same project for [Kanji](https://en.wikipedia.org/wiki/Kanji) requires a different analysis since there are 966 characters.

## Design Considerations
Separation of Concern, design flexibility and fault tolerance are primary considerations.

The database interaction code is found in the model and repository packages.

The business logic is found in the service package.

The database entities do not pass out of the service layer.  Future database changes are confined to the service, model and repository packages.

The REST API interacts with the domain and service packages.  

- The Advice class maps Exceptions thrown by the service to HTTP Status codes.
- An invalid English word throws an InvalidWordException which is mapped to HttpStatus.BAD_REQUEST.

Reduction of boiler plate code

- [LomBok](https://projectlombok.org) generates much of the boiler plate bytecode.  The getters, setters, toString, equals and hash code methods are created by @Data.
- The repositories extend JpaRepository.  The interface generates most of the boiler plate CRUD methods plus many batch methods.  Custom methods and queries are easily declared in the interface.

The application is broken up into discrete parts that junior programmers can code.  This does not mean that junior programmers code to a cast-in-stone interface, rather here is a task, go forth and show everyone why you should be a senior programmer.

With all tasks - leave the modified code better.  Pull common code into a reusable method, Use more advanced techniques to simplify code.  If you need to comment code, consider renaming code to make the code self documenting.  For Example (instead of the dead Latin language abbreviation i.e.)

```java
  for(int i=0; i<k; i++) {
     log.info(databaseRows.get(i).toString())
  }
```
replace with

```java
​  databaseRows.forEach( row -> log.info( row.toString()))
```

## Future Enhancements
- The dictionary is blocked by synchronization to ensure thread safe access. Special concern is given to the scenario of one thread accessing the dictionary while another thread is initializing the dictionary. A better way is to place the dictionary in a database table and place SQL inserts in /anagram/src/main/resources/data.sql.  The dictionary table is initialized before the application is ready to accept requests.
- Dictionary table contains a column flagging the word type (word, proper noun, abbreviation, phrase).  Some examples:
  - apple - word
  - Zythia - proper noun
  - NASA - abbreviation
  - Life is beautiful - phrase
  - FedEx - doesn't quite follow the rules for a proper noun
- Remove the .json extension from the REST API.  To remove all anagrams of 'read' use:
  - curl -i -X DELETE http://localhost:8080/anagrams/read
- If word metric information is requested by multiple API calls then consider converting the Java code to custom database calls. 
- Add an endpoint to add proper nouns to the datastore.  Do not verify the proper noun against the dictionary.  Allow any word that starts with a capital letter and the rest of the letters are lower case.
- Currently the design is one word entered, several words of the same length are returned. Accept phrases - 'nag a ram'  would return 'anagram'  
- Display subsets - right now a seven letter word displays the seven letter anagrams.  Display the
  - five and two letter words that utilize all seven letters
  - four and three letter words that utilize all seven letters
- Add a web interface to display metrics
- Add metrics to the API calls and display those metrics on the web interface

Clients will interact with the API over HTTP, and all data sent and received is expected to be in JSON format.

## Run Application
The API runs on localhost port 8080.
To run the application on Windows 10:
```bash
# Go to the application directory
mvnw.cmd spring-boot:run

# Linux machines
./mvnw spring-boot:run
```


```bash
# Adding words to the corpus  
# The JSON is stored in a text file due to issues with CURL on Windows 10
# The content type is added because of issues with Windows 10

data.txt holds  '{ "words": ["read", "dear", "dare"] }'

$ curl -i -X POST -d "@data.txt" -H "Content-Type: application/json"  http://localhost:8080/words.json
HTTP/1.1 200 Created
...

# Fetching anagrams
$ curl -i http://localhost:8080/anagrams/read.json
HTTP/1.1 200 OK
...
{
  "anagrams": [
    "dear",
    "dare"
  ]
}

# Specifying maximum number of anagrams
$ curl -i http://localhost:8080/anagrams/read.json?limit=1
HTTP/1.1 200 OK
...
{
  "anagrams": [
    "dear"
  ]
}

# Specifying maximum number of anagrams and return Proper Nouns
$ curl -i http://localhost:8080/anagrams/read.json?limit=1&allowProperNoun=true
HTTP/1.1 200 OK
...
{
  "anagrams": [
    "dear"
  ]
}

# Specify an invalid English word
$ curl -i http://localhost:8080/anagrams/readdjsfl.json
HTTP/1.1 400
...
invalid English word [readdjsfl]

# Delete single word
$ curl -i -X DELETE http://localhost:8080/words/read.json
HTTP/1.1 200
...

# Delete invalid word
$ curl -i -X DELETE http://localhost:8080/words/readjkfdafa.json
HTTP/1.1 200
...

# Delete all words
$ curl -i -X DELETE http://localhost:8080/words.json
HTTP/1.1 200
...
```

## Optional

#### Endpoint that returns a count of words in the corpus and min/max/median/average word length
```bash
$ curl -i http://localhost:8080/anagrams/metrics
HTTP/1.1 200 OK
...
{
  "count": 11,
  "min":2,
  "max":8,
  "average":5.181818,
  "median":4.0
}
```
#### Respect a query param for whether or not to include proper nouns in the list of anagrams
```bash
# Specifying maximum number of anagrams and return Proper Nouns
$ curl -i http://localhost:8080/anagrams/read.json?limit=1&allowProperNoun=true
HTTP/1.1 200 OK
...
{
  "anagrams": [
    "dear"
  ]
}

$ curl -i http://localhost:8080/anagrams/a.json?allowProperNoun=true
HTTP/1.1 200 OK
...
{
  "anagrams": [
    "A"
  ]
}
```

#### Endpoint that takes a set of words and returns whether or not they are all anagrams of each other

- The words passed to the call are not placed in datastore
- The words have to be in the dictionary
- The endpoint fails fast - the endpoint does not return a list of all invalid words

```bash
# The JSON is stored in a text file due to issues with CURL on Windows 10
# The content type is added because of issues with Windows 10

data.json holds  '{ "words": ["peanut", "pea", "nut"] }'

$ curl -i -X POST -d "@data.json" -H "Content-Type: application/json"  http://localhost:8080/anagrams
HTTP/1.1 200
...
false

data.json holds  '{ "words": ["rare", "rear"] }'
$ curl -i -X POST -d "@data.json" -H "Content-Type: application/json"  http://localhost:8080/anagrams
HTTP/1.1 200
...
true

data.json holds  '{ "words": ["rare", "rear", "RARE", "Rear"] }'
$ curl -i -X POST -d "@data.json" -H "Content-Type: application/json"  http://localhost:8080/anagrams
HTTP/1.1 400
...
invalid English word [RARE]
```

#### Endpoint that identifies words with the most anagrams
```bash
$ curl -i http://localhost:8080/anagrams/group
HTTP/1.1 200 OK
...
[
  { "word": "dear", "count": 3 }
]
```
#### Endpoint to return all anagram groups of size >= x
```bash
$ curl -i http://localhost:8080/anagrams/group?size=1
HTTP/1.1 200 OK
...
[
  { "word": "dear", "count": 3 },
  { "word": "a", "count": 2 },
  { "word": "rare", "count": 2 }
]
```
#### Endpoint to delete a word and all of its anagrams

1. Deleting a word that is not in the database quietly fails

```bash
# Delete all anagrams for the word 'read'
$ curl -i -X DELETE http://localhost:8080/anagrams/read
HTTP/1.1 200
...
```