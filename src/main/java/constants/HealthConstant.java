package constants;

/**
 * HealthConstant class contains constants related to health-related functionalities in the application.
 * It includes headers, flags, parameters, thresholds, formatted strings/messages, and split indices.
 */
public class HealthConstant {

    // Headers
    public static final String BMI = "bmi";
    public static final String PERIOD = "period";
    public static final String APPOINTMENT = "appointment";

    // Flags
    public static final String HEALTH_FLAG = "/h:";
    public static final String HEIGHT_FLAG = "/height:";
    public static final String WEIGHT_FLAG = "/weight:";
    public static final String DATE_FLAG = "/date:";
    public static final String START_FLAG = "/start:";
    public static final String END_FLAG = "/end:";
    public static final String TIME_FLAG = "/time:";
    public static final String DESCRIPTION_FLAG = "/description:";

    // Parameters
    public static final Integer NUM_BMI_PARAMETERS = 3;
    public static final Integer NUM_PERIOD_PARAMETERS = 2;
    public static final Integer NUM_APPOINTMENT_PARAMETERS = 3;

    // Threshold
    public static final double UNDERWEIGHT_BMI_THRESHOLD = 18.5;
    public static final double NORMAL_BMI_THRESHOLD = 24.9;
    public static final double OVERWEIGHT_BMI_THRESHOLD = 29.9;
    public static final double OBESE_BMI_THRESHOLD = 39.9;
    public static final double MIN_WEIGHT = 0;
    public static final double MIN_HEIGHT = 0;
    public static final double MAX_HEIGHT = 2.75;
    public static final double MAX_WEIGHT = 640;


    // Formatted Strings/Messages
    // BMI
    public static final String LOG_DELETE_BMI_FORMAT = "Removed BMI entry of %.2f from %s";
    public static final String TWO_DECIMAL_PLACE_FORMAT = "%.2f";
    public static final String BMI_ADDED_MESSAGE_PREFIX = "Added: bmi | ";
    public static final String BMI_REMOVED_MESSAGE_PREFIX = "Removed BMI with index: ";
    public static final String UNDERWEIGHT_MESSAGE = "You're underweight.";
    public static final String NORMAL_WEIGHT_MESSAGE = "Great! You're within normal range.";
    public static final String OVERWEIGHT_MESSAGE = "You're overweight.";
    public static final String OBESE_MESSAGE = "You're obese.";
    public static final String SEVERELY_OBESE_MESSAGE = "You're severely obese.";
    public static final String BMI_HISTORY_HEADER = "Your BMI history:";

    // PERIOD
    public static final String PRINT_PERIOD_FORMAT = "Period Start: %s Period End: %s"
            + System.lineSeparator()
            + "Period Length: %d %s";
    public static final String PRINT_BMI_FORMAT = "%s"
            + System.lineSeparator()
            + "Your BMI is %.2f"
            + System.lineSeparator()
            + "%s";
    public static final String LOG_DELETE_PERIOD_FORMAT = "Removed period entry with start date: %s and end date: %s";
    public static final String PERIOD_ADDED_MESSAGE_PREFIX = "Added: period | ";
    public static final String PERIOD_REMOVED_MESSAGE_PREFIX = "Removed period with index: ";
    public static final String PERIOD_HISTORY_HEADER = "Your Period history:";

    // PREDICTION
    public static final String PRINT_CYCLE_FORMAT = "Cycle Length: %d days";
    public static final Integer LATEST_THREE_CYCLE_LENGTHS = 3;
    public static final Integer LAST_CYCLE_INDEX_OFFSET = 2;
    public static final Integer LAST_CYCLE_OFFSET = 1;
    public static final Integer MIN_SIZE_FOR_PREDICTION = 4;
    public static final String PREDICTED_START_DATE_MESSAGE = "Your next cycle's predicted start date is ";
    public static final String COUNT_DAYS_MESSAGE = ", in ";
    public static final String PREDICTED_DATE_IS_TODAY_MESSAGE = ", which is today! ";
    public static final String PERIOD_IS_LATE = ". Your period is late by ";
    public static final String DAY_MESSAGE = "day";
    public static final String DAYS_MESSAGE = "days";

    // APPOINTMENT
    public static final String PRINT_APPOINTMENT_FORMAT = "On %s at %s: %s";
    public static final String LOG_DELETE_APPOINTMENT_FORMAT = "Removed appointment on %s at %s: %s";
    public static final String APPOINTMENT_ADDED_MESSAGE_PREFIX = "Added: appointment | ";
    public static final String APPOINTMENT_REMOVED_MESSAGE_PREFIX = "Removed appointment with index: ";
    public static final Integer MAX_DESCRIPTION_LENGTH = 100;
    public static final String APPOINTMENT_HISTORY_HEADER = "Your Appointment history:";

    // SPLIT INDEX
    public static final int BMI_HEIGHT_INDEX = 0;
    public static final int BMI_WEIGHT_INDEX = 1;
    public static final int BMI_DATE_INDEX = 2;
    public static final int PERIOD_START_DATE_INDEX = 0;
    public static final int PERIOD_END_DATE_INDEX = 1;
    public static final int APPOINTMENT_DATE_INDEX = 0;
    public static final int APPOINTMENT_TIME_INDEX = 1;
    public static final int APPOINTMENT_DESCRIPTION_INDEX = 2;

    public static final int NUM_OF_SLASHES_FOR_PERIOD = 3;
    public static final int NUM_OF_SLASHES_FOR_BMI = 4;
    public static final int NUM_OF_SLASHES_FOR_APPOINTMENT = 4;
}
