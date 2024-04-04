package ui;

import health.HealthList;
import storage.DataFile;
import utility.CustomExceptions;
import constants.ErrorConstant;
import constants.UiConstant;
import constants.HealthConstant;
import constants.WorkoutConstant;
import utility.Filters.Command;
import utility.Filters.DeleteFilters;
import utility.Filters.HealthFilters;
import utility.Parser;
import utility.Filters.WorkoutFilters;
import workouts.WorkoutList;

import java.util.Scanner;
import storage.LogFile;

/**
 * Represents user input parsing and handling
 * before providing feedback to the user.
 */
public class Handler {
    public static Scanner in;
    static LogFile logFile = LogFile.getInstance();

    /**
     * Processes user input and filters for valid command words from enum {@code Command},
     * then creates the relevant object based on details entered.
     *
     * @throws IllegalArgumentException If an error occurs during command processing.
     */
    public static void processInput() {
        while (in.hasNextLine()) {
            String userInput = in.nextLine();
            String instruction = userInput.toUpperCase().split(UiConstant.SPLIT_BY_WHITESPACE)[0];
            LogFile.writeLog("User Input: " + userInput, false);

            assert userInput != null : "Object cannot be null";

            try {
                Command command = Command.valueOf(instruction);
                switch (command) {
                case EXIT:
                    LogFile.writeLog("User ran command: exit", false);
                    System.out.println(UiConstant.EXIT_MESSAGE);
                    return;

                case WORKOUT:
                    handleWorkout(userInput);
                    break;

                case HEALTH:
                    handleHealth(userInput);
                    break;

                case HISTORY:
                    handleHistory(userInput);
                    break;

                case LATEST:
                    handleLatest(userInput);
                    break;

                case DELETE:
                    handleDelete(userInput);
                    break;

                case HELP:
                    Output.printHelp();
                    break;

                default:
                    break; // valueOf results in immediate exception for non-match with enum Command
                }
            } catch (CustomExceptions.InvalidInput e) {
                Output.printException(e.getMessage());
            } catch (IllegalArgumentException e) {
                Output.printException(ErrorConstant.INVALID_COMMAND_ERROR);
            }
        }
    }


    /**
     * Handles workout command.
     * Adds a Run or Gym object to PulsePilot.
     *
     * @param userInput The user input string.
     */
    //@@author JustinSoh
    public static void handleWorkout(String userInput) {
        try {
            String typeOfWorkout = Parser.extractSubstringFromSpecificIndex(userInput,
                    WorkoutConstant.EXERCISE_FLAG);
            WorkoutFilters filter = WorkoutFilters.valueOf(typeOfWorkout.toUpperCase());
            switch(filter) {
            case RUN:
                Parser.parseRunInput(userInput);
                break;

            case GYM:
                Parser.parseGymInput(userInput);
                break;

            default:
                break;
            }
        } catch (CustomExceptions.InvalidInput | CustomExceptions.InsufficientInput e) {
            Output.printException(e.getMessage());
        } catch (IllegalArgumentException e) {
            Output.printException("Invalid workout type! Please input either /e:run or /e:gym!");
        }
    }

    /**
     * Handles history command.
     * Show history of all exercises, run or gym.
     *
     * @param userInput The user input string.
     */
    public static void handleHistory(String userInput) {
        String filter = Parser.parseHistoryAndLatestInput(userInput);
        if (filter != null) {
            Output.printHistory(filter);
        }
    }

    /**
     * Handles the delete command.
     * Deletes an item stored within PulsePilot.
     *
     * @param userInput The user input string.
     */
    public static void handleDelete(String userInput) throws CustomExceptions.InvalidInput {
        String[] parsedInputs = Parser.parseDeleteInput(userInput);
        if (parsedInputs == null) {
            return;
        }
        try {
            DeleteFilters filter = DeleteFilters.valueOf(parsedInputs[0].toUpperCase());
            int index = Integer.parseInt(parsedInputs[1]) - 1;
            switch (filter) {
            case BMI:
                HealthList.deleteBmi(index);
                break;

            case PERIOD:
                HealthList.deletePeriod(index);
                break;

            case GYM:
                WorkoutList.deleteGym(index);
                break;

            case RUN:
                WorkoutList.deleteRun(index);
                break;

            case APPOINTMENT:
                HealthList.deleteAppointment(index);
                break;

            default:
                break;
            }
        } catch (CustomExceptions.OutOfBounds e) {
            Output.printException(e.getMessage());
        }
    }

