import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author Dalton Kruppenbacher
 * @version 1.0
 * Revision Notes: Full Release
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
    //attributes
    private int id;
    private JSONObject employeeData = new JSONObject();
    private boolean clockedIn;

    /**
     * Parameterized Constructor
     * @param employeeData An Employee's data
     */
    public Employee (JSONObject employeeData) {
        clockedIn = false;
        this.employeeData = employeeData;
    }//end constructor

    /**
     * Method to return an employee's clockedIn status
     * @return clockedIn
     */
    public boolean getClockedIn(){
        return clockedIn;
    }//end getClockedIn()

    /**
     * Method to set an employee's clockedIn status
     * @param clockedIn Boolean
     */
    public void setClockedIn(boolean clockedIn) {
        this.clockedIn = clockedIn;
        employeeData.put("clockedIn", clockedIn);
    }//end setClockedIn()

    /**
     * Method to set an employee's Data
     * @param employeeData an employee's Data
     */
    public void setEmployeeData(JSONObject employeeData){
        this.employeeData = employeeData;
    }//end setEmployeeData()

    /**
     * Method to get an employee object
     * @return employeeData
     */
    public JSONObject getEmployeeData(){
        return (JSONObject) employeeData;
    }//end getEmployeeData()

    /**
     * Method to get Clock In Times
     * @return clock in times
     */
    public JSONArray getClockTimes(){
        return (JSONArray) employeeData.get("punches");
    }//end getClockTimes()

    /**
     * Method to add a clock in/out time
     * @param jsa Object to add the time to
     */
    public void addClockTime(JSONObject jsa){
        employeeData.put("punches", jsa);
    }//end addClockTime()

    /**
     * Method to set a punch time
     * @param jsa Object to add the time to
     */
    public void setPunches(JSONArray jsa){
        employeeData.put("punches", jsa);
    }//end setPunches()

    /**
     * Method to get ID
     * @return id
     */
    public int getId(){
        return this.id;
    }//end getId()

    /**
     * Method to set an ID
     * @param id an ID
     */
    public void setId(int id){
        this.id = id;
    }//end setId()

    /**
     * Method to calculate pay earned
     * @param start Start time
     * @param end End time
     * @return pay earned
     */
    public double calculateForPeriod(long start, long end){
        double rate = (double) employeeData.get("wage");
        long duration = end - start;
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        double minutes = (double) (TimeUnit.MILLISECONDS.toMinutes(duration) % 60) /60;
        double total = (double) hours + (double) minutes;
        return total * rate;
    }//end calculateForPeriod()

}//end Employee class

