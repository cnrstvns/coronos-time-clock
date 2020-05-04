import java.io.Serializable;
/**
 * @author Connor Stevens - GUI
 * @version 1.0
 * Revision Notes: Full Release
 *
 *
 * ISTE 121.01 CPS:ID2
 * Final Project
 *
 * Class description: The CoronosAuth class allows for an interface to verify a user's login.
 *
 *                    The duplication of this code without written consent of the authors is strictly prohibited.
 *
 */

public class CoronosAuth implements Serializable{
    //attributes
    private String username;
    private String password;
    private Boolean allow;
    private String reason = "";

    /**
     * Parameterized Constructor
     * @param username A user's username
     * @param password A user's password
     */
    public CoronosAuth(String username, String password){
        this.username = username;
        this.password = password;
    }//end constructor

    /**
     * Accessor for username
     * @return username
     */
    public String getUsername(){
        return username;
    }//end getUsername

    /**
     * Accessor for password
     * @return password
     */
    public String getPassword(){
        return password;
    }//end getPassword

    /**
     * Accessor to check whether a person can log in
     * @return allow
     */
    public Boolean getAllow(){
        return allow;
    }//end getAllow()

    /**
     * Accessor to get login reason
     * @return reason
     */
    public String getReason(){
        return reason;
    }//end getAllow()

    /**
     * Mutator to set whether a person can log in
     * @param allow Boolean
     */
    public void setAllow(Boolean allow){
        this.allow = allow;
    }//end setAllow()

    /**
     * Mutator to set whether a person can log in
     * @param allow Boolean
     * @param reason The reason why they can't login
     */
    public void setAllow(Boolean allow, String reason){
        this.allow = allow;
        this.reason = reason;
    }//end setAllow()

    /**
     * toString method to return formatted data
     * @return returnData
     */
    public String toString(){
        String returnData = String.format("Username: %s\nPassword: %s", username, password);
        return returnData;
    }//end toString();
}//end class