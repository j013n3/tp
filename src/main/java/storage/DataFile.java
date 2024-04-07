//@@author L5-Z
package storage;

import java.io.FileWriter;
import java.nio.file.Files;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
 *
 * This class handles the reading and writing of various data related to PulsePilot, including user's name,
 * health data like BMI, appointments, periods, and workout data like runs and gym sessions.
 * It provides methods to load, save, and process different types of data as well as prevent tampering.
 */
public class DataFile {

    public static String userName = null;
    private static DataFile instance = null;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the data file.
     */
    private DataFile() {
        loadDataFile();
    }

    /**
     * Retrieves the singleton instance of DataFile class.
     * If the instance is null, it creates a new instance.
     *
     * @return Singleton instance of DataFile class.
     */
    public static DataFile getInstance() {
        if (instance == null) {
            instance = new DataFile();
        }
        return instance;
    }

    /**
     * Generates the SHA-256 hash value of the pulsepilot_data.txt file.
     *
     * @param file The file for which to generate the hash.
     * @return A String representing the SHA-256 hash value of the pulsepilot_data.txt file.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    private static String generateFileHash(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file);
        byte[] dataBytes = new byte[1024];

        int bytesRead;
        while ((bytesRead = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, bytesRead);
        }

        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }

        fis.close();
        return sb.toString();
    }

    /**
     * Verifies the integrity of the data file by checking its existence and hs value.
     * If the data file already exists, checks its hash value against the expected hash value.
     * If the data file does not exist, creates a new file and logs the event.
     *
     * @param dataFile The data file to verify integrity.
     * @return An integer representing whether the file was found (0) or not found (1).
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
     * Initializes the data file to be used. Or loads the existing data file, verifies its integrity, and processes
     * its content. The function exits if the file cannot be created or loaded.
     *
     * @return An integer representing whether the file was found (0) or not found (1).
     */
    public static int loadDataFile() {
        int status = UiConstant.FILE_NOT_FOUND;
        try {
            File dataFile = UiConstant.SAVE_FILE;
            File hashFile = new File(UiConstant.HASH_FILE_PATH);

            if (dataFile.exists() && hashFile.exists()) {
                String expectedHash = generateFileHash(dataFile);
                String actualHash = readHashFromFile(hashFile);

                if (expectedHash.equals(actualHash)) {
                    status = verifyIntegrity(dataFile);
                } else {
                    LogFile.writeLog(ErrorConstant.DATA_INTEGRITY_ERROR, true);
                    Output.printException(ErrorConstant.DATA_INTEGRITY_ERROR);
                    hashFile.delete();
                    dataFile.delete();
                    System.exit(1);
                }
            } else if (!dataFile.exists() && !hashFile.exists()) {
                status = verifyIntegrity(dataFile);
            } else {
                LogFile.writeLog(ErrorConstant.MISSING_INTEGRITY_ERROR, true);
                Output.printException(ErrorConstant.MISSING_INTEGRITY_ERROR);
                hashFile.delete();
                dataFile.delete();
                System.exit(1);
            }
        } catch (CustomExceptions.FileCreateError e) {
            System.err.println(ErrorConstant.CREATE_FILE_ERROR);
            LogFile.writeLog(ErrorConstant.CREATE_FILE_ERROR, true);
            System.exit(1);
        } catch (IOException | NoSuchAlgorithmException e) {
            LogFile.writeLog("Error occurred while processing file hash: " + e.getMessage(), true);
            Output.printException(ErrorConstant.HASH_ERROR);
            System.exit(1);
        }

        Path dataFilePath = Path.of(UiConstant.DATA_FILE_PATH);
        assert Files.exists(dataFilePath) : "Data file does not exist.";
        return status;
    }

