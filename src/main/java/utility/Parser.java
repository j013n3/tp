package utility;

import constants.ErrorConstant;
import constants.HealthConstant;
import constants.UiConstant;
import constants.WorkoutConstant;
import ui.Handler;
import ui.Output;

import health.Appointment;
import health.Bmi;
import health.HealthList;
import health.Period;
import workouts.Gym;
import workouts.Run;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Represents the parser used to parse and split input for PulsePilot.
 */
public class Parser {

    /**
     * Counts the number of '/' characters there are in a given string.
     *
     * @param input The user input string.
     * @return An integer representing the number of '/' characters there are.
     */
    public static int countForwardSlash(String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '/') {
                count++;
            }
        }
        return count;
    }

    /**
     * Parses and converts String date to a LocalDate variable.
     *
     * @param date String representing the date.
     * @return LocalDate variable representing the date.
     * @throws DateTimeParseException If there is an error parsing the date.
     */
    public static LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate formattedDate = null;
        try {
            formattedDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            Output.printException(ErrorConstant.PARSING_DATE_ERROR);
        }
        return formattedDate;
    }

    /**
     * Converts a LocalDate object to a formatted String representation.
     *
     * @param date LocalDate object representing the date.
     * @return Formatted String representation of the date in the format "dd-MM-yyyy".
     * @throws DateTimeParseException If there is an error parsing the date.
     */
    public static String parseFormattedDate(LocalDate date) {
        DateTimeFormatter formatter = null;
        try {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        } catch (DateTimeParseException e) {
            Output.printException(ErrorConstant.PARSING_DATE_ERROR);
        }
        if (date == null || formatter == null) {
            return "NA";
        }
        return date.format(formatter);
    }

    /**
     * Parses and converts String time to a LocalDate variable.
     *
     * @param stringTime String representing the time.
     * @return LocalTime variable representing the time.
     * @throws DateTimeParseException If there is an error parsing the time.
     */
    public static LocalTime parseTime(String stringTime) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime formattedTime = null;
        try {
            formattedTime = LocalTime.parse(stringTime, formatter);
        } catch (DateTimeParseException e) {
            Output.printException(ErrorConstant.PARSING_TIME_ERROR);
        }
        return formattedTime;
    }

    /**
     * Splits user input for Delete command into item and index.
     *
     * @param input A user-provided string.
     * @return An array of strings containing the extracted delete command parameters.
     * @throws CustomExceptions.InsufficientInput If not enough parameters are specified.
     */
    public static String[] splitDeleteInput(String input) throws CustomExceptions.InsufficientInput,
            CustomExceptions.InvalidInput {
        if (!input.contains(UiConstant.ITEM_FLAG) || !input.contains(UiConstant.INDEX_FLAG)) {
            throw new CustomExceptions.InsufficientInput(ErrorConstant.INSUFFICIENT_DELETE_PARAMETERS_ERROR);
        }

        if (countForwardSlash(input) > UiConstant.NUM_OF_SLASHES_FOR_DELETE) {
            throw new CustomExceptions.InvalidInput(ErrorConstant.TOO_MANY_SLASHES_ERROR);
        }

        String[] results = new String[UiConstant.NUM_DELETE_PARAMETERS];
        results[UiConstant.DELETE_ITEM_STRING_INDEX] = extractSubstringFromSpecificIndex(input,
                UiConstant.ITEM_FLAG).trim();
        results[UiConstant.DELETE_ITEM_NUMBER_INDEX] = extractSubstringFromSpecificIndex(input,
                UiConstant.INDEX_FLAG).trim();
        return results;
    }

    /**
     * Parses and validates user input for the delete command. Returns a list of parsed user input
     * containing the filter string and the index to delete.
     *
     * @param userInput The user input string.
     * @return A list of strings containing the filter string and index to delete.
     */
    public static String[] parseDeleteInput(String userInput) {

        try {
            String[] deleteDetails = splitDeleteInput(userInput);
            Validation.validateDeleteInput(deleteDetails);
            return deleteDetails;
        } catch (CustomExceptions.InvalidInput | CustomExceptions.InsufficientInput e) {
            Output.printException(e.getMessage());
            return null;
        }
    }

    //@@author JustinSoh

    /**
     * Function validates and parses the user input for the history and latest commands.
     *
     * @param userInput String representing the user input.
     * @return The filter string, set to either 'gym', 'run', 'bmi' or 'period'.
     */
    public static String parseHistoryAndLatestInput(String userInput) {
        try {
            if (countForwardSlash(userInput) > UiConstant.NUM_OF_SLASHES_FOR_LATEST_AND_HISTORY) {
                throw new CustomExceptions.InvalidInput(ErrorConstant.TOO_MANY_SLASHES_ERROR);
            }
            String type = extractSubstringFromSpecificIndex(userInput, UiConstant.ITEM_FLAG);

            if (type.isBlank()) {
                throw new CustomExceptions.InsufficientInput(ErrorConstant.INVALID_HISTORY_FILTER_ERROR);
            }
            Validation.validateFilter(type.toLowerCase());
            return type.toLowerCase();
        } catch (CustomExceptions.InvalidInput | CustomExceptions.InsufficientInput e) {
            Output.printException(e.getMessage());
            return null;
        }
    }

    /**
     * Parses input for Bmi command. Adds Bmi object to HealthList if valid.
     *
     * @param userInput The user input string.
     * @throws CustomExceptions.InvalidInput      If input is invalid.
     * @throws CustomExceptions.InsufficientInput If the height, weight or date parameters are missing.
     */
    public static void parseBmiInput(String userInput) throws CustomExceptions.InvalidInput,
            CustomExceptions.InsufficientInput {
        String[] bmiDetails = splitBmiInput(userInput);
        Validation.validateBmiInput(bmiDetails);
        Bmi newBmi = new Bmi(
                bmiDetails[HealthConstant.BMI_HEIGHT_INDEX],
                bmiDetails[HealthConstant.BMI_WEIGHT_INDEX],
                bmiDetails[HealthConstant.BMI_DATE_INDEX]);
        HealthList.addBmi(newBmi);
        Output.printAddBmi(newBmi);
    }

    //@@author syj02

    /**
     * Split user input for Bmi command, height, weight and date.
     *
     * @param input A user-provided string.
     * @return An array of strings containing the extracted Bmi parameters.
     * @throws CustomExceptions.InsufficientInput If the height, weight or date parameters are missing.
     */
    public static String[] splitBmiInput(String input) throws CustomExceptions.InsufficientInput,
            CustomExceptions.InvalidInput {

        if (!input.contains(HealthConstant.HEIGHT_FLAG)
                || !input.contains(HealthConstant.WEIGHT_FLAG)
                || !input.contains(HealthConstant.DATE_FLAG)) {
            throw new CustomExceptions.InsufficientInput(ErrorConstant.INSUFFICIENT_BMI_PARAMETERS_ERROR);
        }

        if (countForwardSlash(input) > HealthConstant.NUM_OF_SLASHES_FOR_BMI) {
            throw new CustomExceptions.InvalidInput(ErrorConstant.TOO_MANY_SLASHES_ERROR);
        }

        String[] results = new String[HealthConstant.NUM_BMI_PARAMETERS];
        results[HealthConstant.BMI_HEIGHT_INDEX] = extractSubstringFromSpecificIndex(input,
                HealthConstant.HEIGHT_FLAG).trim();
        results[HealthConstant.BMI_WEIGHT_INDEX] = extractSubstringFromSpecificIndex(input,
                HealthConstant.WEIGHT_FLAG).trim();
        results[HealthConstant.BMI_DATE_INDEX] = extractSubstringFromSpecificIndex(input,
                HealthConstant.DATE_FLAG).trim();
        return results;
    }
    //@@author

    /**
     * Parses input for Period command. Adds Period object to HealthList if valid.
     *
     * @param userInput The user input string.
     * @throws CustomExceptions.InvalidInput      If input is invalid.
     * @throws CustomExceptions.InsufficientInput If the start date or end date parameters are missing.
     */
    public static void parsePeriodInput(String userInput) throws CustomExceptions.InvalidInput,
            CustomExceptions.InsufficientInput {
        int size = HealthList.getPeriodSize();
        String[] periodDetails = splitPeriodInput(userInput);
        Validation.validatePeriodInput(periodDetails);

        if (userInput.contains(HealthConstant.START_FLAG) && userInput.contains(HealthConstant.END_FLAG)) {
            if ((size == 0) || (size > 0 && HealthList.getPeriod(size-1).getEndDate() != null)) {
                Period newPeriod = new Period(
                        periodDetails[HealthConstant.PERIOD_START_DATE_INDEX],
                        periodDetails[HealthConstant.PERIOD_END_DATE_INDEX]);
                HealthList.addPeriod(newPeriod);
                Output.printAddPeriod(newPeriod);
            } else if (size > 0 && HealthList.getPeriod(size-1).getEndDate() == null) {
                HealthList.getPeriod(size - 1).updateEndDate(periodDetails[1]);
                Output.printAddPeriod(HealthList.getPeriod(size - 1));
            }
        } else if (userInput.contains(HealthConstant.START_FLAG)) {
            Period newPeriod = new Period(periodDetails[HealthConstant.PERIOD_START_DATE_INDEX]);
            HealthList.addPeriod(newPeriod);
            Output.printAddPeriod(newPeriod);
        }
    }

    /**
     * Split user input into Period command, start date and end date.
     *
     * @param input A user-provided string.
     * @return An array of strings containing the extracted Period parameters.
     * @throws CustomExceptions.InsufficientInput If the user input is invalid or blank.
     */
    public static String[] splitPeriodInput(String input) throws CustomExceptions.InsufficientInput,
            CustomExceptions.InvalidInput {
        if (!input.contains(HealthConstant.START_FLAG)
                || (!input.contains(HealthConstant.START_FLAG) && !input.contains(HealthConstant.END_FLAG))) {
            throw new CustomExceptions.InsufficientInput(ErrorConstant.INSUFFICIENT_PERIOD_PARAMETERS_ERROR);
        }

        if (countForwardSlash(input) > HealthConstant.NUM_OF_SLASHES_FOR_PERIOD) {
            throw new CustomExceptions.InvalidInput(ErrorConstant.TOO_MANY_SLASHES_ERROR);
        }

        String[] results = new String[HealthConstant.NUM_PERIOD_PARAMETERS];

        if (input.contains(HealthConstant.START_FLAG) && input.contains(HealthConstant.END_FLAG)) {
            results[HealthConstant.PERIOD_START_DATE_INDEX] = extractSubstringFromSpecificIndex(input,
                    HealthConstant.START_FLAG).trim();
            results[HealthConstant.PERIOD_END_DATE_INDEX] = extractSubstringFromSpecificIndex(input,
                    HealthConstant.END_FLAG).trim();
        } else if (input.contains(HealthConstant.START_FLAG) && !input.contains(HealthConstant.END_FLAG)) {
            results[HealthConstant.PERIOD_START_DATE_INDEX] = extractSubstringFromSpecificIndex(input,
                    HealthConstant.START_FLAG).trim();
        }
        return results;
    }


    /**
     * Parses input for Prediction command.
     * Prints period prediction if possible.
     *
     * @throws CustomExceptions.InsufficientInput If prediction cannot be made.
     */
    public static void parsePredictionInput() throws CustomExceptions.InsufficientInput {
        if (HealthList.getPeriodSize() >= HealthConstant.MIN_SIZE_FOR_PREDICTION) {
            HealthList.printLatestThreeCycles();
            LocalDate nextPeriodStartDate = HealthList.predictNextPeriodStartDate();
            Period.printNextCyclePrediction(nextPeriodStartDate);
        } else {
            throw new CustomExceptions.InsufficientInput(ErrorConstant.UNABLE_TO_MAKE_PREDICTIONS_ERROR);
        }
    }

    /**
     * Split user input into Appointment command, date, time and description.
     *
     * @param input A user-provided string.
     * @return An array of strings containing the extracted Appointment parameters.
     * @throws CustomExceptions.InsufficientInput If the user input is invalid or blank.
     */
    public static String[] splitAppointmentDetails(String input)
            throws CustomExceptions.InsufficientInput, CustomExceptions.InvalidInput {
        String [] results = new String[HealthConstant.NUM_APPOINTMENT_PARAMETERS];
        if (!input.contains(HealthConstant.DATE_FLAG)
                || !input.contains(HealthConstant.TIME_FLAG)
                || !input.contains(HealthConstant.DESCRIPTION_FLAG)) {
            throw new CustomExceptions.InsufficientInput(ErrorConstant.INSUFFICIENT_APPOINTMENT_PARAMETERS_ERROR);
        }

        if (countForwardSlash(input) > HealthConstant.NUM_OF_SLASHES_FOR_APPOINTMENT) {
            throw new CustomExceptions.InvalidInput(ErrorConstant.TOO_MANY_SLASHES_ERROR);
        }

        results[HealthConstant.APPOINTMENT_DATE_INDEX] = extractSubstringFromSpecificIndex(input,
                HealthConstant.DATE_FLAG).trim();
        results[HealthConstant.APPOINTMENT_TIME_INDEX] = extractSubstringFromSpecificIndex(input,
                HealthConstant.TIME_FLAG).trim();
        results[HealthConstant.APPOINTMENT_DESCRIPTION_INDEX] = extractSubstringFromSpecificIndex(input,
                HealthConstant.DESCRIPTION_FLAG).trim();
        return results;
    }

    /**
     * Parses input for Appointment command. Adds Appointment object to HealthList if valid.
     *
     * @param userInput The user input string.
     * @throws CustomExceptions.InvalidInput      If input is invalid.
     * @throws CustomExceptions.InsufficientInput If the date, time or description parameters are missing.
     */
    public static void parseAppointmentInput(String userInput) throws CustomExceptions.InvalidInput,
            CustomExceptions.InsufficientInput {
        String[] appointmentDetails = splitAppointmentDetails(userInput);
        Validation.validateAppointmentDetails(appointmentDetails);
        Appointment newAppointment = new Appointment(
                appointmentDetails[HealthConstant.APPOINTMENT_DATE_INDEX],
                appointmentDetails[HealthConstant.APPOINTMENT_TIME_INDEX],
                appointmentDetails[HealthConstant.APPOINTMENT_DESCRIPTION_INDEX]);
        HealthList.addAppointment(newAppointment);
        Output.printAddAppointment(newAppointment);
    }

    /**
     * Extracts a substring from the given input string based on the provided delimiter.
     *
     * @param input     The input string from which to extract the substring.
     * @param delimiter The delimiter to search for in the input string.
     * @return The extracted substring, or an empty string if the delimiter is not found.
     */
    public static String extractSubstringFromSpecificIndex(String input, String delimiter) {
        int index = input.indexOf(delimiter);
        if (index == -1 || index == input.length() - delimiter.length()) {
            return "";
        }
        int startIndex = index + delimiter.length();
        int endIndex = input.indexOf("/", startIndex);
        if (endIndex == -1) {
            endIndex = input.length();
        }
        return input.substring(startIndex, endIndex).trim();
    }

    //@@author JustinSoh

    /**
     * Splits the user input when adding a Gym.
     *
     * @param input The user input string.
     * @return The Gym parameters split from the user input.
     * @throws CustomExceptions.InsufficientInput If the number of stations is missing.
     */
    public static String[] splitGymInput(String input) throws CustomExceptions.InsufficientInput,
            CustomExceptions.InvalidInput {
        if (!input.contains(WorkoutConstant.NUMBER_OF_STATIONS_FLAG)) {
            throw new CustomExceptions.InsufficientInput(ErrorConstant.INSUFFICIENT_GYM_PARAMETERS_ERROR);
        }

        if (countForwardSlash(input) != WorkoutConstant.NUM_OF_SLASHES_FOR_GYM_WITH_DATE &&
            countForwardSlash(input) != WorkoutConstant.NUM_OF_SLASHES_FOR_GYM_WITHOUT_DATE) {
            throw new CustomExceptions.InvalidInput(ErrorConstant.TOO_MANY_SLASHES_ERROR);
        }


        String[] results = new String[WorkoutConstant.NUMBER_OF_GYM_PARAMETERS];
        results[WorkoutConstant.GYM_NUMBER_OF_STATIONS_INDEX] = extractSubstringFromSpecificIndex(input,
                WorkoutConstant.NUMBER_OF_STATIONS_FLAG).trim();

        if (input.contains(WorkoutConstant.DATE_FLAG)) {
            results[WorkoutConstant.GYM_DATE_INDEX] = extractSubstringFromSpecificIndex(input,
                    WorkoutConstant.DATE_FLAG).trim();
        }
        return results;
    }

    /**
     * Parses input for the Gym command. Adds Gym object if valid.
     *
     * @param userInput The user input string.
     * @throws CustomExceptions.InvalidInput      If input is invalid.
     * @throws CustomExceptions.InsufficientInput If number of station parameter is missing.
     */
    public static void parseGymInput(String userInput) throws CustomExceptions.InsufficientInput,
            CustomExceptions.InvalidInput {
        String[] gymDetails = splitGymInput(userInput);
        Validation.validateGymInput(gymDetails);
        Gym newGym;
        if (gymDetails[WorkoutConstant.GYM_DATE_INDEX] == null) {
            newGym = new Gym();
        } else {
            newGym = new Gym(gymDetails[WorkoutConstant.GYM_DATE_INDEX]);
        }
        int numberOfStations = Integer.parseInt(gymDetails[WorkoutConstant.GYM_NUMBER_OF_STATIONS_INDEX]);
        parseGymStationInput(numberOfStations, newGym);
    }

    /**
     * Splits the user input for adding a run.
     *
     * @param input The user input string.
     * @return The Run parameters split from the user input.
     * @throws CustomExceptions.InsufficientInput If the distance and time taken for the run are missing.
     */
    public static String[] splitRunInput(String input) throws CustomExceptions.InsufficientInput,
            CustomExceptions.InvalidInput {
        if (!input.contains(WorkoutConstant.DISTANCE_FLAG) ||
            !input.contains(WorkoutConstant.RUN_TIME_FLAG)) {
            throw new CustomExceptions.InsufficientInput(ErrorConstant.INSUFFICIENT_RUN_PARAMETERS_ERROR);
        }

        if (countForwardSlash(input) != WorkoutConstant.NUM_OF_SLASHES_FOR_RUN_WITH_DATE &&
                countForwardSlash(input) != WorkoutConstant.NUM_OF_SLASHES_FOR_RUN_WITHOUT_DATE) {
            throw new CustomExceptions.InvalidInput(ErrorConstant.TOO_MANY_SLASHES_ERROR);
        }

        String[] results = new String[WorkoutConstant.NUMBER_OF_RUN_PARAMETERS];
        results[WorkoutConstant.RUN_TIME_INDEX] = extractSubstringFromSpecificIndex(input,
                WorkoutConstant.RUN_TIME_FLAG).trim();
        results[WorkoutConstant.RUN_DISTANCE_INDEX] = extractSubstringFromSpecificIndex(input,
                WorkoutConstant.DISTANCE_FLAG).trim();

        if (input.contains(WorkoutConstant.DATE_FLAG)) {
            results[WorkoutConstant.RUN_DATE_INDEX] = extractSubstringFromSpecificIndex(input,
                    WorkoutConstant.DATE_FLAG).trim();
        }
        return results;
    }

    /**
     * Parses input for the Run command. Adds a Run object if valid.
     *
     * @param input The user input string.
     * @throws CustomExceptions.InvalidInput      If input is invalid.
     * @throws CustomExceptions.InsufficientInput If the run time or run distance parameters are missing.
     */
    public static void parseRunInput(String input) throws CustomExceptions.InsufficientInput,
            CustomExceptions.InvalidInput {
        String[] runDetails = splitRunInput(input);
        Validation.validateRunInput(runDetails);
        Run newRun;
        if (runDetails[WorkoutConstant.RUN_DATE_INDEX] == null) {
            newRun = new Run(
                    runDetails[WorkoutConstant.RUN_TIME_INDEX],
                    runDetails[WorkoutConstant.RUN_DISTANCE_INDEX]);
        } else {
            newRun = new Run(
                    runDetails[WorkoutConstant.RUN_TIME_INDEX],
                    runDetails[WorkoutConstant.RUN_DISTANCE_INDEX],
                    runDetails[WorkoutConstant.RUN_DATE_INDEX]);
        }
        Output.printAddRun(newRun);
    }

    /**
     * Retrieves the gym station details and adds a GymStation object to Gym.
     *
     * @param numberOfStations The number of stations in one gym session.
     * @param gym              The Gym object.
     */
    public static void parseGymStationInput(int numberOfStations, Gym gym) {
        int i = 0;
        while (i < numberOfStations) {
            try {
                Output.printGymStationPrompt(i + 1);
                String userInput = Handler.in.nextLine();
                if (countForwardSlash(userInput) > WorkoutConstant.NUM_OF_SLASHES_FOR_GYM_STATION) {
                    throw new CustomExceptions.InvalidInput(ErrorConstant.TOO_MANY_SLASHES_ERROR);
                }
                String[] validGymStationInput = Validation.splitAndValidateGymStationInput(userInput);

                int numberOfSets = Integer.parseInt(validGymStationInput[WorkoutConstant.GYM_STATION_SET_INDEX]);
                int numberOfRepetitions = Integer.parseInt(
                        validGymStationInput[WorkoutConstant.GYM_STATION_REPS_INDEX]);
                ArrayList<Double> weightsArray = Validation.validateWeightsArray(
                        validGymStationInput[WorkoutConstant.GYM_STATION_WEIGHTS_INDEX]);

                gym.addStation(validGymStationInput[WorkoutConstant.GYM_STATION_NAME_INDEX], numberOfSets,
                        numberOfRepetitions, weightsArray);
                i++;
            } catch (CustomExceptions.InsufficientInput | CustomExceptions.InvalidInput e) {
                Output.printException(e.getMessage());
            }
        }
        Output.printAddGym(gym);
    }

    //@@author L5-Z
    /**
     * Splits the Gym File Input that comes from Storage.
     * Validates the numberOfStation and Date input.
     *
     * @param input The user input string.
     * @return String[] containing the gym details
     * @throws CustomExceptions.FileReadError If the file cannot be read.
     */
    private static String[] splitGymFileInput (String input) throws CustomExceptions.FileReadError {

        String [] gymDetails = input.split(UiConstant.SPLIT_BY_COLON);
        String gymType;
        String numOfStationStr;
        int numOfStation;
        String date;

        // checks if there are enough parameters in the gym file + if numOfStation is a digit
        try {
            gymType = gymDetails[WorkoutConstant.GYM_FILE_INDEX].toLowerCase();
            numOfStationStr = gymDetails[WorkoutConstant.NUM_OF_STATIONS_FILE_INDEX];
            numOfStation = Integer.parseInt(numOfStationStr);
            date = gymDetails[WorkoutConstant.DATE_FILE_INDEX];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CustomExceptions.FileReadError(ErrorConstant.LOAD_GYM_FORMAT_ERROR);
        } catch (NumberFormatException e) {
            throw new CustomExceptions.FileReadError(ErrorConstant.LOAD_NUMBER_OF_STATION_ERROR);
        }

        // Check if the gym type is correct (e.g. storage starts with gym| ...)
        if (!gymType.equals(WorkoutConstant.GYM)) {
            throw new CustomExceptions.FileReadError(ErrorConstant.LOAD_GYM_TYPE_ERROR);
        }

        // Check if the number of station is blank
        if (numOfStationStr.isBlank()){
            throw new CustomExceptions.FileReadError(ErrorConstant.LOAD_NUMBER_OF_STATION_ERROR);
        }

        // Check if the date is correct
        if (date.isBlank()){
            throw new CustomExceptions.FileReadError(ErrorConstant.INVALID_DATE_ERROR);
        }

        // if the date is not NA, then validate and make sure it is correct
        try{
            if (!date.equals(ErrorConstant.NO_DATE_SPECIFIED_ERROR)){
                Validation.validateDateInput(date);
            }
        } catch (CustomExceptions.InvalidInput e){
            throw new CustomExceptions.FileReadError(ErrorConstant.INVALID_DATE_ERROR);
        }

        return gymDetails;
    }

    /**
     * Adds a station to the gym object based of the file input.
     * This method is used in the {@code parseGymFileInput} method.
     * How the method works is that it will check if the station details are valid
     * and then add the station to the gym.
     *
     * @param gym the gym object that the station will be added to
     * @param gymDetails the array of strings containing the gym details
     * @param baseCounter the base counter to start adding the station
     * @return the new base counter after adding the station
     * @throws CustomExceptions.FileReadError if there is an error in the file input
     * @throws CustomExceptions.InvalidInput if the input is invalid
     */
    private static int addStationFromFile(Gym gym, String[] gymDetails, int baseCounter)
            throws CustomExceptions.FileReadError,
            CustomExceptions.InvalidInput {

        String currentStationName;
        String numberOfSetsStr;
        String repsStr;
        String weightStrings;

        int numberOfSets;
        int reps;
        ArrayList<Double> weights = new ArrayList<>();
        try {
            currentStationName = gymDetails[baseCounter];
            numberOfSetsStr = gymDetails[baseCounter + WorkoutConstant.SETS_OFFSET];
            repsStr = gymDetails[baseCounter + WorkoutConstant.REPS_OFFSET];
            weightStrings = gymDetails[baseCounter + WorkoutConstant.WEIGHTS_OFFSET];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CustomExceptions.FileReadError(ErrorConstant.LOAD_GYM_FORMAT_ERROR);
        }

        if(currentStationName.isBlank() ||
                numberOfSetsStr.isBlank() ||
                repsStr.isBlank() ||
                weightStrings.isBlank()){
            throw new CustomExceptions.FileReadError(ErrorConstant.LOAD_GYM_FORMAT_ERROR);
        }

        // Check if valid numbers
        try {
            numberOfSets = Integer.parseInt(numberOfSetsStr);
            reps = Integer.parseInt(repsStr);
        } catch (NumberFormatException e) {
            throw new CustomExceptions.FileReadError(ErrorConstant.LOAD_GYM_FORMAT_ERROR);
        }

        // Check if weights able to split
        String[] weight = weightStrings.split(UiConstant.SPLIT_BY_COMMAS);
        if (weight.length != numberOfSets){
            throw new CustomExceptions.FileReadError(ErrorConstant.LOAD_NUMBER_OF_SETS_ERROR);
        }

        // Check if weights are valid numbers
        try {
            for (String weightString : weight) {
                weights.add(Double.parseDouble(weightString));
            }
        } catch (NumberFormatException e) {
            throw new CustomExceptions.FileReadError(ErrorConstant.LOAD_GYM_FORMAT_ERROR);
        }

        gym.addStation(currentStationName, numberOfSets, reps, weights);
        baseCounter += WorkoutConstant.INCREMENT_OFFSET;

        return baseCounter;
    }


    /**
     * Parses the gym input from the storage file and returns a Gym object.
     * The input of the storage file needs to be in the following format
     * gym:NUM_STATIONS:DATE:STATION1_NAME:NUM_SETS:REPS:WEIGHT1,WEIGHT2,WEIGHT3,WEIGHT4
     * :STATION2_NAME:NUM_SETS:REPS:WEIGHT1,WEIGHT2,WEIGHT3,WEIGHT4 ....
     *
     * @param input in the format specified above
     * @return gym object created from the input
     * @throws CustomExceptions.InvalidInput If there is invalid input from the file.
     * @throws CustomExceptions.FileReadError If the file cannot be read.
     */
    public static Gym parseGymFileInput(String input)
            throws CustomExceptions.InvalidInput,
            CustomExceptions.FileReadError {

        // does initial round of input checking
        String[] gymDetails = splitGymFileInput(input);
        Gym gym;
        String date = gymDetails[WorkoutConstant.DATE_FILE_INDEX];

        if (date.equals(ErrorConstant.NO_DATE_SPECIFIED_ERROR)) {
            gym = new Gym();
        } else {
            gym = new Gym(date);
        }

        int counter = WorkoutConstant.GYM_FILE_BASE_COUNTER;
        while (counter < gymDetails.length) {
            counter = addStationFromFile(gym , gymDetails, counter);
        }
        return gym;
    }

}
