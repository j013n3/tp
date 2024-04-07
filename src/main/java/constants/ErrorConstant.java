package constants;

/**
 * ErrorConstant class contains constants for various types of errors that may occur in the application.
 * The constants are used to provide descriptive error messages to the user when errors occur.
 */
public class ErrorConstant {

    // General Errors
    public static final String NEGATIVE_VALUE_ERROR = "Requires a positive integer!";
    public static final String INVALID_INDEX_DELETE_ERROR = "Invalid index to delete!";


    // Storage Errors
    public static final String SAVE_ERROR = "File save failed. Write error occurred:";
    public static final String LOAD_ERROR = "File read error:" + "Error at ";
    public static final String CREATE_FILE_ERROR = "Unable to create file.";
    public static final String CORRUPT_ERROR = "File is corrupted!" +
            System.lineSeparator() + "Deleting 'pulsepilot_data.txt' and 'pulsepilot_hash.txt'. Try running again!" +
            System.lineSeparator() + UiConstant.PARTITION_LINE;
    public static final String DATA_INTEGRITY_ERROR = "Data file integrity compromised. Exiting.";
    public static final String MISSING_INTEGRITY_ERROR = "Key files for integrity missing. Exiting.";
    public static final String HASH_ERROR = "Error occurred while processing file hash.";

    // Storage Error for Gym
    public static final String LOAD_GYM_FORMAT_ERROR = LOAD_ERROR + "Format of gym entry is incorrect/corrupted";
    public static final String LOAD_GYM_TYPE_ERROR = LOAD_ERROR + "Format of gym type is incorrect/corrupted";
    public static final String LOAD_NUMBER_OF_STATION_ERROR = LOAD_ERROR + "Number of stations is corrupted";
    public static final String LOAD_NUMBER_OF_SETS_ERROR = LOAD_ERROR
            + "Number of weights doesn't match number of sets";

    // Input Errors
    public static final String INVALID_COMMAND_ERROR = "Invalid command. Enter 'help' to view available commands.";
    public static final String NO_DATE_SPECIFIED_ERROR = "NA";
    public static final String INVALID_ITEM_ERROR = "Invalid item specified.";
    public static final String CORRECT_FILTER_ITEM_FORMAT = "/item:run/gym/workouts/bmi/period/appointment";

    // Date errors
    public static final String INVALID_DATE_ERROR = "Invalid date format. Format is DD-MM-YYYY in integers. " +
            "Make sure a valid date is entered (take note of leap years)!";
    public static final String INVALID_YEAR_ERROR = "Year has to be after 1967!";
    public static final String INVALID_LEAP_YEAR_ERROR = "29 Feb does not exist in this year!";
    public static final String PARSING_DATE_ERROR ="Error parsing date!";

    // Time errors
    public static final String INVALID_ACTUAL_TIME_ERROR = "Invalid time format. Format is HH:MM in 24 hours format!";
    public static final String INVALID_MINUTES_ERROR = "Minutes must be a positive integer between 00 and 59.";
    public static final String INVALID_HOURS_ERROR = "Hours must be a positive integer between 00 and 23";
    public static final String PARSING_TIME_ERROR = "Error parsing time!";

    //Delete Errors
    public static final String INSUFFICIENT_DELETE_PARAMETERS_ERROR = "Insufficient parameters for delete! " +
            "Example input: /item:item /index:index"
            + System.lineSeparator()
            + "Only input what is required! Additional characters between flags will cause errors.";
    public static final String INVALID_INDEX_ERROR = "Index must be a valid positive integer.";

    // EXERCISE ERRORS

    // HISTORY ERRORS
    public static final String HISTORY_RUN_EMPTY_ERROR = "No runs found! You need to add a run first!";
    public static final String HISTORY_GYM_EMPTY_ERROR = "No gyms found! You need to add a gym first!";
    public static final String HISTORY_WORKOUTS_EMPTY_ERROR = "No workouts found! You need to add " +
            "either a run or a gym first";
    public static final String HISTORY_APPOINTMENT_EMPTY_ERROR = "No appointments found! You need to add an " +
            "appointment first";
    public static final String HISTORY_BMI_EMPTY_ERROR = "No bmis found! You need to add a bmi first!";
    public static final String HISTORY_PERIOD_EMPTY_ERROR = "No periods found! You need to add a period first!";

