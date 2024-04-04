package health;

import storage.LogFile;
import utility.CustomExceptions;
import constants.ErrorConstant;
import constants.HealthConstant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents the list of BMI objects stored.
 */
public class HealthList extends ArrayList<Health> {

    /**
     * LogFile for logging health-related activities.
     */
    static LogFile logFile = LogFile.getInstance();

    /**
     * The list of Bmi records.
     */
    private static final ArrayList<Bmi> BMIS = new ArrayList<>();

    /**
     * The list of Appointment records.
     */
    private static final ArrayList<Appointment> APPOINTMENTS = new ArrayList<>();

    /**
     * The list of Period records.
     */
    private static final ArrayList<Period> PERIODS = new ArrayList<>();


    //@@author j013n3
    /**
     * Adds a Bmi to the list of Bmis whenever addBmi is called.
     *
     * @param bmi Bmi object.
     * @throws AssertionError If Bmi object is null.
     */
    public static void addBmi(Bmi bmi) {
        assert bmi != null : ErrorConstant.NULL_BMI_ERROR;
        BMIS.add(bmi);
    }

    //@@author syj02
    /**
     * Prints the most recently added Bmi object from bmis list.
     *
     * @throws AssertionError If bmis list is empty.
     */
    public static void showCurrentBmi() {
        assert !BMIS.isEmpty() : ErrorConstant.EMPTY_BMI_LIST_ERROR;
        int currentIndex = BMIS.size();
        System.out.println(BMIS.get(currentIndex - 1));
    }

    /**
     * Prints all the BMI entries recorded.
     *
     * @throws AssertionError If bmis list is empty.
     */
    public static void showBmiHistory() {
        assert !BMIS.isEmpty() : ErrorConstant.EMPTY_BMI_LIST_ERROR;
        for (Bmi bmi : BMIS) {
            System.out.println(bmi);
        }
    }

    /**
     * Adds a period to the ArrayList of periods.
     *
     * @param period Period object to be added.
     * @throws AssertionError If period object is null.
     */
    public static void addPeriod(Period period) {
        assert period != null : ErrorConstant.NULL_PERIOD_ERROR;
        if (!PERIODS.isEmpty()) {
            Period previousPeriod = PERIODS.get(PERIODS.size() - 1);
            previousPeriod.setCycleLength(period.getStartDate());
        }
        PERIODS.add(period);
    }

    /**
     * Prints the latest period object added.
     *
     * @throws AssertionError If periods list is empty.
     */
    public static void showLatestPeriod() {
        assert !PERIODS.isEmpty() : ErrorConstant.EMPTY_PERIOD_LIST_ERROR;
        int currentIndex = PERIODS.size();
        System.out.println(PERIODS.get(currentIndex - 1));
    }

    //@@author j013n3
    /**
     * Prints all Period entries tracked.
     *
     * @throws AssertionError If periods list is empty.
     */
    public static void showPeriodHistory() {
        assert !PERIODS.isEmpty() : ErrorConstant.EMPTY_PERIOD_LIST_ERROR;
        for (Period period : PERIODS) {
            System.out.println(period);
        }
    }

    /**
     * Prints the latest three Period objects from the list of periods.
     */
    public static void printLatestThreeCycles() {
        int size = PERIODS.size();
        int startIndex = size - HealthConstant.LATEST_THREE_CYCLE_LENGTHS;
        assert startIndex >= 0 : ErrorConstant.START_INDEX_NEGATIVE_ERROR;

        int endIndex = size - HealthConstant.LAST_CYCLE_OFFSET;

        for (int i = startIndex; i <= endIndex; i++) {
            System.out.println(PERIODS.get(i));
        }
    }

    /**
     * Retrieves the list of periods recorded.
     *
     * @return The periods array list.
     */
    public static ArrayList<Period> getPeriods(){
        return PERIODS;
    }

    /**
     * Retrieves the list of bmis recorded.
     *
     * @return The bmis array list.
     */
    public static ArrayList<Bmi> getBmis(){
        return BMIS;
    }

    /**
     * Retrieves the list of appointments recorded.
     *
     * @return The appointments array list.
     */
    public static ArrayList<Appointment> getAppointments(){
        return APPOINTMENTS;
    }

    /**
     * Retrieves the number of periods recorded.
     *
     * @return The number of periods recorded.
     */
    public static int getPeriodSize() {
        return PERIODS.size();
    }

    /**
     * Gets the Period object at the specified index.
     *
     * @param index The index of the Period object.
     * @return The Period object at the specified index, or null if the index is out of bounds.
     */
    public static Period getPeriod(int index) {
        if (index < 0 || index >= PERIODS.size()) {
            return null;
        }
        return PERIODS.get(index);
    }

    /**
     * Predicts the start date of the next period based on the average cycle length of the last three cycles.
     *
     * @return The predicted start date of the next period.
     * @throws AssertionError If periods lists is empty.
     */
    public static LocalDate predictNextPeriodStartDate() {
        assert !PERIODS.isEmpty() : ErrorConstant.EMPTY_PERIOD_LIST_ERROR;

        Period latestPeriod = PERIODS.get(PERIODS.size() - 1);
        return latestPeriod.nextCyclePrediction();
    }

