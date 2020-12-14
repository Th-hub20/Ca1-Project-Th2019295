//*************BY Tiago Hubner*************//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//Main Screen of application. From here user can select for login or registration
public class MainScreen extends JFrame implements ActionListener{
    //Variables used
    private Container jPanel;
    private JLabel title;
    private JLabel slogan;
    private JSeparator sep ;   //Seperator
    private Color bg;   //Background color
    private Color c1;
    private JButton loginBtn;
    private JButton registerBtn; 
    
    //Constructor
    public MainScreen(){
        //Building GUI
        //Color scheme
        bg = new Color(213,204,255);
        c1 = new Color(170,153,255);
        
        //Setting Panel
        jPanel = getContentPane(); 
        jPanel.setLayout(null);
        jPanel.setBackground(bg);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(300,90,800,500);         
        
        title = new JLabel("Barber Shop");
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 30));
        title.setBounds(290,20,400,40);
        jPanel.add(title);
        
        slogan = new JLabel("All About Quality.");        
        slogan.setFont(new Font("Courier", Font.PLAIN, 12));
        slogan.setBounds(520,50,400,40);
        jPanel.add(slogan);
        
        sep = new JSeparator();
        sep.setOrientation(SwingConstants.HORIZONTAL);
        sep.setBackground(Color.BLACK);
        sep.setBounds(150,80,500,20);
        jPanel.add(sep);
        
        loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.PLAIN, 15)); 
        loginBtn.setBackground(c1);
        loginBtn.setBounds(320,200,150,40);
        loginBtn.addActionListener(this);       
        jPanel.add(loginBtn);
        
        registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Arial", Font.PLAIN, 15)); 
        registerBtn.setBackground(c1);
        registerBtn.setBounds(320,260,150,40);
        registerBtn.addActionListener(this);
        jPanel.add(registerBtn);
        setVisible(true); 
    }

    @Override
    public void actionPerformed(ActionEvent e) {    
        //If user has pressed login button then the screen will be redirected to 'Login Screen'
        if (e.getSource() == loginBtn) {
            LoginPage login = new LoginPage();
            this.dispose();
        }
        //If user has pressed register button then the screen will be redirected to 'Registration Screen'
        else if(e.getSource() == registerBtn) {
            RegistrationPage register = new RegistrationPage();
            this.dispose();
        }
    }
}
