# JavaECHO for the Command Line

From the [JavaECHO home page](http://cogsci.uwaterloo.ca/JavaECHO/jecho.html):

> JavaECHO is an implementation of ECHO, a system for computing explanatory coherence developed by [Paul Thagard](http://cogsci.uwaterloo.ca/Biographies/pault.html) and explained in detail in his 1992 book *Conceptual Revolutions*.

This version is both updated and stripped down to run from the command line.  It was updated because the code was last updated in 2001 and much has changed with Java since then. In particular, several classes have been depreciated for performance and/or security reasons.  Most of my changes involved simply replacements of new classes for old:

- Replaced `Vector` with `ArrayList`
- Replaced `Hashtable` with `HashMap`, 
- Replaced `Enumeration` with `Iterator` -- just because the method `iterator()` is available for both `ArrayList` and `HashMap`
- Replaced `StringBufferInputStream` with `ByteArrayInputStream`

I dropped all the GUI classes and created a new main class `App` with the essential pieces pulled from the old GUI.

### Goal
The purpose for this code is to make JavaECHO more accessible to reasearchers, especially those who want to incorporate it into their agent architecture or otherwise modify or extend it.  Myself, I aim to do both, including possibly a port to Javascript in order to run it in modern browsers without needing a Java plugin.

Therefore, no other substantive changes or improvements will be made to this version except to set the execution parameters through the command line. Another extention I will add: read input from a file, since this would be a command line option and would preserve the existing code.

## Known Bugs

1. Currently, command line arguments don't work. This should be a simple fix. They were working just before I created this repo. :-)

## Install and Run Executable

1. Download `JavaECHO.jar` into a folder of your choice. It has all the dependencies included in it.
    - The SHA-1 checksum is: 047f507f17a4c8123ea6b084f50315f5fae89b38
2. Open a terminal window and then change directory until you are at the folder where you stored the JAR file.
    - All input and output are done through the terminal.
3. To run, type `java -jar JavaEcho.jar`, followed by any command line arguements

### Runtime Arguments
Currently, there is only one argument: "-t" followed by an integer "#" between 0 and 11 to select the input code + data:
    
| #  | Title | Description |
| ------------- | ------------- | ------------- |
| 0 | ulcers_1983  | Dominant peptic ulcer view in 1983 view that most peptic ulcers were caused by excess acidity  |
| 1 | ulcers_1994 |  Dominant peptic ulcer view in 1994 for bacterial theory, which is contrary to the excess acid theory |
| 2 | breadth | Explanatory breadth example,from p.74- 75 of *Conceptual Revolutions* |
| 3 | breadth2 | Sames as explanatory breadth example,from p.74-75 of *Conceptual Revolutions*, except contradict statement has been removed |
| 4 | analogy | Analogy, from p. 78-79 of *Conceptual Revolutions* |
| 5 | beingExplained | Being explained, from p.75-76 of *Conceptual Revolutions* |
| 6 | simplicity | Simplicity, from p. 77-78 of *Conceptual Revolutions*. JavaECHO also finds (correctly) that H1 and H3 compete, although this is not shown in *CR*. |
| 7 | unification | Unification, from p.76-77 of *Conceptual Revolutions*. JavaECHO finds a number of competiting explanations that are not mentioned in *CR*. |
| 8 | lavoisier | The Lavoisier example from pages 83-84 of *Conceptual Revolutions* |
| 9 | evidence | Evidence and acceptability, from p. 79-80 of *Conceptual Revolutions* |
| 10 | darwin | The Darwin example from page 144 of *Conceptual Revolutions* |
| 11 | wegener | Wegener example from p.184, Table 7.2 of *Conceptual Revolutions* |

## Install Source Code
1. Click the "Clone or Download" button.  "Clone" the git repo or "Download" a zip file.
2. I use NetBeans IDE and Maven for build and dependency management, so both `pom.xml` and 'nbactions.xml` files are included. If you use a different IDE and build software, you'll need to convert.

## Credits

- [Toby Donaldson](https://www.sfu.ca/computing/people/faculty/tobydonaldson.html), now Senior Lecturer, School of Computing Science at Simon Fraser University, is the the implementor of JavaECHO. 
- [Paul Thagard](http://cogsci.uwaterloo.ca/Biographies/pault.html), Emeritus Professor of Philosophy at University of Waterloo (retired Oct. 2016), is designer and author of ECHO.
    
    
