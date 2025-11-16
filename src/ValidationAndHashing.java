import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ValidationAndHashing {
    private UserDatabase users = new UserDatabase("users.txt");
    public boolean gmailValidation(String gmail){
            if(gmail == null || gmail.trim().isEmpty())
                return false;
        if(!Character.isLetterOrDigit(gmail.charAt(0)))
                return false;

        int i = 1;
        for(;i<gmail.length();i++){
            if(Character.isLetterOrDigit(gmail.charAt(i)))
                continue;
            else if(isAllowed(gmail.charAt(i))&&!isAllowed(gmail.charAt(i-1)))
                continue;
                else if(gmail.charAt(i) == '@')
                    break;
                else return false;
        }
        i++;
        try{
            if(!Character.isLetterOrDigit(gmail.charAt(i)))
                return false;

        }catch (Exception e){
            return false;
        }
        i++;
        for(;i<gmail.length();i++){
            if(Character.isLetterOrDigit(gmail.charAt(i)))
                continue;
            else if((gmail.charAt(i) == '.' && ! isAllowed(gmail.charAt(i-1))))
                continue;
                else if((gmail.charAt(i) == '-' && ! isAllowed(gmail.charAt(i-1))))
                    continue;
            else return false;
        }
        if(isAllowed(gmail.charAt(i-1)))
            return false;
        return true;

    }
    public boolean isAllowed(char s){
        return s == '.' || s == '_' || s == '-'|| s == '+';
    }
    public String gmailExist(String gmail){
        users.loadFromFile();
        ArrayList<User> users1 = users.getUsers();
        for(int i = 0;i<users1.size();i++){
            if(gmail.equalsIgnoreCase(users1.get(i).getEmail()))
                return users1.get(i).getId();
        }
        return null;
    }
    public boolean validID(String id,String type){
        if(id == null || id.trim().isEmpty())
            return false;
        if(type.equalsIgnoreCase("Student")) {
            if (id.charAt(0) != 's' && id.charAt(0) != 'S')
                return false;}
        else if (id.charAt(0) != 'i' && id.charAt(0) != 'I') {
                return false;}
        for (int i = 1; i < id.length(); i++) {
                if (!Character.isDigit(id.charAt(i)))
                    return false;
            }
            return true;
    }
    public boolean idExist(String id){
        users.loadFromFile();
        ArrayList<User> users1 = users.getUsers();
        for(int i = 0;i<users1.size();i++){
            if(users1.get(i).getId().equalsIgnoreCase(id))
                return true;
        }
        return false;
    }
    public boolean nameValidation(String name){
        if(name == null||name.trim().isEmpty())
            return false;
        for(int i = 0;i<name.length();i++)
            if(!Character.isLetter(name.charAt(i)))
                return false;
        return true;
    }
    public String passwordHashing(String pass) throws NoSuchAlgorithmException {
        pass = pass.trim();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] md1 = md.digest(pass.getBytes());
        BigInteger big = new BigInteger(1,md1);
        return big.toString(16);
    }
    public String passwordCheck(String password,String id) throws NoSuchAlgorithmException {
        password = password.trim();
        users.loadFromFile();
        ArrayList<User> users1 = users.getUsers();
        password = passwordHashing(password);
        int i;
        for(i = 0;i<users1.size();i++)
            if(users1.get(i).getId().equalsIgnoreCase(id))
                break;
        if(!users1.get(i).getHashPassword().equals(password))
            return null;
        return users1.get(i).name;
    }
}
