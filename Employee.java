import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.json.simple.*;

/**
 * @author Dalton Kruppenbacher
 * @version 0.4
 * Revision Notes: Added Boolean Methods to test for clockedIn Status
 *
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
 */
public class Employee implements Serializable {
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
    private String position;

    //global employee account data
    private transient String userName;
    private transient String password;

    //global payroll data
    private double vacationDaysBalance;
    private double sickDaysBalance;
    private double hourlyRate;
    private double overtimeRate = hourlyRate * OVERTIME_MULTIPLIER;
    private double totalWagesEarned;
    private double ytdTotal;
    private boolean clockedIn;
    private int id;

    private JSONObject employeeData = new JSONObject();

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
     * @param position An Employee's company position
     * @param hourlyRate An Employee's hourly wage
     *
     * @since 0.1
     */
    public Employee (JSONObject employeeData) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.socialSecurityNumber = socialSecurityNumber;
        this.position = position;
        this.hourlyRate = hourlyRate;
        userName = firstName + "_" + lastName;
        password = "password";
        vacationDaysBalance = VACATION_DEFAULT_BALANCE;
        sickDaysBalance = SICK_DEFAULT_BALANCE;
        ytdTotal = 0.0;
        clockedIn = false;
        this.employeeData = employeeData;
    }//end constructor

    /**
     * A method to handle calculating the employee's total shift time
     * @param clockIn The Time/Date that the Employee's shift started
     * @param clockOut The Time/Date that the Employee's shift ended
     *
     * @since 0.3
     */
    public void calculateDuration(Date clockIn, Date clockOut) {
        long millis = 0;
        final int MINUTES_IN_HOUR = 60;

        //check to make sure clock out time isn't "smaller" than clock in time
        if (clockOut.getTime() - clockIn.getTime() < 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(clockOut);
            cal.add(Calendar.DATE, 1);
            clockOut = cal.getTime();
        } else {
            millis = clockOut.getTime() - clockIn.getTime();
        }

        //convert milliseconds to hours and minutes
        long hours = TimeUnit.HOURS.convert(millis, TimeUnit.HOURS);
        long minutes = TimeUnit.MINUTES.convert(millis, TimeUnit.MILLISECONDS);

        //calculate duration, add to list
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
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%2.2f,%2.2f,%2.2f", firstName, lastName, streetAddress, city,
                state, zipCode, socialSecurityNumber, position, hourlyRate, vacationDaysBalance, sickDaysBalance);
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
     * @return totalWagesEarned The total wages earned for a particular pay period, pre tax and pre deduction
     *
     * @since 0.1
     */
    public double calculateWages() {
        for(Double hours : shiftTimes) {
            double normalWagesEarned;
            if(hours > STANDARD_HOURS) {
                double overtimeWagesEarned = (hours - STANDARD_HOURS) * overtimeRate;
                normalWagesEarned = STANDARD_HOURS * hourlyRate;
                totalWagesEarned = normalWagesEarned + overtimeWagesEarned;
                ytdTotal += totalWagesEarned;
            } else {
                normalWagesEarned = STANDARD_HOURS * hourlyRate;
                totalWagesEarned = normalWagesEarned;
                ytdTotal += totalWagesEarned;
            }
        }
        return totalWagesEarned;
    }//end calculateWages()


    /**
     * Accessor to get an Employee's First Name
     * @return firstName
     *
     * @since 0.2
     */
    public String getFirstName() {
        return firstName;
    }//end getFirstName()

    /**
     * Mutator to set an Employee's First Name
     * @param firstName an Employee's First Name
     *
     * @since 0.2.1
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }//end setFirstName()

    /**
     * Accessor to get an Employee's Last Name
     * @return lastName
     *
     * @since 0.2
     */
    public String getLastName() {
        return lastName;
    }//end getLastName()

    /**
     * Mutator to set an Employee's Last Name
     * @param lastName an Employee's Last Name
     *
     * @since 0.2.1
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }//end setLastName

    /**
     * Accessor to get an Employee's Address
     * @return streetAddress
     *
     * @since 0.2
     */
    public String getStreetAddress() {
        return streetAddress;
    }//end getStreetAddress()

    /**
     * Mutator to set an Employee's Street Address
     * @param streetAddress an Employee's Street Address
     *
     * @since 0.2.1
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }//end setStreetAddress()

    /**
     * Accessor to get an Employee's City
     * @return city
     *
     * @since 0.2
     */
    public String getCity() {
        return city;
    }//end getCity()

    /**
     * Mutator to set an Employee's city
     * @param city an Employee's city
     *
     * @since 0.2.1
     */
    public void setCity(String city) {
        this.city = city;
    }//end setCity()

    /**
     * Accessor to get an Employee's State
     * @return state
     *
     * @since 0.2
     */
    public String getState() {
        return state;
    }//end getState()

    /**
     * Mutator to set an Employee's state
     * @param state an Employee's state
     *
     * @since 0.2.1
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Accessor to get an Employee's ZIP Code
     * @return zipCode
     *
     * @since 0.2
     */
    public String getZipCode() {
        return zipCode;
    }//end getZipCode()

    /**
     * Mutator to set an Employee's ZIP Code
     * @param zipCode an Employee's ZIP Code
     *
     * @since 0.2.1
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }//end setZipCode()

    /**
     * Accessor to get an Employee's Social Security Number
     * @return socialSecurityNumber
     *
     * @since 0.2
     */
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }//end getSocialSecurityNumber()

    /**
     * Mutator to set an Employee's Social Security Number
     * @param socialSecurityNumber an Employee's Social Security Number
     *
     * @since 0.2.1
     */
    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }//end setSocialSecurityNumber()

    /**
     * Accessor to get an Employee's Position
     * @return position
     *
     * @since 0.2
     */
    public String getPosition() {
        return position;
    }//end getPosition()

    /**
     * Mutator to set an Employee's Position
     * @param position an Employee's Position
     *
     * @since 0.2.1
     */
    public void setPosition(String position) {
        this.position = position;
    }//end setPosition()

    /**
     * Accessor to get an Employee's Vacation Day Balance
     * @return vacationDaysBalance
     *
     * @since 0.2
     */
    public double getVacationDaysBalance() {
        return vacationDaysBalance;
    }//end getVacationDaysBalance()

    /**
     * Mutator to adjust an Employee's Vacation Day Balance
     * @param vacationDaysBalance an Employee's Vacation Day Balance
     *
     * @since 0.2.1
     */
    public void setVacationDaysBalance(double vacationDaysBalance) {
        this.vacationDaysBalance = vacationDaysBalance;
    }//end setVacationDaysBalance()

    /**
     * Accessor to get an Employee's Sick Day Balance
     * @return sickDaysBalance
     *
     * @since 0.2
     */
    public double getSickDaysBalance() {
        return sickDaysBalance;
    }//end getSickDaysBalance()

    /**
     * Mutator to adjust an Employee's Sick Day Balance
     * @param sickDaysBalance an Employee's Sick Day Balance
     *
     * @since 0.2.1
     */
    public void setSickDaysBalance(double sickDaysBalance) {
        this.sickDaysBalance = sickDaysBalance;
    }//end setSickDaysBalance()

    /**
     * Accessor to get an Employee's Hourly Rate
     * @return hourlyRate
     *
     * @since 0.2
     */
    public double getHourlyRate() {
        return hourlyRate;
    }//end getHourlyRate()

    /**
     * Mutator to set an Employee's Hourly Rate
     * @param hourlyRate An Employee's hourly rate
     *
     * @since 0.2.1
     */
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }//end setHourlyRate()

    /**
     * Accessor to get an Employee's Overtime Rate
     * @return overtimeRate
     *
     * @since 0.2
     */
    public double getOvertimeRate() {
        return overtimeRate;
    }//end getOvertimeRate()

    /**
     * Accessor to get an Employee's Year-to-Date total
     * @return ytdTotal
     *
     * @since 0.2
     */
    public double getYtdTotal() {
        return ytdTotal;
    }//end getYtdTotal()

    /**
     * Accessor to get an Employee's Year-to-Date total
     * @return clockedIn Whether the employee is clockedIn or not
     *
     * @since 0.4
     */
    public boolean getClockedIn() {
        return clockedIn;
    }//end getClockedIn()

    /**
     * Accessor to get an Employee's Year-to-Date total
     * @param clockedIn
     *
     * @since 0.4
     */
    public void setClockedIn(boolean clockedIn) {
        this.clockedIn = clockedIn;
        employeeData.put("clockedIn", clockedIn);
    }//end setClockedIn()

    public void setEmployeeData(JSONObject employeeData){
        this.employeeData = employeeData;
    }

    public String getEmployeeData(){
        return employeeData.toJSONString();
    }

    public JSONArray getClockTimes(){
        return (JSONArray) employeeData.get("punches");
    }

    public void addClockTime(JSONObject jsa){
        employeeData.put("punches", jsa);
    }

    public void setPunches(JSONArray jsa){
        employeeData.put("punches", jsa);
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public double calculateForPeriod(long start, long end){
        double rate = (double) employeeData.get("wage");
        long duration = end - start;
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        double minutes = (double) (TimeUnit.MILLISECONDS.toMinutes(duration) % 60) /60;
        double total = (double) hours + (double) minutes;
        return total * rate;
    }

}//end Employee class

