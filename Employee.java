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

    private int id;
    private JSONObject employeeData = new JSONObject();
    private boolean clockedIn;


    public Employee (JSONObject employeeData) {
        clockedIn = false;
        this.employeeData = employeeData;
    }//end constructor

    public boolean getClockedIn(){
        return clockedIn;
    }

    public void setClockedIn(boolean clockedIn) {
        this.clockedIn = clockedIn;
        employeeData.put("clockedIn", clockedIn);
    }//end setClockedIn()

    public void setEmployeeData(JSONObject employeeData){
        this.employeeData = employeeData;
    }

    public JSONObject getEmployeeData(){
        return (JSONObject) employeeData;
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