    // RUN ERRORS
    public static final String INSUFFICIENT_RUN_PARAMETERS_ERROR = "Insufficient parameters for run! "
            + "Example input: /e:run /d:5.25 /t:25:23 [/date:DATE]"
            + System.lineSeparator()
            + "Only input what is required! Additional characters between flags will cause errors.";
    public static final String INVALID_RUN_DISTANCE_ERROR = "Distance is a 2 decimal point positive number!";
    public static final String INVALID_RUN_TIME_ERROR = "Invalid time format. Format is either HH:MM:SS or " +
            "MM:SS with integers.";
    public static final String INVALID_SECOND_ERROR = "Seconds must be a positive integer between 00 and 59";
    public static final String INVALID_HOUR_ERROR = "Hours cannot set to 00. Use MM:SS instead";
    public static final String INVALID_MINUTE_ERROR = "Minutes must be a positive integer between 01 and 59";

    // GYM ERRORS
    public static final String INSUFFICIENT_GYM_PARAMETERS_ERROR = "Insufficient parameters for gym! "
            + "Example input: /e:gym /n:2 [/date:DATE]"
            + System.lineSeparator()
            + "Only input what is required! Additional characters between flags will cause errors.";
    public static final String INVALID_NUMBER_OF_STATIONS_ERROR = "Number of stations is a positive number!";
    public static final String EMPTY_EXERCISE_NAME_ERROR = "Exercise name cannot be blank!";

    public static final String INVALID_EXERCISE_NAME_ERROR = "Exercise name can only have letters!";
    public static final String EXERCISE_NAME_LENGTH_ERROR = "Exercise name cannot be more than 40 characters!";
    public static final String GYM_STATION_FORMAT_ERROR = "Remember that you are now adding gym station input!"
            + System.lineSeparator()
            + "Expected format: [Station Name] /s:[SETS] /r:[REPS] /w:[WEIGHTS]";
    public static final String INVALID_SETS_ERROR = "Number of sets must be a positive integer!"
            + System.lineSeparator()
            + GYM_STATION_FORMAT_ERROR;
    public static final String INVALID_REPS_ERROR = "Number of reps must be a positive integer!"
            + System.lineSeparator()
            + GYM_STATION_FORMAT_ERROR;
    public static final String INVALID_WEIGHT_VALUE_ERROR = "The weight done for each set must "
            + "be a multiple of 0.125." 
            + System.lineSeparator() 
            + "This is because the smallest weight increment in most gyms is 0.125kg."
            + System.lineSeparator() 
            + GYM_STATION_FORMAT_ERROR;

    public static final String EMPTY_WEIGHTS_ARRAY_ERROR = "Weights array cannot be empty"
            + System.lineSeparator()
            + GYM_STATION_FORMAT_ERROR;
    public static final String GYM_WEIGHT_POSITIVE_ERROR = "Weights specified must a positive integer! " +
            "e.g. /w:10,20,30";
         
    public static final String GYM_WEIGHT_DIGIT_ERROR = " Weights must be a number! e.g. /w:5,10,20";
    public static final String GYM_WEIGHTS_INCORRECT_NUMBER_ERROR = " Number of weight values must be the same as" +
            " the number of sets! e.g. bench press /s:2 /r:10 /w:10,20";

    // HEALTH ERRORS
    public static final String INVALID_HEALTH_INPUT_ERROR = "Invalid input for health type! " +
            "Please input either /h:bmi, /h:period, /h:prediction or /h:appointment";

    // BMI ERRORS
    public static final String INSUFFICIENT_BMI_PARAMETERS_ERROR = "Insufficient parameters for bmi! " +
            "Example input: /h:bmi /height:height /weight:weight /date:date"
            + System.lineSeparator()
            + "Only input what is required! Additional characters between flags will cause errors.";
    public static final String NEGATIVE_BMI_ERROR = "Bmi must be a positive value";
    public static final String NULL_BMI_ERROR = "Bmi object cannot be null.";
    public static final String EMPTY_BMI_LIST_ERROR = "BMI List is empty.";
    public static final String BMI_LIST_UNCLEARED_ERROR = "Bmi list is not cleared.";
    public static final String HEIGHT_WEIGHT_INPUT_ERROR =
            "Height and weight should be 2 decimal place positive numbers!";

