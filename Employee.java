import java.util.ArrayList;
import java.util.Date;
import org.joda.time.*;
/**
 * @author Dalton Kruppenbacher
 * @version 0.1
 *
 * ISTE 121.01 CPS:ID2
 * Final Project
 *
 * Class description: The Employee Class is the basis of storage for information pertaining to a particular employee.
 *                    This class stores personal information, payroll information, and contains the calculations to
 *                    add and subtract time between punches.
 *
 *                    The duplication of this code without written consent of the author is strictly prohibited.
 *                    (C) Dalton Kruppenbacher 2020
 *
 * Additional Materials Used: This class uses Date/Time functions released by Joda.org. The .jar file used from joda.org
 *                            is used under the Apache 2.0 license.
 */
public class Employee {
    //global constants
    private final double VACATION_DEFAULT_BALANCE = 0.00;
    private final double SICK_DEFAULT_BALANCE = 5.00;
    private final double OVERTIME_MULTIPLIER = 1.5;
    private final int STANDARD_HOURS = 8;

    //global employee data
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private transient String socialSecurityNumber;

    //global payroll data
    private double vacationDaysBalance;
    private double sickDaysBalance;
    private double hourlyRate;
    private double overtimeRate = hourlyRate * OVERTIME_MULTIPLIER;
    private double totalWagesEarned;
    private double ytdTotal = 0.0;


    //global lists for storing items
    ArrayList<Date> shiftClockDates = new ArrayList<>();
    ArrayList<Double> shiftTimes = new ArrayList<>();

    /**
     * Parameterized Constructor of the Employee Class
     *      Sets employee data for a given employee
     *      Sets payroll information to default values
     * @param firstName An Employee's first name
     * @param lastName An Employee's last name
     * @param streetAddress An Employee's street address (ex. 123 Anywhere Rd.)
     * @param city An Employee's city (ex. Rochester)
     * @param state An Employee's state, non-abbreviated (ex. New York)
     * @param zipCode An Employee's Zipcode (ex. 12345)
     * @param socialSecurityNumber An Employee's Social Security Number (ex. XXX-XX-XXXX)
     * @param hourlyRate An Employee's hourly wage
     *
     * @since 0.1
     */
    public Employee (String firstName, String lastName, String streetAddress, String city, String state, String zipCode,
                     String socialSecurityNumber, double hourlyRate) {
        //TODO Data validation for input
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.socialSecurityNumber = socialSecurityNumber;
        this.hourlyRate = hourlyRate;
        vacationDaysBalance = VACATION_DEFAULT_BALANCE;
        sickDaysBalance = SICK_DEFAULT_BALANCE;
    }//end constructor

    /**
     * A method to handle calculating the employee's total shift time
     * @param clockIn The Time/Date that the Employee's shift started
     * @param clockOut The Time/Date that the Employee's shift ended
     *
     * @since 0.1
     */
    public void calculateDuration(Date clockIn, Date clockOut) {
        final int MINUTES_IN_HOUR = 60;
        DateTime dtClockIn = new DateTime(clockIn);
        DateTime dtClockOut = new DateTime(clockOut);

        int hours = Hours.hoursBetween(dtClockIn, dtClockOut).getHours();
        int minutes = Minutes.minutesBetween(dtClockIn, dtClockOut).getMinutes();

        double duration = hours + (minutes % MINUTES_IN_HOUR);
        addShiftTimes(duration);
    }//end calculateShiftTimes()

    /**
     * A method to generate a String for use in a Comma Separated Value file (.csv)
     * @return A CSV-formatted String
     *
     * @since 0.1
     */
    public String generateCSV() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%2.2f,%2.2f,%2.2f", firstName, lastName, streetAddress, city,
                state, zipCode, socialSecurityNumber, hourlyRate, vacationDaysBalance, sickDaysBalance);
    }//end generateCSV()

    /**
     * A method to add a clock punch Date to an ArrayList of clock punch Dates
     * @param clockTime A clock punch Date(format: dd-MM-yyyy hh:mm)
     *
     * @since 0.1
     */
    public synchronized void addClockDate(Date clockTime) {
        shiftClockDates.add(clockTime);
    }//end addClockDate()

    /**
     * A method to add a duration of time to an ArrayList of durations
     * @param duration The time elapsed between two clock punch Dates
     *
     * @since 0.1
     */
    public synchronized void addShiftTimes(double duration) {
        shiftTimes.add(duration);
    }//end addShiftTime()

    /**
     * Method to calculate wages
     *      Method logic takes into account checking for overtime payment
     * @return totalWagesEarned The total wages earned for a particular pay period, pre tax and deduction
     *
     * @since 0.1
     */
    public double calculateWages() {
        //TODO Add YTD totals
        //TODO Add FTD (Fiscal Year to Date) totals
        for(Double hours : shiftTimes) {
            double normalWagesEarned;
            if(hours > STANDARD_HOURS) {
                double overtimeWagesEarned = (hours - STANDARD_HOURS) * overtimeRate;
                normalWagesEarned = STANDARD_HOURS * hourlyRate;
                totalWagesEarned = normalWagesEarned + overtimeWagesEarned;
            } else {
                normalWagesEarned = STANDARD_HOURS * hourlyRate;
                totalWagesEarned = normalWagesEarned;
            }
        }
        return totalWagesEarned;
    }//end calculateWages()

}
