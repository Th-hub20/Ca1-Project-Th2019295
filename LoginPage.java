//*************BY Tiago Hubner*************//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;

//Class for user login
class LoginPage extends JFrame implements ActionListener{
    //Variables used
    private Container jPanel;
    private JLabel title;
    private JSeparator sep ;   //Seperator
    private Color bg;   //Background color
    private Color c1;
    private Color c2;
    private JLabel emailL;
    private JTextField email;
    private JLabel passwordL;
    private JPasswordField password;
    private JRadioButton barber;
    private JRadioButton client;
    private ButtonGroup rBtnGrp;
    private JButton loginBtn;
    private JButton goForReg;
    String regexMail = "^(.+)@(.+)$";
    BarberHomePage b;
    ClientHomePage c;
    
    //Constructor
    public LoginPage(){
        //GUI building
        bg = new Color(213,204,255);
        c1 = new Color(234,230,255);
        c2 = new Color(170,153,255);
        
        //Setting Panel
        jPanel = getContentPane(); 
        jPanel.setLayout(null);
        jPanel.setBackground(bg);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(300,90,800,500); 
               
        title = new JLabel("Login");
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 30));
        title.setBounds(350,20,400,50);
        jPanel.add(title);
        
        sep = new JSeparator();
        sep.setOrientation(SwingConstants.HORIZONTAL);
        sep.setBackground(Color.BLACK);
        sep.setBounds(150,80,500,3);
        jPanel.add(sep);
        
        emailL = new JLabel("Email");
        emailL.setFont(new Font("Arial", Font.PLAIN, 15)); 
        emailL.setBounds(220,170,150,30);
        jPanel.add(emailL);
        email = new JTextField();
        email.setFont(new Font("Arial", Font.PLAIN, 15)); 
        email.setBackground(c1);
        email.setBounds(300,170,190,30);
        jPanel.add(email);
        
        passwordL = new JLabel("Password");
        passwordL.setFont(new Font("Arial", Font.PLAIN, 15)); 
        passwordL.setBounds(220,220,150,30);
        jPanel.add(passwordL);
        password = new JPasswordField(); 
        password.setFont(new Font("Arial", Font.PLAIN, 15));
        password.setBackground(c1);
        password.setBounds(300,220,190,30);
        jPanel.add(password);
        
        client = new JRadioButton("Client"); 
        client.setFont(new Font("Arial", Font.PLAIN, 12)); 
        client.setBackground(bg);
        client.setSelected(true);      
        client.setBounds(300,280,60,20);
        jPanel.add(client);
        
        barber = new JRadioButton("Hair-Dresser/Barber"); 
        barber.setFont(new Font("Arial", Font.PLAIN, 12)); 
        barber.setBackground(bg);
        barber.setSelected(false); 
        barber.setBounds(360,280,130,20);
        jPanel.add(barber); 
        
        rBtnGrp = new ButtonGroup();
        rBtnGrp.add(client);
        rBtnGrp.add(barber);
                
        loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.PLAIN, 15)); 
        loginBtn.setBackground(c2);
        loginBtn.setBounds(320,320,150,40);
        loginBtn.addActionListener(this);
        jPanel.add(loginBtn);
        
        goForReg = new JButton("New User? Click to Register");
        goForReg.setFont(new Font("Arial", Font.PLAIN, 11)); 
        goForReg.setBackground(bg);
        goForReg.setForeground(Color.BLUE);
        goForReg.setBorder(BorderFactory.createEmptyBorder());
        goForReg.setBounds(310,410,170,15);
        goForReg.addActionListener(this);
        jPanel.add(goForReg);
        
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String mail = email.getText();
        String pass = password.getText();
        
        //If user has pressed login button
        if (e.getSource() == loginBtn) {           
                
        try {
            //Making connection with database
            boolean flag = false;
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/BarberShopDB;create=true");
            
            Statement st = conn.createStatement();            
            ResultSet rs;
            //Preparing statement according to the the i.e Client or Barber
            if(barber.isSelected()) //If user have selected barber
                rs = st.executeQuery("SELECT * FROM BARBERLOGIN");  //Then add data of barber in result set
            else
                rs = st.executeQuery("SELECT * FROM CLIENTLOGIN");  //Otherwise add data of client in result set
            
            //Checks if the user is a registered user nor not
            while(rs.next()){
               //Match the mail and password of registered user
               if(rs.getString(3).equals(mail) && rs.getString(4).equals(pass)){
                    flag = true;
                    break;
                }
            }
            if(flag && barber.isSelected()){
                //If user is registered as barber then go to the Barber home page and pass barber name to the class
                b = new BarberHomePage(rs.getString(1));    
                this.dispose();     //Close this window (MainScreen)
            }
            else if(flag && client.isSelected()){
                //If user is registered as client then go to the Client home page and pass client name to the class
                c = new ClientHomePage(rs.getString(1));    
                this.dispose(); //Close this window (MainScreen)
            }
            else
                JOptionPane.showMessageDialog(null,"Invalid user!");    //Otherwise propmt the user that he/she 'invalid user'
                  
        } catch (SQLException ex) {
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //If user has pressed 'New User? Click to Register'
        else if(e.getSource() == goForReg){
            RegistrationPage registration = new RegistrationPage(); //Take the user to Registration Screen
            this.dispose();
        }
    }
    
}
