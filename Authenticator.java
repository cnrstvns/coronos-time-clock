public class Authenticator {

    private String username;
    private transient String password;

    public Authenticator(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String toString(){
        String returnData = String.format("Username: %s\nPassword: %s", username, password);
        return returnData;
    }

    //example run will not have main in final
	/*
    public static void main(String[] args){
        Authenticator auth = new Authenticator("connor", "SecurePa$$word");
        System.out.print(auth.toString());
    }
	*/
}