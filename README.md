# Quinzical

JavaFX based quiz game created by Hajin Kim and Matt Moran.


## Aim of the game

The aim of the game is to take home the cash! Answer questions correctly to increase your winnings,
but be warned, incorrect answers will penalise you!


## Features

- Play more than 80 unique questions over 7 categories
- Customize you own categories and questions
- Amazing interface
- Cross-platform support
- Advanced text-to-speech support for reading questions (`festival` must be in PATH)
- Secret-sauce custom NZ voice with accurate pronunciation of Māori words
- Resizable and draggable window
- Questions support have multiple answers - never be incorrectly marked!


## Categories & Questions

The game reads categories from a `./categories` folder (relative to the current working directory).

Inside this folder, there should be one or more files which are named as their category.
<br>Example: `./categories/animals` is the `animals` category.

Each file is in a pipe-delimited format without a header row. The format is `question|answer1|answer2|...answerN`.
<br>Example: `What is the highest mountain in New Zealand|Mount Cook|Aorangi|Aoraki`.

Changes to categories and questions will not be processed until you reset the game.


## Runtime Requirements

- Java 8+ ([GitHub](https://github.com/openjdk/jdk), [installation instructions](https://openjdk.java.net/install/))
- JavaFX ([GitHub](https://github.com/openjdk/jfx), [download](https://gluonhq.com/products/javafx/))


## Development Requirements

- Java 8+ ([GitHub](https://github.com/openjdk/jdk), [installation instructions](https://openjdk.java.net/install/))
- JavaFX ([GitHub](https://github.com/openjdk/jfx), [download](https://gluonhq.com/products/javafx/))
- Gson ([GitHub](https://github.com/google/gson), [direct download `.jar`](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar))
- FX-Gson ([GitHub](https://github.com/joffrey-bion/fx-gson), [direct download `.jar`](https://repo1.maven.org/maven2/org/hildan/fxgson/fx-gson/3.1.2/fx-gson-3.1.2.jar))


## Usage

Download the [latest compiled release](https://github.com/SOFTENG206-2020/assignment-3-and-project-team-24/releases/latest/download/quinzical.jar).
Then run `java --module-path /path/to/javafx/lib/ --add-modules javafx.controls -jar quinzical.jar`.
 
Don't have JavaFX? Check out [jfxRunner](https://github.com/DarkMatterMatt/jfxrunner/releases/latest), which handles JavaFX
for you. Just run `java -jar jfxrunner.jar quinzical.jar` (you might have to wait for a moment while JavaFX is downloaded!).

// TODO: annotated screenshots
