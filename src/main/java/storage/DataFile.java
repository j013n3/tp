//@@author L5-Z
package storage;

import java.io.FileWriter;
import java.nio.file.Files;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import health.Appointment;
import health.Bmi;
import health.HealthList;
import health.Period;
import constants.ErrorConstant;
import ui.Output;
import utility.Parser;
import workouts.Gym;
import workouts.Run;
import workouts.Workout;
import utility.CustomExceptions;
import constants.UiConstant;
import utility.Filters.DataType;

/**
 * Represents a DataFile object used to read and write data stored in PulsePilot to a file.
 */
public class DataFile {

    public static String userName = null;
    private static DataFile instance = null;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private DataFile() {
        loadDataFile();
    }

    /**
     * Returns a singular instance of the DataFile class.
     * If the instance is null, it creates a new instance.
     *
     * @return An instance of the DataFile class.
     */
    public static DataFile getInstance() {
        if (instance == null) {
            instance = new DataFile();
        }
        return instance;
    }

    /**
     * Checks if data file already exists. If it does, log it. Else, create the file and log the event.
     *
     * @param dataFile Represents the data file.
     * @return Integer representing a found data file, 0, or not found, 1
     * @throws CustomExceptions.FileCreateError If there is an error creating the data file.
     */
    public static int verifyIntegrity(File dataFile) throws CustomExceptions.FileCreateError {
        try {
            if (dataFile.createNewFile()) {
                LogFile.writeLog("Created new data file", false);
                return UiConstant.FILE_NOT_FOUND;
            } else {
                LogFile.writeLog("Reading from existing data file", false);
                return UiConstant.FILE_FOUND;
            }
        } catch (IOException e) {
            throw new CustomExceptions.FileCreateError(ErrorConstant.CREATE_FILE_ERROR);
        }
    }

    /**
     * Initialises the data file to be used. Function exits if file cannot be created.
     */
    public static int loadDataFile() {
        int status = UiConstant.FILE_NOT_FOUND;
        try {
            status = verifyIntegrity(UiConstant.SAVE_FILE);
        } catch (CustomExceptions.FileCreateError e) {
            System.err.println(ErrorConstant.CREATE_FILE_ERROR);
            LogFile.writeLog(ErrorConstant.CREATE_FILE_ERROR, true);
            System.exit(1);
        }
        Path dataFilePath = Path.of(UiConstant.DATA_FILE_PATH);
        assert Files.exists(dataFilePath) : "Data file does not exist.";
        return status;
    }

    /**
     * Function reads the existing data file, and adds the relevant data to PulsePilot.
     * @throws CustomExceptions.FileReadError If there is an error reading the data file.
     */
    public static void readDataFile() throws CustomExceptions.FileReadError {
        int lineNumberCount = 0; // just for getting lineNumber, no other use
        try (final Scanner readFile = new Scanner(UiConstant.SAVE_FILE)) {
            LogFile.writeLog("Read begins", false);
            try {
                String [] input = readFile.nextLine().split(UiConstant.SPLIT_BY_COLON);
                String dataType = input[UiConstant.DATA_TYPE_INDEX].trim();
                if (dataType.equalsIgnoreCase(UiConstant.NAME_LABEL)) {
                    String name = input[UiConstant.NAME_INDEX].trim();
                    processName(name);
                } else {
                    Output.printException(ErrorConstant.CORRUPT_ERROR);
                    System.exit(1);
                }

            } catch (Exception e) {
                LogFile.writeLog("Data file is missing name, exiting." + e, true);
                Output.printException(ErrorConstant.CORRUPT_ERROR);
                System.exit(1);
            }

            while (readFile.hasNextLine()) {
                String rawInput = readFile.nextLine();
                String [] input = rawInput.split(UiConstant.SPLIT_BY_COLON);

                String dataType = input[UiConstant.DATA_TYPE_INDEX].trim();

                DataType filter = DataType.valueOf(dataType);
                switch (filter){

                case APPOINTMENT:
                    processAppointment(input);
                    break;
                
                case PERIOD:
                    processPeriod(input);
                    break;

                case BMI:
                    processBmi(input);
                    break;

                case GYM:
                    processGym(rawInput);
                    break;

                case RUN:
                    processRun(input);
                    break;

                default:
                    break; // valueOf results in immediate exception for non-match with enum DataType
                }
                lineNumberCount += 1;
            }
        } catch (Exception e) {
            LogFile.writeLog("Invalid item read at line: " + (lineNumberCount + 1) + "! " + e, true);
            throw new CustomExceptions.FileReadError(ErrorConstant.PARTIAL_CORRUPT_ERROR);
        }
    }
    public static void processName(String name){
        userName = name.trim();
    }

