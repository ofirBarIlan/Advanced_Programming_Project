# Java Scrabble Project
This repository contains a Scrabble application developed using the MVVM (Model-View-ViewModel) architecture. The application allows players to interact with a user-friendly GUI built with JavaFX, where they can play the game and receive real-time feedback on word validity and points. The players can play alone, or alongside other players through a web application. In addition, games can be saved and loaded as the players wish.

# Architecture Overview
The Scrabble app follows the MVVM architecture pattern, which separates the concerns of the user interface (View) from the underlying logic and data (Model). The ViewModel acts as the mediator between the View and the Model.

View: The View component consists of the graphical user interface (GUI) developed using JavaFX. It provides a visually appealing and intuitive interface for players to interact with the game. Any user actions, such as button presses, are translated into code in the JavaFX controller.

ViewModel: The ViewModel is responsible for handling the communication between the View and the Model. It receives user input from the View and forwards it to the Model for processing. Likewise, it retrieves data and results from the Model and updates the View accordingly. The ViewModel serves as a bridge between the two.

Model: The Model represents the "brain" of the Scrabble application. It contains the game logic and data. The Model receives input from the ViewModel, processes it, and returns results back to the ViewModel. It also has access to a project that we created in last year's course. This project allows it to use a server component in order to access a dictionary and determine word validity and point calculations. 

# Usage
To use the Scrabble app, follow these steps:

Clone the repository to your local machine:

git clone https://github.com/ofirBarIlan/Advanced_Programming_Project.git

Ensure you have Java Development Kit (JDK) installed on your system.

Install the MongoDB client, and make sure that you can access the database in order to enable save and load feature.

Build the project and resolve any dependencies if necessary.

Run the application in the following way:

- Run the server, in a java class called ServerRun.java
  
- Run the users, in the App.java class
  
- A GUI will open up. Decide if playing as guest or host, single or multiplayer, and enter port number, as well as name and if to create a new game or to load an existing game.
  
Enjoy!

# Acknowledgements
Ofir, Bar, Yanai, Tomer 

# Division into sub-teams + GANTT chart
[Division into sub-teams + GANTT chart.xlsx](https://github.com/ofirBarIlan/Advanced_Programming_Project/files/11585305/Division.into.sub-teams.%2B.GANTT.chart.xlsx)

# Link to video and the presentation
https://drive.google.com/drive/folders/1gG60Xp2j-INrTy-UsE_x-uVzrwb1MLcj?usp=sharing