    /**
     * Reads the hash value from a hash file.
     *
     * @param hashFile The hash file to read from.
     * @return The hash value read from the file.
     * @throws IOException If an I/O error occurs.
     */
    private static String readHashFromFile(File hashFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(hashFile);
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
        }
        scanner.close();
        return sb.toString();
    }

    /**
     * Writes the hash value to a hash file.
     *
     * @param hashFile The hash file to write to.
     * @param hash     The hash value to write.
     * @throws IOException If an I/O error occurs.
     */
    private static void writeHashToFile(FileWriter hashFile, String hash) throws IOException {
        FileOutputStream fos = new FileOutputStream(UiConstant.HASH_FILE_PATH);
        fos.write(hash.getBytes());
        fos.close();
    }

    /**
     * Reads data from the existing data file and processes it.
     *
     * @throws CustomExceptions.FileReadError If there is an error reading the data file.
     */
    public static void readDataFile() throws CustomExceptions.FileReadError {
        int lineNumberCount = 0; // just for getting lineNumber, no other use
        try (final Scanner readFile = new Scanner(UiConstant.SAVE_FILE)) {
            LogFile.writeLog("Read begins", false);
            try {
                String [] input = readFile.nextLine().split(UiConstant.SPLIT_BY_COLON);
                String name = input[UiConstant.NAME_INDEX].trim();
                processName(name);

            } catch (Exception e) {
                LogFile.writeLog("Data file is missing name, exiting." + e, true);
                Output.printException(ErrorConstant.CORRUPT_ERROR);
                File dataFile = UiConstant.SAVE_FILE;
                File hashFile = new File(UiConstant.HASH_FILE_PATH);
                dataFile.delete();
                hashFile.delete();
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
            LogFile.writeLog("Data file is corrupted, exiting." + e, true);
            Output.printException(ErrorConstant.CORRUPT_ERROR);
            File dataFile = UiConstant.SAVE_FILE;
            File hashFile = new File(UiConstant.HASH_FILE_PATH);
            dataFile.delete();
            hashFile.delete();
            System.exit(1);
        }
    }

    /**
     * Processes the user name from the data file.
     *
     * @param name The user name read from the data file.
     */
    public static void processName(String name){
        userName = name.trim();
    }

    /**
     * Processes an appointment entry from the input string array and adds it to the health list.
     *
     * @param input The input string array containing appointment data.
     */
    public static void processAppointment(String[] input) {
        String date = input[1].trim(); // date
        String time = input[2].trim(); // time
        String formattedTime = time.replace(".", ":");
        String description = input[3].trim(); // description
        Appointment appointment = new Appointment(date, formattedTime, description);
        HealthList.addAppointment(appointment);
    }

    /**
     * Processes a period entry from the input string array and adds it to the health list.
     *
     * @param input The input string array containing period data.
     */
    public static void processPeriod(String[] input) {
        String startDate = input[1].trim(); // start
        String endDate = input[2].trim(); // end, skip 3 duration
        if (endDate.equals(ErrorConstant.NO_DATE_SPECIFIED_ERROR)) {
            Period period = new Period(startDate);
            HealthList.addPeriod(period);
        } else {
            Period period = new Period(startDate, endDate);
            HealthList.addPeriod(period);
        }
    }

    /**
     * Processes a BMI entry from the input string array and adds it to the health list.
     *
     * @param input The input string array containing BMI data.
     */
    public static void processBmi(String[] input) {
        String height = input[1].trim(); // height
        String weight = input[2].trim(); // weight
        String date = input[4].trim();// skip 3, bmi score, 4 is date
        Bmi bmi = new Bmi(height, weight, date);
        HealthList.addBmi(bmi);
    }

    /**
     * Processes a run entry from the input string array and adds it to the health list.
     *
     * @param input The input string array containing run data.
     * @throws CustomExceptions.InvalidInput If there is an error in the input data format.
     */
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

    /**
     * Processes a gym entry from the raw input string and delegates parsing to Parser class.
     *
     * @param rawInput The raw input string containing gym data.
     * @throws CustomExceptions.InvalidInput   If there is an error in the input data format.
     * @throws CustomExceptions.FileReadError If there is an error reading the gym file.
     */
    public static void processGym(String rawInput)
            throws CustomExceptions.InvalidInput, CustomExceptions.FileReadError {
        Parser.parseGymFileInput(rawInput);
    }

    /**
     * Saves data to the data file.
     *
     * @param name               The user name to be saved.
     * @param bmiArrayList       List of BMI entries to be saved.
     * @param appointmentArrayList List of appointment entries to be saved.
     * @param periodArrayList    List of period entries to be saved.
     * @param workoutArrayList   List of workout entries to be saved.
     * @throws CustomExceptions.FileWriteError If there is an error writing to the data file.
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

        try (FileWriter hashFile = new FileWriter(UiConstant.HASH_FILE_PATH)) {
            LogFile.writeLog("Attempting to write hash", false);
            File dataFile = UiConstant.SAVE_FILE;
            writeHashToFile(hashFile, generateFileHash(dataFile));

            LogFile.writeLog("Write end", false);
            hashFile.close();

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new CustomExceptions.FileWriteError(ErrorConstant.SAVE_ERROR);
        }
    }

    /**
     * Writes the user's name to the data file.
     *
     * @param dataFile The FileWriter object for writing to the data file.
     * @param name     The user's name to be written to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void writeName(FileWriter dataFile, String name) throws IOException {
        dataFile.write(UiConstant.NAME_LABEL + UiConstant.SPLIT_BY_COLON + name.trim() + System.lineSeparator());
        LogFile.writeLog("Wrote name to file", false);
    }

    /**
     * Writes health-related data (BMI, appointments, periods) to the data file.
     *
     * @param dataFile             The FileWriter object for writing to the data file.
     * @param bmiArrayList         The list of BMI entries to be written.
     * @param appointmentArrayList The list of appointment entries to be written.
     * @param periodArrayList      The list of period entries to be written.
     * @throws IOException If an I/O error occurs while writing to the file.
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
     * Writes workout-related data (runs and gym sessions) to the data file.
     *
     * @param dataFile         The FileWriter object for writing to the data file.
     * @param workoutArrayList The list of workout entries to be written.
     * @throws IOException If an I/O error occurs while writing to the file.
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
                    dataFile.write(gymString + System.lineSeparator());
                }
            }
        }
    }
}