    //@@author syj02
    /**
     * Handles user input related to health data. Parses the user input to determine
     * the type of health data and processes it accordingly.
     *
     * @param userInput A string containing health data information of user.
     */
    public static void handleHealth(String userInput) {
        try {
            String typeOfHealth = Parser.extractSubstringFromSpecificIndex(userInput, HealthConstant.HEALTH_FLAG);
            HealthFilters filter = HealthFilters.valueOf(typeOfHealth.toUpperCase());
            switch(filter) {
            case BMI:
                Parser.parseBmiInput(userInput);
                break;

            case PERIOD:
                Parser.parsePeriodInput(userInput);
                break;

            case PREDICTION:
                Parser.parsePredictionInput();
                break;

            case APPOINTMENT:
                Parser.parseAppointmentInput(userInput);
                break;

            default:
                break;
            }
        } catch (CustomExceptions.InvalidInput |  CustomExceptions.InsufficientInput e) {
            Output.printException(e.getMessage());
        } catch (IllegalArgumentException e) {
            Output.printException(ErrorConstant.INVALID_HEALTH_INPUT_ERROR);
        }
    }

    //@@author JustinSoh

    /**
     * Prints the latest run, gym, BMI entry or Period tracked.
     *
     * @param userInput String representing user input.
     */
    public static void handleLatest(String userInput) {
        String filter = Parser.parseHistoryAndLatestInput(userInput);
        if (filter != null) {
            Output.printLatest(filter);
        }
    }

    //@@author

    /**
     * Get user's name, and print profile induction messages.
     */
    public static void userInduction() {
        String name = in.nextLine();
        DataFile.userName = name;
        System.out.println("Welcome aboard, Captain " + name);
        Output.printLine();

        System.out.println("Tips: Enter 'help' to view the pilot manual!");
        System.out.println("Initiating FTL jump sequence...");

        // DataFile.saveName(name);
        LogFile.writeLog("Name Entered: " + name, false);

        System.out.println("FTL jump completed.");
    }

    /**
     * Initialise scanner to read user input.
     */
    public static void initialiseScanner() {
        in = new Scanner(System.in);
        assert in != null : "Object cannot be null";
    }

    /**
     * Close scanner to stop reading user input.
     */
    public static void destroyScanner() {
        if (in != null) {
            assert in != null : "Object cannot be null";
            in.close();
        }
    }

    /**
     * Initializes PulsePilot by printing a welcome message, loading tasks from storage,
     * and returning the tasks list.
     */
    public static void initialiseBot() {
        Output.printWelcomeBanner();
        initialiseScanner();
        LogFile.writeLog("Started bot", false);

        int status = DataFile.loadDataFile();

        if (status == 0) {
            try {
                DataFile.readDataFile(); // File read
                Output.printGreeting(status, DataFile.userName);
            } catch (CustomExceptions.FileReadError e) {
                Output.printException(e.getMessage());
            }
        } else {
            Output.printGreeting(status, DataFile.userName);
            userInduction();
        }

        System.out.println("Terminal primed. Command inputs are now accepted...");
        Output.printLine();
    }

    /**
     * Terminates PulsePilot by saving tasks to storage, printing a goodbye message,
     * and indicating the filename where tasks are saved.
     */
    public static void terminateBot() {
        LogFile.writeLog("User terminating PulsePilot", false);
        try {
            LogFile.writeLog("Attempting to save data file", false);


            DataFile.saveDataFile(DataFile.userName, HealthList.getBmis(), HealthList.getAppointments(),
                    HealthList.getPeriods(), WorkoutList.getWorkouts());
            LogFile.writeLog("File saved", false);
        } catch (CustomExceptions.FileWriteError e) {
            LogFile.writeLog("File write error", true);
            Output.printException(e.getMessage());
        }
        Output.printGoodbyeMessage();
        // Yet to implement : Reply.printReply("Saved tasks as: " + Constant.FILE_NAME);
        LogFile.writeLog("Bot exited gracefully", false);
        destroyScanner();
        System.exit(0);
    }
}
