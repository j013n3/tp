package utility;

/**
 * Class representing the filters used for PulsePilot.
 */
public class Filters {

    public enum Command {
        WORKOUT,
        HISTORY,
        LATEST,
        HEALTH,
        DELETE,
        HELP,
        EXIT
    }

    public enum DeleteFilters {
        RUN,
        GYM,
        PERIOD,
        BMI,
        APPOINTMENT,
    }

    public enum HealthFilters {
        PERIOD,
        BMI,
        APPOINTMENT,
        PREDICTION,
    }

    public enum WorkoutFilters {
        RUN,
        GYM,
    }

    public enum HistoryAndLatestFilters {
        RUN,
        GYM,
        PERIOD,
        BMI,
        APPOINTMENT,
        WORKOUTS
    }

    public enum DataType {
        BMI,
        APPOINTMENT,
        PERIOD,
        GYM,
        RUN
    }

}
