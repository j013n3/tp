package workouts;

import utility.CustomExceptions;
import constants.ErrorConstant;
import utility.Parser;
import constants.UiConstant;
import constants.WorkoutConstant;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a Gym object that contains an ArrayList of GymStation objects.
 */
public class Gym extends Workout {
    protected LocalDate date = null;
    protected ArrayList<GymStation> stations = new ArrayList<>();

    /**
     * Constructor that adds a Gym object to WorkoutList.
     */
    public Gym() {
        WorkoutList.addGym(this);
    }

    /**
     * Overloaded constructor that adds a Gym object to WorkoutList, and also takes the optional date parameter.
     *
     * @param stringDate String representing the date parameter specified.
     */
    public Gym(String stringDate) {
        this.date = Parser.parseDate(stringDate);
        WorkoutList.addGym(this);
    }

    /**
     * Adds station to an ArrayList of GymStation object.
     *
     * @param name        Name of the gym station.
     * @param numberOfSet Number of sets done.
     * @param numberOfRepetitions Number of repetitions done.
     * @param weightsList Weights used for the station.
     * @throws CustomExceptions.InvalidInput If there is invalid input in any parameter.
     */
    public void addStation(String name, int numberOfSet, int numberOfRepetitions,
                           ArrayList<Integer> weightsList) throws CustomExceptions.InvalidInput {
        try {
            GymStation newStation = new GymStation(name, numberOfSet, numberOfRepetitions, weightsList);
            stations.add(newStation);
        } catch (Exception e) {
            throw new CustomExceptions.InvalidInput(WorkoutConstant.INVALID_GYM_INPUT);
        }
    }

    /**
     * Get specific station as part of Gym object based on workout.
     *
     * @return The desired GymStation object.
     */
    public ArrayList<GymStation> getStations() {

        return stations;
    }

    public GymStation getStationByIndex(int index) throws CustomExceptions.OutOfBounds {
        if (index >= stations.size() || index < 0) {
            throw new CustomExceptions.OutOfBounds(WorkoutConstant.INVALID_GYM_STATION_INDEX);
        }
        return stations.get(index);
    }


    @Override
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Retrieves the string representation of a Gym object.
     *
     * @return A formatted string representing the Gym object, inclusive of the date and gym stations done.
     */
    @Override
    public String toString() {
        String printedDate;
        if (date != null) {
            printedDate = date.toString();
            return String.format(" (Date: %s)", printedDate);
        } else {
            return " (Date: NA)";
        }
    }


    /**
     * Retrieves the string representation of a Gym object.
     * Used for the formatting of the Gym Object before writing into a file.
     *
     * @return StringBuilder Object that contains the formatted string.
     */
    private StringBuilder formatFileString(){
        StringBuilder fileString = new StringBuilder();
        String type = WorkoutConstant.GYM.toUpperCase();
        String numOfStation = String.valueOf(stations.size());
        String date;
        if(this.getDate() == null){
            date = ErrorConstant.NO_DATE_SPECIFIED_ERROR;
        } else {
            date = Parser.parseFormattedDate(this.getDate());
        }

        fileString.append(type);
        fileString.append(UiConstant.SPLIT_BY_COLON);
        fileString.append(numOfStation);
        fileString.append(UiConstant.SPLIT_BY_COLON);
        fileString.append(date);
        fileString.append(UiConstant.SPLIT_BY_COLON);
        return fileString;
    }

    /**
     * Converts the Gym Object into the String format for writing into a file.
     * The format that this output is
     *  gym:NUM_STATIONS:DATE:STATION1_NAME:NUM_SETS:REPS:WEIGHT1,WEIGHT2,WEIGHT3,WEIGHT4
     *  :STATION2_NAME:NUM_SETS:REPS:WEIGHT1,WEIGHT2,WEIGHT3,WEIGHT4 ....
     *  Example: "gym:2:1997-11-11:bench press:4:4,4,4,4:10,20,30,40:squats:4:3,3,3,3:20,30,40,50"
     *  Can refer to GymTest {@code toFileString_correctInput_expectedCorrectString()} for more examples
     *
     * @return A formatted string in the format specified above.
     */
    public String toFileString(){

        StringBuilder fileString = formatFileString();
        ArrayList<GymStation> stations = getStations();
        for (GymStation station : stations) {
            fileString.append(station.toFileString());
            if (stations.indexOf(station) != stations.size() - 1) {
                fileString.append(UiConstant.SPLIT_BY_COLON);
            }
        }
        return fileString.toString();
    }

    /**
     * Used when printing all the workouts. This method takes in two parameters {@code isFirstIteration} and {@code i}
     * @param index indicates which particular gymStation is being queried.
     * @return A string representing the history format for gym.
     */
    public String getHistoryFormatForSpecificGymStation(int index) {

        StringBuilder gymDate = new StringBuilder();
        if (date != null) {
            gymDate.append(date);
        } else {
            gymDate.append(ErrorConstant.NO_DATE_SPECIFIED_ERROR);
        }

        // Get the string format for a specific gym station
        GymStation station = getStations().get(index);
        String gymStationString = station.getStationName();
        String gymSetString = String.valueOf(station.getNumberOfSets());

        // Process the reps and weights into string format
        String gymRepString = station.toRepString(UiConstant.COMMAS);
        String gymWeightString = station.toWeightString(UiConstant.COMMAS);

        // If it is first iteration, includes dashes for irrelevant field
        if (index == 0){
            return String.format(WorkoutConstant.HISTORY_WORKOUTS_DATA_FORMAT,
                    WorkoutConstant.GYM, gymDate,
                    UiConstant.DASH,
                    UiConstant.DASH,
                    UiConstant.DASH,
                    gymStationString,
                    gymSetString,
                    gymRepString,
                    gymWeightString);
        } else {
            // if it is not, then leave it blank
            return String.format(WorkoutConstant.HISTORY_WORKOUTS_DATA_FORMAT,
                    UiConstant.EMPTY_STRING,
                    UiConstant.EMPTY_STRING,
                    UiConstant.EMPTY_STRING,
                    UiConstant.EMPTY_STRING,
                    UiConstant.EMPTY_STRING,
                    gymStationString,
                    gymSetString,
                    gymRepString,
                    gymWeightString
            );

        }
    }

}