    // appointment format: appointment:DATE:TIME:DESCRIPTION
    public static void processAppointment(String[] input) {
        String date = input[1].trim(); // date
        String time = input[2].trim(); // time
        String formattedTime = time.replace(".", ":");
        String description = input[3].trim(); // description
        Appointment appointment = new Appointment(date, formattedTime, description);
        HealthList.addAppointment(appointment);
    }

    // period format: period:START:END:DURATION
    public static void processPeriod(String[] input) {
        String startDate = input[1].trim(); // start
        String endDate = input[2].trim(); // end, skip 3 duration
        Period period = new Period(startDate, endDate);
        HealthList.addPeriod(period);
    }

    // bmi format: bmi:HEIGHT:WEIGHT:BMI_SCORE:DATE (NA if no date)
    public static void processBmi(String[] input) {
        String height = input[1].trim(); // height
        String weight = input[2].trim(); // weight
        String date = input[4].trim();// skip 3, bmi score, 4 is date
        Bmi bmi = new Bmi(height, weight, date);
        HealthList.addBmi(bmi);
    }

    // run format: run:DISTANCE:TIME:PACE:DATE
    public static void processRun(String[] input) throws CustomExceptions.InvalidInput {
        String distance = input[1].trim(); // distance
        String time = input[2].trim(); // time
        String formattedTime = time.replace(".", ":");
        String date = input[3].trim(); // 3 is date
        if (date.equals(ErrorConstant.NO_DATE_SPECIFIED_ERROR)) {
            new Run(formattedTime, distance);
        } else {
            new Run(formattedTime, distance, date);
        }
    }

    public static void processGym(String rawInput)
            throws CustomExceptions.InvalidInput, CustomExceptions.FileReadError {
        Parser.parseGymFileInput(rawInput);
    }

    /**
     * Saves the user data to a file.
     *
     * // param health data of all specified user input to be saved.
     * @throws CustomExceptions If an error occurs during file operations.
     */
    public static void saveDataFile(String name,
                                    ArrayList<Bmi> bmiArrayList,
                                    ArrayList<Appointment> appointmentArrayList,
                                    ArrayList<Period> periodArrayList,
                                    ArrayList<Workout> workoutArrayList
                                    ) throws CustomExceptions.FileWriteError {

        try (FileWriter dataFile = new FileWriter(UiConstant.DATA_FILE_PATH)) {
            LogFile.writeLog("Attempting to write data, name: " + name, false);
            writeName(dataFile, name);
            LogFile.writeLog("Written name", false);
            writeHealthData(dataFile, bmiArrayList,
                    appointmentArrayList,
                    periodArrayList);

            writeWorkoutData(dataFile, workoutArrayList);

            LogFile.writeLog("Write end", false);
            dataFile.close();

        } catch (IOException e) {
            throw new CustomExceptions.FileWriteError(ErrorConstant.SAVE_ERROR);
        }
    }

    /**
     * Writes health data to the data file.
     *
     * // param healthData Health data to be written.
     */
    public static void writeName(FileWriter dataFile, String name) throws IOException {
        dataFile.write(UiConstant.NAME_LABEL + UiConstant.SPLIT_BY_COLON + name.trim() + System.lineSeparator());
        LogFile.writeLog("Wrote name to file", false);
    }

