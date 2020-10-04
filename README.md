# Quinzical

JavaFX based quiz game created by Hajin Kim and Matt Moran.


The aim of the game is to take home the cash! Answer questions correctly to increase your winnings. But be warned, incorrect answers will penalise you!

## Runtime Requirements

- Java 8+ ([GitHub](https://github.com/openjdk/jdk), [installation instructions](https://openjdk.java.net/install/))
- JavaFX ([GitHub](https://github.com/openjdk/jfx), [download](https://gluonhq.com/products/javafx/))
- (optional) Espeak or Festival

## Development Requirements

- Java 8+ ([GitHub](https://github.com/openjdk/jdk), [installation instructions](https://openjdk.java.net/install/))
- JavaFX ([GitHub](https://github.com/openjdk/jfx), [download](https://gluonhq.com/products/javafx/))
- Gson ([GitHub](https://github.com/google/gson), [direct download `.jar`](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar))
- FX-Gson ([GitHub](https://github.com/joffrey-bion/fx-gson), [direct download `.jar`](https://repo1.maven.org/maven2/org/hildan/fxgson/fx-gson/3.1.2/fx-gson-3.1.2.jar))

## How to run it

Make sure you meet the __Runtime Requirements__.

You must have the following folders/files in one folder:
- `quinzical.jar` runnable jar file containing compiled JavaFX quiz application
- `categories` folder containing questions and icon resources
- `javafx-sdk-11.0.2` folder that contains JavaFX libraries
- `quinzical.sh` containing script that runs the application. 

**Option 1: Run with the script file**

Move into the folder that contains all of the folders/files above and run the script.

To do this, enter these two commands into the bash (assuming your current directory is the folder that contains the above folders/files):
- `chmod 777 quinzical.sh`
- `./script.sh`

**Option 2: Run from the runnable jar (for experts!)**

Download the [latest compiled release](https://github.com/SOFTENG206-2020/assignment-3-and-project-team-24/releases/latest/download/quinzical.jar).
Then run `java --module-path /path/to/javafx/lib/ --add-modules javafx.controls -jar quinzical.jar`.
 
Don't have JavaFX? Check out [jfxRunner](https://github.com/DarkMatterMatt/jfxrunner/releases/latest), which handles JavaFX
for you. Just run `java -jar jfxrunner.jar quinzical.jar` (you might have to wait for a moment while JavaFX is downloaded!).

## Features

- Our Quinzical application features _80+ NZ related questions, 7 categories_. That's huge!
- Advanced text-to-speech support for reading questions (`festival` must be in PATH). 
- Secret-sauce custom NZ voice with accurate pronunciation of Māori words
- Unlike our previous application, _Jeopardy_, our brand-new application comes with __Practice module__! Here you can get comfortable with our application interface, try out some questions before attempting the __Main module__.
- Want to start over during the quiz, or have to suddenly exit the game for a family dinner? We got you covered :) Exit at any screen and you will land on the exact page or question you were at last time.
- Customize you own categories and questions
- Amazing interface
- Cross-platform support
- Resizable and draggable window
- Questions support have multiple answers - never be incorrectly marked!

## Two Modules

There are two main modules in this game.

**Practice Module**\
You can attempt any of the categories and questions available from the supplied set of questions and categories! Press any category on the left, and you will get a random question selected from that category.\
Rules:
- You can attempt the question three times.
- The category will display the same question until you have attempted it 3 times, have correctly answered it, or pressed skip.
- You can skip question any time.
- You will not earn any money in this module.
- Hint will be displayed at the third attempt; the hint given is the first letter of one of the acceptable answers.

**Main Module**\
You will be greeted with a randomly selected set of 5 categories, each category containing randomly selected 5 questions. 
Rules:
- Clicking category gives you the lowest value question available in that category.
- The first question is worth $100, second one $200, third one $300, fourth one $400, and fifth one - the last question - $500.
- You will have ONE attempt for each question. You do not lose anything for incorrectly answering a question.

## Other auxiliary stuff that might be worth noting
- Taskbar (the top bar containing icons) contains speech slider, text visibility toggle button, back-to-home button, reset button (only available in the main module), exit button.
- Resetting the game will reset the main module progress and the score, and you will be taken back to the main screen.
- You can repeat the question as many times as you want by pressing the 
- You can drag around and resize the screen
- You can press keys or mouse during the display of correct/incorrect answer indication screen to skip that page.

## Usage
// with screenshot?
// TODO: annotated screenshots

## Customize Questions and Categories
The game reads categories from a `./categories` folder (relative to the current working directory).

Inside this folder, there should be one or more files which are named as their category.
<br>Example: `./categories/animals` is the `animals` category.

Each file is in a pipe-delimited format without a header row. The format is `question|answer1|answer2|...answerN`.
<br>Example: `What is the highest mountain in New Zealand|Mount Cook|Aorangi|Aoraki`.

Changes to categories and questions will not be processed until you reset the game.
