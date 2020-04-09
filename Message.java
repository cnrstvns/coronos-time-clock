/**
 * @author Everett Simone
 *
 * Wraps a string in the Message class for Object transfer
 */
public class Message {
    private String msg;
    public Message(String _msg){
        msg = _msg;
    }
    public String toString(){
        return msg;
    }
}
