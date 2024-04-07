package health;

import constants.ErrorConstant;
import constants.HealthConstant;
import utility.Parser;
import constants.UiConstant;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * The Period class extends the Health class.
 * It contains information about the start date, end date, time, period length, and cycle length of the period,
 * and the functionalities to calculate period and cycle length and predict the next period.
 */
public class Period extends Health {
    /**
     * The start date of period i.e. the first day of period flow which is also the first day of cycle.
     */
    protected LocalDate startDate;

    /**
     * The end date of period i.e. the last day of period flow.
     */
    protected LocalDate endDate;

    /**
     * The number of days between the first day and last day of period flow.
     */
    protected long periodLength;

    /**
     * The number of days between the first day and last day of the period cycle.
     */
    protected long cycleLength;

    //@@author syj02
    /**
     * Constructor for Period object.
     *
     * @param stringStartDate A string representing the start date of the period.
     * @param stringEndDate   A string representing the end date of the period.
     */
    public Period(String stringStartDate, String stringEndDate) {
        this.startDate = Parser.parseDate(stringStartDate);
        this.endDate = Parser.parseDate(stringEndDate);
        this.periodLength = calculatePeriodLength();
        this.cycleLength = 0;
    }

    /**
     * Constructs a new Period object with only the start date provided.
     *
     * @param stringStartDate A string representing the start date of the period.
     */
    public Period(String stringStartDate) {
        this.startDate = Parser.parseDate(stringStartDate);
        this.endDate = null;
        this.periodLength = 0;
        this.cycleLength = 0;
    }

    /**
     * Updates the end date of the period and calculates the period length.
     *
     * @param stringEndDate A string representing the new end date of the period.
     */
    public void updateEndDate(String stringEndDate) {
        this.endDate = Parser.parseDate(stringEndDate);
        this.periodLength = calculatePeriodLength();
    }

    /**
     * Retrieves the start date of the period of LocalDate type.
     *
     * @return The start date of period.
     * @throws AssertionError if the start date is null.
     */
    public LocalDate getStartDate() {
        assert startDate != null : ErrorConstant.NULL_START_DATE_ERROR;
        return startDate;
    }

    /**
     * Retrieves the end date of the period of LocalDate type.
     *
     * @return The end date of period.
     * @throws AssertionError if the end date is null.
     */
    public LocalDate getEndDate() {
        assert endDate != null : ErrorConstant.NULL_END_DATE_ERROR;
        return endDate;
    }

    /**
     * Retrieves the length of the period of long type.
     *
     * @return The period length.
     */
    public long getPeriodLength() {
        return periodLength;
    }

    /**
     * Calculates the length of the period in days.
     *
     * @return The length of the period.
     */
    public long calculatePeriodLength() {
        assert getStartDate().isBefore(getEndDate()) : ErrorConstant.PERIOD_END_BEFORE_START_ERROR;
        if (endDate == null || startDate == null) {
            return 0;
        } else {
            // Add 1 to include both start and end dates
            return ChronoUnit.DAYS.between(getStartDate(), getEndDate()) + 1;
        }
    }

    /**
     * Sets the cycle length of the current period based on the start date of the next period.
     *
     * @param nextStartDate The start date of the next period.
     */
    public void setCycleLength(LocalDate nextStartDate) {
        this.cycleLength = ChronoUnit.DAYS.between(getStartDate(), nextStartDate);
    }


    /**
     * Calculates the sum of the cycle lengths of the latest three menstrual cycles.
     *
     * @return The sum of the cycle lengths of the latest three menstrual cycles.
     */
    public long getLastThreeCycleLengths() {
        int size = HealthList.getPeriodSize();

        long sumOfCycleLengths = 0;

        int startIndexForPrediction = size - HealthConstant.MIN_SIZE_FOR_PREDICTION;
        assert startIndexForPrediction >= 0 : ErrorConstant.START_INDEX_NEGATIVE_ERROR;

        int endIndexForPrediction = size - HealthConstant.LAST_CYCLE_INDEX_OFFSET;
        assert endIndexForPrediction >= startIndexForPrediction : ErrorConstant.END_INDEX_GREATER_THAN_START_ERROR;

        for (int i = startIndexForPrediction; i <= endIndexForPrediction; i++) {
            sumOfCycleLengths += Objects.requireNonNull(HealthList.getPeriod(i)).cycleLength;
        }

        return sumOfCycleLengths;
    }


    /**
     * Predicts the start date of the next period based on the average cycle length.
     *
     * @return The predicted start date of the next period.
     */
    public LocalDate nextCyclePrediction() {
        long averageCycleLength = getLastThreeCycleLengths() / HealthConstant.LATEST_THREE_CYCLE_LENGTHS;
        return getStartDate().plusDays(averageCycleLength);
    }

    /**
     * Prints a message indicating the number of days until the predicted start date of the next period,
     * or how many days late the period is if the current date is after the predicted start date.
     *
     * @param nextPeriodStartDate The predicted start date of the next period.
     */
    public static void printNextCyclePrediction(LocalDate nextPeriodStartDate) {
        LocalDate today = LocalDate.now();
        long daysUntilNextPeriod = today.until(nextPeriodStartDate, ChronoUnit.DAYS);
        if (today.isBefore(nextPeriodStartDate)) {
            System.out.println(HealthConstant.PREDICTED_START_DATE_MESSAGE
                    + nextPeriodStartDate
                    + HealthConstant.COUNT_DAYS_MESSAGE
                    + daysUntilNextPeriod
                    + ((daysUntilNextPeriod == 1) ?
                    UiConstant.SPLIT_BY_WHITESPACE + HealthConstant.DAY_MESSAGE + UiConstant.FULL_STOP
                    : UiConstant.SPLIT_BY_WHITESPACE + HealthConstant.DAYS_MESSAGE + UiConstant.FULL_STOP));
        }

        if (today.isEqual(nextPeriodStartDate)) {
            System.out.println(HealthConstant.PREDICTED_START_DATE_MESSAGE
                    + nextPeriodStartDate
                    + HealthConstant.PREDICTED_DATE_IS_TODAY_MESSAGE);
        }

        if (today.isAfter(nextPeriodStartDate)) {
            System.out.println(HealthConstant.PREDICTED_START_DATE_MESSAGE
                    + nextPeriodStartDate
                    + HealthConstant.PERIOD_IS_LATE
                    + -daysUntilNextPeriod
                    + ((-daysUntilNextPeriod == 1) ?
                    UiConstant.SPLIT_BY_WHITESPACE + HealthConstant.DAY_MESSAGE + UiConstant.FULL_STOP
                    : UiConstant.SPLIT_BY_WHITESPACE + HealthConstant.DAYS_MESSAGE + UiConstant.FULL_STOP));
        }
    }

    /**
     * Returns the string representation of a Period object.
     *
     * @return A formatted string representing a Period object.
     */
    @Override
    public String toString() {
        String periodLengthUnit = (periodLength == 1) ? HealthConstant.DAY_MESSAGE : HealthConstant.DAYS_MESSAGE;
        String endDateUnit = (getEndDate() == null) ? ErrorConstant.NO_DATE_SPECIFIED_ERROR : getEndDate().toString();
        return String.format(HealthConstant.PRINT_PERIOD_FORMAT,
                getStartDate(),
                endDateUnit,
                getPeriodLength(),
                periodLengthUnit)
                + (this.cycleLength > 0 ? System.lineSeparator()
                + String.format(HealthConstant.PRINT_CYCLE_FORMAT, this.cycleLength) : UiConstant.EMPTY_STRING);
    }
}
