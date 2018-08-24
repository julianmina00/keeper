# Keeper of the Seven Keys - Full stack coding challenge

The Keeper of the Seven Keys is lost in a maze. There is an exit but it’s locked. He needs to find the seven keys to open the door and escape.

Use Java 8+ to code some logic capable of finding the keys and escape any random maze. Use any front end technology to create a web page that can display the maze and show the Keeper of the Keys finding its way out. Create a REST API that can communicate the front end and the back end.

Web users should be able to start/stop the game or reboot the system (creating a new maze). When the Keeper finds the seven keys and then the door, the web page should display a message indicating that the mission has been accomplished. 

## The framework

Some Java objects (**Maze**, **Position**), some interfaces (**Keeper**, **Observable**) and some enumerations (**Action**, **Cell**) will be provided to you. ***These classes cannot be modified***.

The maze is randomly generated and is represented by an array of 40x40 cells. Each Cell can be ***EMPTY***, a ***WALL***, a ***KEY*** or the ***DOOR***.

 ``` [language]
package tws.keeper.model;

/**
 * Represent the contents of a Cell
 */
public enum Cell {

    WALL("wall"), PATH("path"), KEY("key"), DOOR("door");

    private String stringValue;

    Cell(String theValue) {
        stringValue = theValue;
    }

    public String toString() {
        return stringValue;
    }

}
```

The keys, the door and the keeper are placed in random **Positions**. The maze can always be solved.

``` [language]
package tws.keeper.model;

public class Position {

    private final int vertical, horizontal;

    /**
     * Construct an immutable instance
     */
    public Position(int vert, int horz) {
        vertical = vert;
        horizontal = horz;
    }

    /**
     * Getter
     */
    public int getVertical() {
        return vertical;
    }

    /**
     * Getter
     */
    public int getHorizontal() {
        return horizontal;
    }

}
```

The **Maze** is **Observable**, which means that we can look up, down, left or right from our current Position, which will return a **Cell** value. We can also retrieve the total number of keys (it would be always 7), the keys that have already been found, and our current **Position**. We we can also know if the maze have been completed.

The logic of the keeper should implement the **Keeper** interface. The system should create a new **Maze** instance passing an instance of the keeper logic in the constructor. The system should then call the *act* method of the keeper, each time passing in an observable instance of the maze. The keeper logic should each time decide what to do next and return an Action. 

The system should keep calling the *act* method until the maze is solved or the user wants to stop the game.

The Keeper should eventually find the seven keys and then find the door, optimising the route as much as possible.

## The solution

First of all, build the keeper logic: a class that implements the **Keeper** interface and is able to solve any **Maze** by a finite number of calls to its act method.

Then build a game system that can instantiate keepers and mazes, keep track of the status of each particular game, etc. and build a REST API on top of it that makes this usable by the Front End.

Finally build a website where users can see the **Maze** and the **Keeper**, with UI controls to start the game (make the keeper solve the maze one step at a time), stop the game, generate a new maze, and anything else you consider useful or fun.

The solution must be written in Java and has to be compatible with the provided framework, which is written in Java 8.

It has to use **Maven** to compile, test and run.

Please upload the project to GitHub. It must have a readme.md file  that contains, at least:

* Your full name
* The mvn command required to build and start the site
* The local URL of the web page
* Instructions to operate the site
* Basic description of the architecture
* Basic description of the keeper AI

## Evaluation criteria

Different aspects of the solution will be taken into consideration for the final score:
#### Correctness 
The solution should solve the problem in the most practical way possible, ideally with no bugs or unhandled edge cases.
#### Design
OOP principles should be present throughout the solution. It should be simple, intuitive and easy to understand.
#### Performance
The solution should make optimal use of resources such as CPU, memory or disk.
#### Modularity
Use separate modules to a reasonable exempt, without over-engineering.
#### Technology
The solution should make use of frameworks and libraries to solve common problems. 
#### Tests
Everything should be reasonably unit tested. Tests should guarantee correctness. Edge cases or negative flows should be also tested.
#### Documentation
The code, the API and the site should all be documented (formally when possible).
#### Maintainability
The solution should be maintainable by a development team. This means, among other things, that it should be ready to accommodate future requirements with minimum effort.
#### Reusability
Whenever possible, parts of the solution should be ready to be reused in other projects. 
#### UI and UX
The site should be visually pleasing and fun to use.
#### Simplicity
Solve the challenge with as less code as possible without sacrificing any of the above.

## Hints

At the very least, the solution and the provided framework should be separate maven modules, the later been a dependency of the former.

Separation of concerns principles are always helpful.

Java sources should be documented with Javadoc. There are languages (such as Swagger or RAML) to formalise APIs that can help you produce good documentation.

Test coverage reports would be a plus.

We prefer JSON over XML.

Functional programming is powerful! And Java supports some of it.

Recursiveness is fun but could get you in trouble here. Use at your own risk.

SpringBoot can help you set up a server for the website and the API in no time.

Front End can be pure HTML+CSS with some JS. We like Angular and React too!

There are many different devices and browsers out there. Responsive design is a plus.

Images are great. SVG have some cool advantages.

**Good luck!**
