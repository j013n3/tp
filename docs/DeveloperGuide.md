# Developer Guide

## Table of Contents

* [Acknowledgements](#acknowledgements)
* [Introduction](#introduction)
* [Design](#design)
* [Commands and Implementation](#commands-and-implementation)
* [Appendix: Requirements](#appendix-requirements)
* [Appendix: Manual Testing](#appendix-manual-testing)
* [Frequently Asked Questions](#frequently-asked-questions)

## Acknowledgements

Our team has referenced [Address Book (Level-3)](https://github.com/se-edu/addressbook-level3) referenced for their [User Guide (UG)](https://se-education.org/addressbook-level3/UserGuide.html) and [Developer Guide (DG)](https://se-education.org/addressbook-level3/DeveloperGuide.html) to better structure our own Developer Guide.

- The `java.util.Scanner` class from the Java Standard Library is used for reading user input.
- The `JUnit 5` testing framework is used for writing and running unit tests.
- {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Introduction

The purpose of this guide is to provide an explanation for all the functions and internal workings in PulsePilot. This enables any technical readers to get a detailed understanding of the application's implementation, making it easier for them to contribute to the project or adapt it according to their preferences.

###### [Back to table of contents](#table-of-contents)

## Design

* [Overview of Components](#overview-of-components)
* [UI](#ui-package)
    * [Handler](#handler)
    * [Output](#output)
* [Workout](#workout-package)
    * [WorkoutList](#workout-list)
    * [Gym](#gym)
        * [GymStation](#gym-station)
        * [GymSet](#gym-set)
    * [Run](#run)
* [Health](#health-package)
    * [HealthList](#health-list)
    * [Bmi](#bmi)
    * [Period](#period)
    * [Appointment](#appointment)
* [Utility](#utility-package)
    * [Parser](#parser)
    * [Validation](#validation)
    * [CustomExceptions](#custom-exceptions)
    * [Filters](#filters)
* [Storage](#storage-package)
    * [LogFile](#log-file)
    * [DataFile](#data-file)
* [PulsePilot](#pulsepilot-package)
* [Constants](#constants-package)

### Overview of Components

The application follows an Object-Oriented Design approach, with separate classes for handling different components of the application, such as user input, output, exercise logging, and health data management.


![ArchitectureDiagram](![img.png](img/architecture_diagram.png)

The **_Architecture Diagram_** given above explains the high-level design of the PulsePilot.

Given below is a quick overview of main components and how they interact with each other.

`Main` is responsible for the initialising, processing of user input and termination of the bot. It creates a `PulsePilot` instance.

The application can be further broken down into the following packages:

- `Ui`: The user interface of PulsePilot.
- `Storage`: Contains the data storage components for PulsePilot.
- `Health`: Stores health-related information.
- `Workout`: Stores workout-related information.
- `Utility`: Contains utility functions, such as input parsing and validation.
- `Constants`: Contains all constants used in PulsePilot.

###### [Back to table of contents](#table-of-contents)

### UI Package

The `UI` package contains `Handler` and `Output`, which are responsible for handling user input and printing of output fo the screen respectively.

![UI Package Class Diagram](img/ui_class_diagram.png)

#### Handler

The main entry point of the application is the `Handler` class, used to handle user input. When the user starts the bot, the steps are as follows:

1. `initialiseBot()` is called, and it prompts the user to enter their name, which is used throughout the bot.
2. `initialiseScanner()` is used to create the singleton `Scanner` instance to read user input.
3. When the user enters input, it is passed to `processInput()`, which determines the type of command inputted.
4. Once the correct input has been determined, it is passed to the relevant handle function. For example, a `workout` command is passed to `handleWorkout()`.

On termination of the bot, `destroyScanner()` is called to close the `Scanner` created, and `terminateBot()` is called to save the data stored within the bot and exit gracefully.

###### [Back to table of contents](#table-of-contents)

#### Output

The `Output` class is responsible for printing messages, prompts, errors and other information to the terminal for the user.

{To include description and `Output` class diagram}

###### [Back to table of contents](#table-of-contents)

### Workout Package

1. `Workout` is a class that stores the date of the workout.
2. `Run` is a subclass of Workout and stores the distance, time, pace, and date of the run.
3. `Gym` is a subclass of Workout and stores the date and an array of `GymStation` objects
4. `GymStation` stores the name of the gym station, number of sets, and an array of `GymSet` objects.
5. `GymSet` stores the weight and repetitions for a particular set.
6. `WorkoutList` is a class that stores an array list different `Workout` objects using ArrayList.

![Workout Package Class Diagram](img/workout_class_diagram.png)

###### [Back to table of contents](#table-of-contents)

#### Workout List

{Include `WorkoutList` description}

###### [Back to table of contents](#table-of-contents)

#### Gym

{Include `Gym` description}

###### [Back to table of contents](#table-of-contents)

##### Gym Station

{Include `GymStation` description}

###### [Back to table of contents](#table-of-contents)

##### Gym Set

{Include `GymSet` description}

###### [Back to table of contents](#table-of-contents)

#### Run

{Include `Run` description}

###### [Back to table of contents](#table-of-contents)

### Health Package
![Class Diagram for Health Package](img/health_package.png)
The Health component consists of `Health`, `HealthList`, `Bmi`, `Period`, and `Appointment`.

1. `Health` class stores date.
2. `HealthList`class stores separate lists for different `Health` objects using ArrayList. It includes methods to add, delete, view history of the various `Health`lists.
3. `Bmi`class stores bmi attributes (i.e. height, weight, date, bmi value and bmi category).
4. `Period`class stores period attributes (i.e. start date of period, end date of period, period length and cycle length).
5. `Appointment`class stores appointment attributes (i.e. date, time, appointment description). Primarily, `Appointment` has all necessary getter methods to access the attributes.

###### [Back to table of contents](#table-of-contents)

#### Health List

**Attributes**
1. `logFile`: Represents a log file for logging health-related activities. 
2. `BMIS`: ArrayList containing BMI records. 
3. `APPOINTMENTS`: ArrayList containing appointment records.
4. `PERIODS`: ArrayList containing period records.

**Methods:**
1. `addBmi(Bmi bmi)`: Adds a BMI object to the list of BMIs. 
2. `showCurrentBmi()`: Prints the most recently added BMI record. 
3. `showBmiHistory()`: Prints all BMI entries recorded. 
4. `addPeriod(Period period)`: Adds a period record to the list of periods. 
5. `showLatestPeriod()`: Prints the latest period record added. 
6. `showPeriodHistory()`: Prints all period entries tracked. 
7. `printLatestThreeCycles()`: Prints the latest three period objects from the periods list. 
8. `getPeriods()`: Retrieves the list of period records. 
9. `getBmis()`: Retrieves the list of BMI records.
10. `getAppointments()`: Retrieves the list of appointment records. 
11. `getPeriodSize()`: Retrieves the number of periods recorded. 
12. `getPeriod(int index)`: Gets the period object at the specified index. 
13. `predictNextPeriodStartDate()`: Predicts the start date of the next period based on the average cycle length of the last three cycles. 
14. `clearHealthLists()`: Clears the lists of BMIs, periods, and appointments. 
15. `getPeriodsSize()`: Retrieves the size of the periods list. 
16. `getBmisSize()`: Retrieves the size of the BMIs list. 
17. `deleteBmi(int index)`: Deletes a BMI record based on the index. 
18. `deletePeriod(int index)`: Deletes a period record based on the index. 
19. `addAppointment(Appointment appointment)`: Adds an appointment to the list of appointments. 
20. `deleteAppointment(int index)`: Deletes an appointment based on the index.
21. `showAppointmentList()`: Prints all appointment entries tracked. 
22. `showLatestAppointment()`: Prints the latest appointment record added.

###### [Back to table of contents](#table-of-contents)

#### BMI
**Attributes**
1. `height`: A double value representing the height.
2. `weight`: A double value representing the weight.
3. `bmiValue`: A double value representing the calculated BMI value.
4. `bmiCategory`: A String representing the BMI category
5. `date`: A LocalDate object representing the date.

**Methods:**
1. `Bmi(String height, String weight, String date)`: The constructor of the Bmi class, which takes height, weight, and date as string parameters.
2. `getBmiValue()`: Returns the calculated BMI value as a string.
3. `getHeight()`: Returns the height value as a string.
4. `getWeight()`: Returns the weight value as a string.
5. `calculateBmiValue()`: Calculates the BMI value based on the provided height and weight.
6. `getBmiCategory(double bmiValue)`: Determines and returns the BMI category as a string based on the calculated BMI value.
7. `toString()`: Returns a string representation of the Bmi object.

###### [Back to table of contents](#table-of-contents)

#### Period

**Attributes**
1. `startDate`: Represents the start date of the period 
2. `endDate`: Represents the end date of the period 
3. `periodLength`: Stores the length of the period in days 
4. `cycleLength`: Stores the length of the menstrual cycle.

**Methods:**
1. `Period(String stringStartDate, String stringEndDate)`: The constructor of Period class which takes start date and end date of period.
2. `getStartDate()`: Retrieves the start date of the period.
3. `getEndDate()`: Retrieves the end date of the period
4. `getPeriodLength()`: Retrieves the length of the period
5. `calculatePeriodLength()`: Calculates the length of the period in days.
6. `setCycleLength(LocalDate nextStartDate)`: Sets the cycle length of the current period based on the start date of the next period.
7. `nextCyclePrediction()`: Predicts the start date of the next period based on the average cycle length obtained from the sum of the latest three menstrual cycle lengths.
8. `printNextCyclePrediction(LocalDate nextPeriodStartDate)`: Prints a message indicating the number of days until the predicted start date of the next period, or how many days late the period is.
9. `toString()`: Returns a string representation of the Period object, including the start date, end date, and period length and cycle length if present.

###### [Back to table of contents](#table-of-contents)

#### Appointment

**Attributes**
1. date: Represents the date of the appointment.
2. time: Represents the time of the appointment.
3. description: Stores a description of the appointment.

**Methods:**
1. Appointment(String stringDate, String stringTime, String description): The constructor of Appointment which takes date, time, and description.
2. getDate(): Retrieves the date of the appointment.
3. getTime(): Retrieves the time of the appointment.
4. getDescription(): Retrieves the description of the appointment.
5. toString(): Returns a string representation of the Appointment object, including the date, time, and description.

###### [Back to table of contents](#table-of-contents)

### Utility Package

The `Utility` package includes classes and methods that handle exceptions, user input parsing, user input validation, and the various filter strings using enumerations.

It consists of `CustomExceptions`, `Filters`, `Parser` and `Validation` classes.

{Include `Utility` class diagram}

###### [Back to table of contents](#table-of-contents)

#### Parser

{Include `Parser` description}

###### [Back to table of contents](#table-of-contents)

#### Validation

{Include `Validation` description}

###### [Back to table of contents](#table-of-contents)

#### Custom Exceptions

{Include `CustomException` description}

###### [Back to table of contents](#table-of-contents)

#### Filters

{Include `Filters` description}

###### [Back to table of contents](#table-of-contents)

### Storage Package

`Storage` contains `DataFile` and `LogFile`. This component handles all logging of commands used and writing of data stored within PulsePilot to an external data file. The reading of the data file is also done here.

{Include `Storage` class diagram}

The Storage component consists of LogFile, DataFile, and DataType.

###### [Back to table of contents](#table-of-contents)

#### Log File

###### [Back to table of contents](#table-of-contents)

#### Data File

###### [Back to table of contents](#table-of-contents)

### PulsePilot Package

###### [Back to table of contents](#table-of-contents)

### Constants Package

###### [Back to table of contents](#table-of-contents)

## Commands and Implementation

* [Workout](#workout)
    * [Add Run](#add-run)
    * [Add Gym](#add-gym)
* [Health](#health)
    * [Add Period](#add-period)
    * [Add BMI](#add-bmi)
    * [Add Appointment](#add-appointment)
    * [Make Period Prediction](#make-period-prediction)
* [View History](#view-history)
* [View Latest](#view-latest)
* [Delete Item](#delete-item)
* [Storage of Data](#storage-of-data)

### Workout

#### Add Run

<code style="color: #D85D43;">
WORKOUT /e:run /d:[distance] /t:[time] /date:[date]
</code>

- `[distance]` is a 2 **decimal place positive number** representing the number of kilometers covered.
- `[time]` is in `HH:MM:SS` or `MM:SS` format with positive integers, representing the amount of time taken for the run.
    - If the time taken is **25 minutes and 20 seconds**: Use `25:20`.
    - If the time taken is **1 hour, 20 minutes and 30 seconds**: Use `01:20:30`.
- `[date]` is in `DD-MM-YYYY` format. **THIS IS AN OPTIONAL PARAMETER**.

##### Run Sequence

1. User input is passed to `Handler.processInput()`, which determines the command used is `workout`, thus passing the input to `Handler.handleWorkout()`.

2. `Handler.handleWorkout()` determines the type of exercise which is `run`, and calls the `Parser.parseRunInput()` method to process the user's input.

3. `Parser.parseRunInput()` splits the input using `Parser.splitRunInput()`. It then validates each input using `Validation.validateRunInput()`.
    - `CustomExceptions.InsufficientInput` is thrown if either not enough parameters are specified or blank parameters are found.
    - `CustomExceptions.InvalidInput` is thrown if the parameters passed in are invalid and do not follow the stipulated format.

4. If valid, a new `Run` object is created with the split user input.

5. The `Run` constructor adds the newly created object into `WorkoutList.WORKOUTS` and `WorkoutList.RUNS`.

6. The `Run` object is passed to `Output.printAddRun()` and a message acknowledging the successful adding is printed to the screen.

This is the sequence diagram for adding a run:

![Run Sequence Diagram](img/run_sequence_diagram.png)

###### [Back to table of contents](#table-of-contents)

#### Add Gym

<code style="color: #D85D43;">
WORKOUT /e:gym /n:[number_of_stations] /date:[date]
</code>

- `[number_of_stations]` is a **positive integer** representing the number of gym stations done.
- `[date]` is in `DD-MM-YYYY` format. **THIS IS AN OPTIONAL PARAMETER**.

The bot will then prompt the user for the specific gym details in this format:

<code style="color: #D85D43;">
[station_name] /s:[number_of_sets] /r:[number_of_repetitions] /w:[weight]
</code>

- `[station_name]` is a string containing **only alphanumeric characters and spaces**, representing the station name.
- `[number_of_sets]` is a **positive integer** representing the number of sets done for the current station.
- `[number_of_repetitions]` is a **positive integer** representing the number of repetitions done for the current station.
- `[weight]` is a string containing **positive integers separated by commas**, representing the weight done for each set in kilograms.
    - For example, if the user has done 3 sets of 10kg, 20kg and 15kg, the string entered is `10,20,15`.
    - **No spaces can be present in this string**.

Example of Gym Station:

<code style="color: #D85D43;">Bench Press /s:2 /r:3 /w:100,200 </code>

- Station name is `Bench Press`.
- User has done **2 sets of 3 repetitions**.
- The first set was done using 100kg, and the second using 200kg.

> NOTE: The number of sets entered matches the number of weights added. If 4 sets have been done, the bot expects 4 positive inters separated by commas as the `weights` input.

##### Gym Sequence

![Gym Sequence Diagram](./img/gym_sequence_diagram.jpg)


1. User input is passed to `Handler.processInput()`, which determines the command used is `workout`, thus passing the input to `Handler.handleWorkout()`.

2. `Handler.handleWorkout()` determines the type of exercise which is `gym`, and calls the `Parser.parseGymInput()` method to process the user's input.

3. `Parser.parseGymInput()` splits the input using `Parser.splitGymInput()`. It then validates each input using `Validation.validateRunInput()`.
    - `CustomExceptions.InsufficientInput` is thrown if either not enough parameters are specified or blank parameters are found.
    - `CustomExceptions.InvalidInput` is thrown if the parameters passed in are invalid and do not follow the stipulated format.

4. If valid, a new `Gym` object is created with the split user input.

5. The `Gym` constructor adds the newly created object into `WorkoutList.WORKOUTS` and `WorkoutList.GYMS`.

6. `Parser.parseGymStationInput()` is then called to get the user's input for each station done.
    - This method calls `Output.printGymStationPrompt()` to print the details required from the user.

7. `Validation.splitAndValidateGymStationInput` is called to both split and validate the user's input for the gym station.
    - This method calls `Validation.validateWeightsArray` to ensure the `weights` input is valid.
    - `CustomExceptions.InsufficientInput` is thrown if either not enough parameters are specified or blank parameters are found.
    - `CustomExceptions.InvalidInput` is thrown if the parameters passed in are invalid and do not follow the stipulated format.

8. If valid, parameters are converted into either `int` for the number of repetitions and sets, or `Arraylist<Integer>` for the weights array. The parameters are then passed to `gym.addStation` to add a `GymStation` object to the new `Gym` object.

9. `Output.printAddGym()` is then called to print the message acknowledging the successful adding of a new `Gym` object.

###### [Back to table of contents](#table-of-contents)

### Health

#### Add Period
<code style="color: #D85D43;">
HEALTH /h:period /start:[start_date] /end:[end_date]
</code>

- `[start_date]`  and `[end_date]` are in DD-MM-YYYY format.

##### Period Sequence

The sequence diagram below illustrates the process of period prediction.

1. The Handler class receives `userInput` through `Handler.processInput()` which calls `Handler.handleHealth` to handle health-related operations.
2. `Parser.extractSubstringaFromSpecificIndex()` is then called to extract `typeOfHealth`, in this case, `prediction`. 
3. The Handler class then calls `Parser.parsePredictionInput()`.
4. `HealthList.getPeriodSize()` is then called to retrieve the size of `Periods` to ensure sufficient `Period` inputs are present for prediction.
5. `HealthList.printLatestThreeCycles()` prints the user's latest three menstrual cycles.
6. `HealthList.predictNextPeriodStartDate()` gets the predicted start date by calling `Period.nextCyclePrediction()`.
7. `Period.getLastThreeCycleLengths()` is called to find the sum of the latest three cycle lengths before returning to `Period.nextCyclePrediction()` for necessary calculation to obtain the predicted start date.
8. The `Parser` class calls `Period.printNextCyclePrediction()` with the predicted start date as a parameter. This method prints a string indicating the number of days until the predicted start date of the next period, or how many days late the period is if the current date is after the predicted start date.
![Sequence Diagram for Period Prediction](img/period_sequence.png)

###### [Back to table of contents](#table-of-contents)

#### Add BMI

<code style="color: #D85D43;">
HEALTH /h:bmi /height:[height] /weight:[weight] /date:[date]
</code>

- `[height]` is a 2 **decimal place positive number** representing the user's height.
- `[weight]`is a 2 **decimal place positive number** representing the user's weight.
- `[date]` is in `DD-MM-YYYY` format (i.e. `19-03-2024`).

##### BMI Sequence

The sequence diagram below shows how a `Bmi` object is added to `BMIS`.

1. Upon receiving `userInput` in `Handler.processInput()`, `Handler.handleHealth()` is called.
2. `Parser.extractSubstringaFromSpecificIndex()` is then called to extract `typeOfHealth`, in this case, `bmi`.
3. The Handler class then calls `Parser.parseBmiInput()`, passing in `userInput`.
4. `Parser.splitBmiInput` splits the input and returns `bmiDetails` which consists of height, weight and date.
5. The `Parser` class calls `Validation.validateBmiInput` to validate `bmiDetails` using `Validation.validateDataInput()` and `Validation.validateDateNotAfterToday()` methods.
6. Upon successful validation, a new `Bmi` object is created based on `bmiDetails` with `bmiValue` obtained from `Bmi.calculateBmiValue()` and the corresponding BMI category obtained from `Bmi.getBmiCategory()`.
7. `HealthList.addBmi()` adds the newly created `Bmi` into `BMIS`.
8. `Output.printAddBmi()` prints `Bmi` string containing height, weight, date, BMI and BMI category to user.
![Sequence Diagram for Add Bmi](img/bmi_sequence.png)

###### [Back to table of contents](#table-of-contents)

#### Add Appointment

<code style="color: #D85D43;">
HEALTH /h:appointment /date:[date] /time:[time] /description:[description]
</code>

- `[date]` is in `DD-MM-YYYY` format representing the date of the appointment.
- `[time]` is in `HH:mm` format representing the time of the appointment.
- `[description]` is a string  representing the details of the appointment. The string can only contain alphanumeric characters and spaces.

##### Appointment Sequence

1. User input is passed to `Handler.processInput()`, which determines the command used is `health`, thus passing the input to `Handler.handleHealth()`.

2. `Handler.handleHealth()` determines the type of health which is `appointment`, and calls the `Parser.parseAppointmentInput()` method to process the user's input.

3. `Parser.parseAppointmentInput()` splits the input using `Parser.splitAppointmentDetails()`. It then validates each input using `Validation.validateAppointmentDetails()`.
    - `CustomExceptions.InsufficientInput` is thrown if either not enough parameters are specified or blank parameters are found.
    - `CustomExceptions.InvalidInput` is thrown if the parameters passed in are invalid and do not follow the stipulated format.

4. If valid, a new `Appointment` object is created with the split user input.

5. The `Appointment` constructor adds the newly created object into `HealthList.APPOINTMENTS`.

6. The `Appointment` object is passed to `Output.printAddAppointment()` and a message acknowledging the successful adding is printed to the screen.

![AddAppointment](img/appointment_sequence.png)

###### [Back to table of contents](#table-of-contents)

#### Make Period Prediction
<code style="color: #D85D43;">
HEALTH /h:prediction 
</code>

The sequence diagram below illustrates the process of period prediction.

1. The Handler class receives `userInput` through `Handler.processInput()` which calls `Handler.handleHealth` to handle health-related operations.
2. `Parser.extractSubstringaFromSpecificIndex()` is then called to extract `typeOfHealth`, in this case, `prediction`. 
3. The Handler class then calls `Parser.parsePredictionInput()`.
4. `HealthList.getPeriodSize()` is then called to retrieve the size of `Periods` to ensure sufficient `Period` inputs are present for prediction.
5. `HealthList.printLatestThreeCycles()` prints the user's latest three menstrual cycles.
6. `HealthList.predictNextPeriodStartDate()` gets the predicted start date by calling `Period.nextCyclePrediction()`.
7. `Period.getLastThreeCycleLengths()` is called to find the sum of the latest three cycle lengths before returning to `Period.nextCyclePrediction()` for necessary calculation to obtain the predicted start date.
8. The `Parser` class calls `Period.printNextCyclePrediction()` with the predicted start date as a parameter. This method prints a string indicating the number of days until the predicted start date of the next period, or how many days late the period is if the current date is after the predicted start date.
![Sequence Diagram for Add Period](img/period_prediction.png)

###### [Back to table of contents](#table-of-contents)

### View History

###### [Back to table of contents](#table-of-contents)

### View Latest

###### [Back to table of contents](#table-of-contents)

### Delete Item

###### [Back to table of contents](#table-of-contents)

### Storage of Data

###### [Back to table of contents](#table-of-contents)

## Appendix: Requirements

* [Product Scope](#product-scope)
    * [Target User Profile](#target-user-profile)
    * [Value Proposition](#value-proposition)
* [User Stores](#user-stories)
* [Non-Functional Requirements](#non-functional-requirements)
* [Glossary](#glossary)

### Product scope

#### Target user profile

Outpatients who need to monitor their health activity and health parameters.

###### [Back to table of contents](#table-of-contents)

#### Value proposition

PulsePilot is a health monitoring application designed to bridge the gap between medical professionals and patients during outpatient recovery.

PulsePilot offers outpatients the capability to input and track a range of health activities, encompassing both aerobic and anaerobic exercises, alongside crucial health parameters such as BMI and menstrual cycles.

Simultaneously, PulsePilot facilitates access to this vital data for various healthcare professionals, ensuring comprehensive and seamless support in guiding outpatient recovery processes.

###### [Back to table of contents](#table-of-contents)

### User Stories

| Version | As a ...              | So that I can ...                                     | I want to ...                         |
|---------|-----------------------|-------------------------------------------------------|---------------------------------------|
| 1.0     | gym enthusiast        | track my gym sessions                                 | enter my gym stats                    |
| 1.0     | runner                | see my relative speed for each run                    | see my running pace                   |
| 1.0     | runner                | track my running progress over time                   | log my runs                           |
| 1.0     | health conscious user | track change in my weight over time                   | calculate my BMI                      |
| 1.0     | female user           | monitor any deviations from my normal menstrual cycle | track my menstrual cycle              |
| 2.0     | runner                | quickly view my most recent run details               | see my latest run                     |
| 2.0     | gym enthusiast        | quickly view my most recent gym session               | see my latest gym session             | 
| 2.0     | gym enthusiast        | accurately track my progress and strength gains       | enter varying weights for sets        | 
| 2.0     | female user           | plan ahead and better manage my health                | p#D85D43ict my next period start date | 
| 2.0     | injured user          | remember the appointments I have                      | track my medical appointments         |

###### [Back to table of contents](#table-of-contents)

### Non-Functional Requirements

- **Usability**: The application should have a user-friendly command-line interface with clear instructions and prompts for user input.
- **Reliability**: The application should handle invalid or incomplete user input gracefully, providing appropriate error messages and prompting the user for correct input.
- **Maintainability**: The codebase should follow best practices for Object-Oriented Programming, including proper separation of concerns, modularization, and code documentation.
- **Testability**: The application should have comprehensive unit tests to  ensure correct functionality and enable easier maintenance and future enhancements.

###### [Back to table of contents](#table-of-contents)

### Glossary

- **Run**: An exercise activity involving running or jogging, typically characterized by distance, duration, and date.
-  **Gym**: An exercise activity involving various strength training exercises or workouts performed at a gym or fitness center.
- **BMI (Body Mass Index)**: A measure of body fat based on height and weight, used to assess overall health and fitness.
- **Menstrual Period**: A recurring physiological event in females, characterized by the start and end dates.
- **Medical Appointment**: An arrangement with a doctor, physiotherapist, or healthcare professional, to meet at a certain time and place.

###### [Back to table of contents](#table-of-contents)

## Appendix: Manual Testing

* [Launching and Termination](#launching-and-termination-testing)
* [Workout](#workout-testing)
    * [Run](#run-testing)
    * [Gym](#gym-testing)
* [Health](#health-testing)
    * [Period](#period-testing)
    * [Prediction](#prediction-testing)
    * [BMI](#bmi-testing)
    * [Appointment](#appointment-testing)
* [Storage](#storage-testing)

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}

###### [Back to table of contents](#table-of-contents)

### Launching and Termination Testing

###### [Back to table of contents](#table-of-contents)

### Workout Testing

#### Run Testing

###### [Back to table of contents](#table-of-contents)

#### Gym Testing

###### [Back to table of contents](#table-of-contents)

### Health Testing

#### Period Testing

###### [Back to table of contents](#table-of-contents)

#### Prediction Testing

###### [Back to table of contents](#table-of-contents)

#### BMI Testing

###### [Back to table of contents](#table-of-contents)

#### Appointment Testing

###### [Back to table of contents](#table-of-contents)

### Storage Testing

###### [Back to table of contents](#table-of-contents)

## Frequently Asked Questions

1. **Q: How do I set up the development environment for the project?**  
   A: You can set up the development environment by first cloning the repository to your local system. Then, load the project into your chosen IDE (we recommend IntelliJ IDEA).

###### [Back to table of contents](#table-of-contents)