    /**
     * Writes health data to the data file.
     *
     * // param healthData Health data to be written.
     */
    public static void writeHealthData(FileWriter dataFile, ArrayList<Bmi> bmiArrayList,
                                       ArrayList<Appointment> appointmentArrayList,
                                       ArrayList<Period> periodArrayList) throws IOException {

        // Write each bmi entry in a specific format
        // bmi format: bmi:HEIGHT:WEIGHT:BMI_SCORE:DATE (NA if no date)
        if (!bmiArrayList.isEmpty()){
            for (Bmi bmiEntry : bmiArrayList) {
                String formattedDate = Parser.parseFormattedDate(bmiEntry.getDate());

                dataFile.write(DataType.BMI + UiConstant.SPLIT_BY_COLON + bmiEntry.getHeight() +
                        UiConstant.SPLIT_BY_COLON + bmiEntry.getWeight() +
                        UiConstant.SPLIT_BY_COLON + bmiEntry.getBmiValue() +
                        UiConstant.SPLIT_BY_COLON + formattedDate + System.lineSeparator());
            }
        }

        // Write each appointment entry in a specific format
        // appointment format: appointment:DATE:TIME:DESCRIPTION
        if (!appointmentArrayList.isEmpty()){
            for (Appointment appointmentEntry : appointmentArrayList) {
                String formattedDate = Parser.parseFormattedDate(appointmentEntry.getDate());
                String formattedTime = String.valueOf(appointmentEntry.getTime());
                formattedTime = formattedTime.replace(":", ".");

                dataFile.write(DataType.APPOINTMENT + UiConstant.SPLIT_BY_COLON + formattedDate +
                        UiConstant.SPLIT_BY_COLON + formattedTime +
                        UiConstant.SPLIT_BY_COLON + appointmentEntry.getDescription() + System.lineSeparator());
            }
        }


        // Write each period entry in a specific format
        // period format: period:START:END:DURATION
        if (!periodArrayList.isEmpty()){
            for (Period periodEntry : periodArrayList) {
                LogFile.writeLog("Writing period to file", false);
                String formattedStartDate = Parser.parseFormattedDate(periodEntry.getStartDate());
                String formattedEndDate = Parser.parseFormattedDate(periodEntry.getEndDate());

                dataFile.write(DataType.PERIOD + UiConstant.SPLIT_BY_COLON + formattedStartDate +
                        UiConstant.SPLIT_BY_COLON + formattedEndDate +
                        UiConstant.SPLIT_BY_COLON + periodEntry.getPeriodLength() + System.lineSeparator());
                LogFile.writeLog("Wrote period to file", false);
            }
        }

    }

    /**
     * Writes Workout data to the data file.
     * // param workoutData Workout data to be written.
     */
    public static void writeWorkoutData(FileWriter dataFile,
                                        ArrayList<Workout> workoutArrayList) throws IOException {

        // Write each run entry in a specific format
        // run format: run:DISTANCE:TIME:DATE
        if (!workoutArrayList.isEmpty()){
            for (Workout workoutEntry : workoutArrayList) {
                if (workoutEntry instanceof Run) {
                    Run runEntry = (Run) workoutEntry;
                    String formattedDate = Parser.parseFormattedDate(runEntry.getDate());
                    String formattedTime = runEntry.getTimes().replace(":", ".");

                    dataFile.write(DataType.RUN + UiConstant.SPLIT_BY_COLON + runEntry.getDistance() +
                            UiConstant.SPLIT_BY_COLON + formattedTime +
                            UiConstant.SPLIT_BY_COLON + formattedDate + System.lineSeparator());
                } else if (workoutEntry instanceof Gym) {
                    Gym gymEntry = (Gym) workoutEntry;
                    String gymString = gymEntry.toFileString();
                    dataFile.write(gymString);
                }
            }
        }
    }
}

