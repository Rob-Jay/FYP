# FYP

This is a readme for the fyp tited Text summarization using Ber Supervised by Mike Hinchey

This Project Contains the following

* Files for an android application that can be run on android studio and android virtual devices.
* An android APK file to run the application on a device if available.
* Python files where the Bert Summarizer is located.
* A .ipynb file containg a simpler version of the summarizer that shows the plotting of sentences.
* Node.JS files showing how the summarizer is hosted

## Running the Application in android studio.
To run this application with android studio you will need to app folder located within this repository. From here you can run the application on a virtual device or connectyour android device.

## Running the Application with an APK file.
An APK file is also located within this repository. This APK file will also allow you tu run the application on an android device.

## Running the summarizer using the provided python files. 
To run the Summarizer using the provided python files. Your text you wish to have summarized msut be located in the Scan.txt file located at Node/Summarizer. The main.py file can be ran through the commandline. 'python main.py'.
The file will also look for a command line argument representing the amount of sentences you wish to have returned for example 'python main.py 3'. This will result in a summary of 3 sentences from Scan.txt being returned to the User.

## Running the summarizer using the .ipynb python file. 
This file is located at Node/Summarizer and is titled 'BERT Sentence transformer as function with plots' assuming you have all imports you can use this file to test the summarizer.

## Node.js
The node js file shows how the text and numeric input is handled from the user. The numeric value is sued within the commandline to run the main.py file. The text is written into Scan.txt




