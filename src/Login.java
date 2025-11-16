import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

public class Login extends JFrame {
    private JPanel p1;
    private JTextField textFieldGmail;
    private JTextField textFieldPassword;
    private JButton loginButton;
    private JButton backButton;
    private ValidationAndHashing v = new ValidationAndHashing();
    public Login() {
        setVisible(true);
        setSize(400,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Login Menu");
        setLocationRelativeTo(null);
        setContentPane(p1);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new WelcomeMenu();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            String gmail = textFieldGmail.getText();
                if(!v.gmailValidation(gmail)){
                    JOptionPane.showMessageDialog(null,"Invalid Email Format");
                    return;
                }
                String id = v.gmailExist(gmail);
                if(id == null){
                    JOptionPane.showMessageDialog(null,"Email doesnt Exist");
                    return;
                }
                String password = textFieldPassword.getText();
                if(password == null || password.trim().isEmpty()){
                    JOptionPane.showMessageDialog(null,"Please Enter Your Password");
                    return;
                }
                String name = null;
                try {
                    name = v.passwordCheck(password,id);
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }

                if(name == null){
                        JOptionPane.showMessageDialog(null,"Incorrect Password");
                    return;
                }
                if(id.charAt(0)=='S'){
                    JOptionPane.showMessageDialog(null,"Login Successful\nWelcome " + name);
                    setVisible(false);
                    //new StudentMenu();
                }
                else {
                    JOptionPane.showMessageDialog(null,"Login Successful\nWelcome " + name);
                    setVisible(false);
                    //new InstructorMenu();
                }


            }
        });
    }
}
