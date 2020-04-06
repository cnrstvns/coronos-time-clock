import java.io.Serializable;

public class CoronosAuth implements Serializable{

    private String username;
    private String password;
    private Boolean allow;
    private String reason = "";

    public CoronosAuth(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public Boolean getAllow(){
        return allow;
    }

    public String getReason(){
        return reason;
    }

    public void setAllow(Boolean allow){
        this.allow = allow;
    }

    public void setAllow(Boolean allow, String reason){
        this.allow = allow;
        this.reason = reason;
    }

    public String toString(){
        String returnData = String.format("Username: %s\nPassword: %s", username, password);
        return returnData;
    }
}