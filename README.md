# Anagrams Tech Task

## How to execute it
Execute `mvn exec:java -Dexec.mainClass="org.anagrams.Main"` from the source root directory.

To run the test execute `mvn test`.

## Usage
The application is an interactive CLI program.
### Feature 1
To use feature#1 introduce in the prompt a `1` followed by the two sentences used as arguments.
Examples:
```
1 evil vile
1 "New York Times" "monkeys write"
```
### Feature 2
To use feature#2, introduce a `2` followed by the argument.
Examples:
```
 2 evil
 2 "monkeys write"
```

Note: Examples took from [Wikipedia page about Anagrams](https://en.wikipedia.org/wiki/Anagram)