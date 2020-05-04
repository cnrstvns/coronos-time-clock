import java.io.Serializable;

/**
 * @author Everett Simone
 * @version 1.0
 * Revision Notes: Full Release
 *
 *
 * ISTE 121.01 CPS:ID2
 * Final Project
 *
 * Class description: The Message class wraps a String into a Message object for transmission.
 *
 *                    The duplication of this code without written consent of the author is strictly prohibited.
 *
 */
public class Message implements Serializable {
    //attribute
    private String msg;

    /**
     * Parameterized Constructor
     * @param _msg Takes in a Message object
     */
    public Message(String _msg){
        msg = _msg;
    }//end constructor

    /**
     * toString method
     * @return msg
     */
    public String toString(){
        return msg;
    }//end toString()
}//end class