    //@@l5_z
    /**
     * Clears the Period, Bmi, and Appointment array lists.
     * @throws AssertionError If periods, bmis, and appointments lists are not empty.
     */
    public static void clearHealthLists() {
        PERIODS.clear();
        BMIS.clear();
        APPOINTMENTS.clear();
        assert BMIS.isEmpty() : ErrorConstant.BMI_LIST_UNCLEARED_ERROR;
        assert PERIODS.isEmpty() : ErrorConstant.PERIOD_LIST_UNCLEARED_ERROR;
        assert APPOINTMENTS.isEmpty() : ErrorConstant.APPOINTMENT_LIST_UNCLEARED_ERROR;
    }

    /**
     * Retrieves size of periods list.
     *
     * @return Size of periods list.
     */
    public static int getPeriodsSize() {
        return PERIODS.size();
    }

    /**
     * Retrieves size of bmis list.
     *
     * @return Size of bmis list.
     */
    public static int getBmisSize() {
        return BMIS.size();
    }

    /**
     * Deletes Bmi object based on index.
     *
     * @param index Index of the Bmi object to be deleted.
     */
    public static void deleteBmi(int index) throws CustomExceptions.OutOfBounds {
        assert !BMIS.isEmpty() : ErrorConstant.EMPTY_BMI_LIST_ERROR;
        if (index < 0 || index >= BMIS.size()) {
            throw new CustomExceptions.OutOfBounds(ErrorConstant.INVALID_INDEX_DELETE_ERROR);
        }
        Bmi deletedBmi = BMIS.get(index);
        System.out.printf((HealthConstant.LOG_DELETE_BMI_FORMAT) + "%n",
                        deletedBmi.bmiValue,
                        deletedBmi.date);
        BMIS.remove(index);
        LogFile.writeLog(HealthConstant.BMI_REMOVED_MESSAGE_PREFIX + index, false);
    }

    /**
     * Deletes Bmi object based on index.
     *
     * @param index Index of the Bmi object to be deleted.
     */
    public static void deletePeriod(int index) throws CustomExceptions.OutOfBounds {
        assert !PERIODS.isEmpty() : ErrorConstant.EMPTY_PERIOD_LIST_ERROR;
        if (index < 0 || index >= PERIODS.size()) {
            throw new CustomExceptions.OutOfBounds(ErrorConstant.INVALID_INDEX_DELETE_ERROR);
        }
        Period deletedPeriod = PERIODS.get(index);
        System.out.printf((HealthConstant.LOG_DELETE_PERIOD_FORMAT) + "%n",
                deletedPeriod.getStartDate(),
                deletedPeriod.getEndDate());
        PERIODS.remove(index);
        LogFile.writeLog(HealthConstant.PERIOD_REMOVED_MESSAGE_PREFIX + index, false);
    }

    //@@author syj_02
    /**
     * Adds an Appointment to the list of Appointments whenever addAppointment is called.
     * Sorts all Appointment objects in the list by date and time of the appointments with
     * the earliest appointment at the top.
     *
     * @param appointment Appointment object.
     * @throws AssertionError If Appointment object is null.
     */
    public static void addAppointment(Appointment appointment) {
        assert appointment != null : ErrorConstant.NULL_APPOINTMENT_ERROR;
        APPOINTMENTS.add(appointment);
        APPOINTMENTS.sort(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getTime));
    }

    /**
     * Deletes Appointment object based on index.
     *
     * @param index Index of the Appointment object to be deleted.
     * @throws CustomExceptions.OutOfBounds If the index is out of bounds.
     */
    public static void deleteAppointment(int index) throws CustomExceptions.OutOfBounds {
        assert !APPOINTMENTS.isEmpty() : ErrorConstant.EMPTY_APPOINTMENT_LIST_ERROR;
        if (index < 1 || index > APPOINTMENTS.size()) {
            throw new CustomExceptions.OutOfBounds(ErrorConstant.INVALID_INDEX_DELETE_ERROR);
        }
        index -= 1;
        Appointment deletedAppointment = APPOINTMENTS.get(index);
        System.out.printf((HealthConstant.LOG_DELETE_APPOINTMENT_FORMAT) + "%n",
                deletedAppointment.date,
                deletedAppointment.time,
                deletedAppointment.description);
        APPOINTMENTS.remove(index);
        LogFile.writeLog(HealthConstant.APPOINTMENT_REMOVED_MESSAGE_PREFIX + index, false);
        showAppointmentList();
    }

    /**
     * Prints all Appointment entries tracked.
     *
     * @throws AssertionError If appointments list is empty.
     */
    public static void showAppointmentList() {
        assert !APPOINTMENTS.isEmpty() : ErrorConstant.EMPTY_APPOINTMENT_LIST_ERROR;
        int index = 1;
        for (Appointment appointment: APPOINTMENTS) {
            System.out.print(index + ". ");
            System.out.println(appointment);
            index += 1;
        }
    }

    /**
     * Prints the latest Appointment object added.
     *
     * @throws AssertionError If Appointments list is empty.
     */
    public static void showLatestAppointment() {
        assert !APPOINTMENTS.isEmpty(): ErrorConstant.EMPTY_APPOINTMENT_LIST_ERROR;
        int currentIndex = APPOINTMENTS.size();
        System.out.println(APPOINTMENTS.get(currentIndex - 1));
    }
}
