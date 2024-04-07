package health;

import utility.Parser;
import constants.ErrorConstant;
import constants.UiConstant;
import constants.HealthConstant;

import java.time.LocalDate;

/**
 * The Bmi class extends the Health class.
 * It contains information about the height, weight, and bmi value of the user, and provides functionalities
 * to calculate and categories the bmi values.
 */
public class Bmi extends Health {
    /**
     * The height of the user in meters.
     */
    protected double height;

    /**
     * The weight of the user in kilograms.
     */
    protected double weight;

    /**
     * The Bmi of the user.
     */
    protected double bmiValue;

    /**
     * The BMI category.
     */
    protected String bmiCategory;

    /**
     * The date of user input.
     */
    protected LocalDate date;

    //@@author j013n3
    /**
     * Constructor for Bmi object.
     *
     * @param height A string representing the user's height.
     * @param weight A string representing the user's weight.
     * @throws AssertionError If height or weight values are not positive.
     */
    public Bmi(String height, String weight, String date) {

        this.height = Double.parseDouble(height);
        this.weight = Double.parseDouble(weight);

        assert this.height > 0 && this.weight > 0 : ErrorConstant.NEGATIVE_VALUE_ERROR;

        this.date = Parser.parseDate(date);

        this.bmiValue = calculateBmiValue();
        this.bmiCategory = getBmiCategory(bmiValue);
    }

    /**
     * Retrieves BMI value recorded in Bmi object of String type.
     *
     * @return The BMI value recorded in the Bmi object.
     */
    public String getBmiValue() {
        return String.format(HealthConstant.TWO_DECIMAL_PLACE_FORMAT, bmiValue);
    }

    /**
     * Retrieves height recorded in Bmi object of String type.
     *
     * @return The height recorded in the Bmi object.
     */
    public String getHeight() {
        return String.format(HealthConstant.TWO_DECIMAL_PLACE_FORMAT, height);
    }

    /**
     * Retrieves weight recorded in Bmi object of String type.
     *
     * @return The weight recorded in the Bmi object.
     */
    public String getWeight() {
        return String.format(HealthConstant.TWO_DECIMAL_PLACE_FORMAT, weight);
    }

    /**
     * Retrieves date recorded in Bmi object of LocalDate type.
     *
     * @return The date recorded in the Bmi object.
     */
    public LocalDate getDate() {
        return date;
    }

    //@@author j013n3
    /**
     * Calculates the Bmi value based on height and weight.
     *
     * @return The calculated Bmi value.
     * @throws AssertionError If calculated value is not positive.
     */
    public double calculateBmiValue() {
        double bmi = Math.round((weight / (Math.pow(height, UiConstant.POWER_OF_TWO))) * UiConstant.ROUNDING_FACTOR)
                / UiConstant.ROUNDING_FACTOR;
        assert bmi > 0: ErrorConstant.NEGATIVE_BMI_ERROR;
        return bmi;
    }

    /**
     * Prints the Bmi category based on the calculated Bmi value.
     *
     * @param bmiValue The Bmi value to categorize.
     * @return A string presenting the Bmi category.
     * @throws AssertionError If calculated value is not positive.
     */
    public static String getBmiCategory(double bmiValue) {
        assert bmiValue > 0: ErrorConstant.NEGATIVE_BMI_ERROR;

        if (bmiValue < HealthConstant.UNDERWEIGHT_BMI_THRESHOLD) {
            return HealthConstant.UNDERWEIGHT_MESSAGE;
        } else if (bmiValue < HealthConstant.NORMAL_BMI_THRESHOLD) {
            return HealthConstant.NORMAL_WEIGHT_MESSAGE;
        } else if (bmiValue < HealthConstant.OVERWEIGHT_BMI_THRESHOLD) {
            return HealthConstant.OVERWEIGHT_MESSAGE;
        } else if (bmiValue < HealthConstant.OBESE_BMI_THRESHOLD) {
            return HealthConstant.OBESE_MESSAGE;
        } else {
            return HealthConstant.SEVERELY_OBESE_MESSAGE;
        }
    }
    
    /**
     * Returns the string presentation of a Bmi object.
     *
     * @return A formatted string representing a Bmi object.
     */
    @Override
    public String toString() {
        return String.format(HealthConstant.PRINT_BMI_FORMAT,
                this.getDate(),
                this.calculateBmiValue(),
                getBmiCategory(bmiValue));
    }
}