    // PERIOD ERRORS
    public static final String INSUFFICIENT_PERIOD_PARAMETERS_ERROR = "Insufficient parameters for period! "
            + System.lineSeparator()
            + "Example inputs: '/h:period /start:startDate' or '/h:period /start:startDate /end:endDate'"
            + System.lineSeparator()
            + "Only input what is required! Additional characters between flags will cause errors.";
    public static final String NULL_PERIOD_ERROR = "Period object cannot be null.";
    public static final String NULL_START_DATE_ERROR = "Start date of period cannot be empty.";
    public static final String NULL_END_DATE_ERROR = "End date of period cannot be empty.";
    public static final String INVALID_START_DATE_ERROR = "Invalid start date!";
    public static final String INVALID_END_DATE_ERROR = "Invalid end date!";
    public static final String EMPTY_PERIOD_LIST_ERROR = "Period List is empty.";
    public static final String PERIOD_LIST_UNCLEARED_ERROR = "Period list is not cleared.";
    public static final String DATE_IN_FUTURE_ERROR = "Date specified cannot be later than today's date.";
    public static final String PERIOD_END_BEFORE_START_ERROR = "Start date of period must be before end date.";
    public static final String UNABLE_TO_MAKE_PREDICTIONS_ERROR = "Insufficient period cycles to make prediction."
            + System.lineSeparator()
            + "Enter at least four period inputs for prediction of the next period's start date.";
    public static final String CURRENT_START_BEFORE_PREVIOUS_END =
            "The start date of your current period input needs to be after the end date of your previous period input."
            + System.lineSeparator()
            + "You may enter 'history /item:period' to check your period history.";
    public static final String INVALID_START_DATE_INPUT_ERROR = "Error is resulted by two possible reasons. "
        +System.lineSeparator()
        + "1. End date for previous period is still empty. " +
        "Add an end date before starting a new period input!"
        + System.lineSeparator()
        + "2. Start dates of current period do not tally. " +
        "Enter 'history /item:period' to view existing period inputs.";

    // APPOINTMENT ERRORS
    public static final String INSUFFICIENT_APPOINTMENT_PARAMETERS_ERROR = "Insufficient parameters for period! " +
            "Example input: /h:appointment /date:date /time:time /description:description /place:place"
            + System.lineSeparator()
            + "Only input what is required! Additional characters between flags will cause errors.";
    public static final String NULL_APPOINTMENT_ERROR = "Appointment object cannot be null.";
    public static final String EMPTY_APPOINTMENT_LIST_ERROR = "Appointment list is empty.";
    public static final String APPOINTMENT_LIST_UNCLEARED_ERROR = "Appointment list is not cleared.";
    public static final String START_INDEX_NEGATIVE_ERROR = "Start index for prediction must be positive";
    public static final String END_INDEX_GREATER_THAN_START_ERROR =
            "End index must be greater than start index is negative";
    public static final String NULL_DATE_ERROR = "Date of appointment cannot be empty.";
    public static final String NULL_TIME_ERROR = "Time of appointment cannot be empty.";
    public static final String DESCRIPTION_LENGTH_ERROR = "Description cannot be more than 100 characters";
    public static final String INVALID_DESCRIPTION_ERROR = "Appointment description can only " +
            "contain alphanumeric characters and spaces!";

    public static final String INVALID_HISTORY_FILTER_ERROR = "Missing/invalid filter used!" +
            System.lineSeparator() +
            "Use /item:run/gym/workouts/period/bmi/appointment";

    public static final String INVALID_LATEST_FILTER_ERROR = "Missing/invalid filter used!" +
            System.lineSeparator() +
            "Use /item:run/gym/period/bmi/appointment";

    public static final String TOO_MANY_SLASHES_ERROR = "Too many '/' characters specified within input. " +
            "Parameters cannot contain any '/' characters!";

    public static final String DISTANCE_TOO_LONG_ERROR = "The world's longest foot race is the Self-Transcendence "
            + "3100 Mile Race."
            + System.lineSeparator()
            + "Please enter a more realistic distance less than 5000km!";

    public static final String ZERO_HEIGHT_AND_WEIGHT_ERROR = "Height and weight must be more than 0.";
    public static final String MAX_HEIGHT_ERROR = "The tallest man Robert Wadlow was 2.72m. "
            + System.lineSeparator()
            + "Please enter a more realistic height less than 2.75m!";
    public static final String MAX_WEIGHT_ERROR = "The heaviest human being Jon Brower Minnnoch weighed in at 635kg. "
            + System.lineSeparator()
            + "Please enter a more realistic weight less than 640kg!";

    public static final String ZERO_DISTANCE_ERROR = "Distance run cannot be 0!";
    public static final String ZERO_TIME_ERROR = "Time cannot be set to 00:00!";

}
