//*************BY Willian Peter Marcal*************//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//Class for user registration 
class RegistrationPage extends JFrame implements ActionListener{
    //Variables used
    private Container jPanel;
    private JLabel title;
    private JSeparator sep ;   //Seperator
    private Color bg;   //Background color
    private Color c1;
    private Color c2;
    
    private JLabel nameL;
    private JLabel phoneL;
    private JLabel emailL;
    private JLabel locationL;
    private JLabel msg;
    private JLabel passwordL;
    private JTextField name;
    private JTextField phone;
    private JTextField email;
    private JTextField location;
    private JPasswordField password;
    private JRadioButton barber;
    private JRadioButton client;
    private ButtonGroup rBtnGrp;
    private JButton register;
    String regexMail = "^(.+)@(.+)$";
    private String regexPhone = "^[0-9]*$";
    
    //Constructors
    public RegistrationPage(){
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
               
        title = new JLabel("Registration");
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 30));
        title.setBounds(290,20,300,50);
        jPanel.add(title);
        
        sep = new JSeparator();
        sep.setOrientation(SwingConstants.HORIZONTAL);
        sep.setBackground(Color.BLACK);
        sep.setBounds(150,80,500,3);
        jPanel.add(sep);
        
        nameL = new JLabel("Full Name");
        nameL.setFont(new Font("Arial", Font.BOLD, 15)); 
        nameL.setBounds(220,150,100,30);
        jPanel.add(nameL);
        phoneL= new JLabel("Phone Number"); 
        phoneL.setFont(new Font("Arial", Font.BOLD, 15)); 
        phoneL.setBounds(220,190,150,30);
        jPanel.add(phoneL);
        emailL = new JLabel("Email");
        emailL.setFont(new Font("Arial", Font.BOLD, 15));
        emailL.setBounds(220,230,100,30);
        jPanel.add(emailL);
        locationL = new JLabel("Location");
        locationL.setFont(new Font("Arial", Font.BOLD, 15));
        locationL.setBounds(220,270,100,30);
        jPanel.add(locationL);
        passwordL = new JLabel("Password");
        passwordL.setFont(new Font("Arial", Font.BOLD, 15));
        passwordL.setBounds(220,310,100,30);
        jPanel.add(passwordL);
           
        name = new JTextField(); 
        name.setFont(new Font("Arial", Font.PLAIN, 15)); 
        name.setBackground(c1);
        name.setBounds(350,150,210,25);        
        jPanel.add(name);       
        phone = new JTextField(); 
        phone.setFont(new Font("Arial", Font.PLAIN, 15)); 
        phone.setBackground(c1);
        phone.setBounds(350,190,210,25);
        jPanel.add(phone);        
        email = new JTextField(); 
        email.setFont(new Font("Arial", Font.PLAIN, 15)); 
        email.setBackground(c1);
        email.setBounds(350,230,210,25);
        jPanel.add(email);        
        location = new JTextField(); 
        location.setFont(new Font("Arial", Font.PLAIN, 15)); 
        location.setBackground(c1);
        location.setBounds(350,270,210,25);
        jPanel.add(location);       
        msg = new JLabel("This field is required for Hair-Dresser/Barber only!");
        msg.setFont(new Font("Arial", Font.PLAIN, 9));
        msg.setBounds(570,260,300,50);
        jPanel.add(msg);
        password = new JPasswordField(); 
        password.setFont(new Font("Arial", Font.PLAIN, 15)); 
        password.setBackground(c1);
        password.setBounds(350,310,210,25);
        jPanel.add(password);        
        client = new JRadioButton("Client"); 
        client.setFont(new Font("Arial", Font.PLAIN, 12)); 
        client.setBackground(bg);
        client.setSelected(true);      
        client.setBounds(360,350,60,20);
        jPanel.add(client);        
        barber = new JRadioButton("Hair-Dresser/Barber"); 
        barber.setFont(new Font("Arial", Font.PLAIN, 12)); 
        barber.setBackground(bg);
        barber.setSelected(false); 
        barber.setBounds(420,350,150,20);
        jPanel.add(barber);         
        rBtnGrp = new ButtonGroup(); 
        rBtnGrp.add(client); 
        rBtnGrp.add(barber);
             
        register = new JButton("Register");
        register.setFont(new Font("Arial", Font.PLAIN, 15)); 
        register.setBackground(c2);
        register.setBounds(380,390,150,40);
        register.addActionListener(this);
        jPanel.add(register);
        
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {        
        Connection conn;
        try {
            //Creating connection with database
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/BarberShopDB;create=true");    
            
        if (e.getSource() == register) { 
        
        String fullName; 
        String mobile = phone.getText().trim();
        String mail = email.getText().trim();
        String locat = location.getText().trim();
        String pass;
        //Validating the user iputs
        if(name.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"Please enter your name!");             
        }     
        else if(mobile.isEmpty() || !mobile.matches(regexPhone)){
            JOptionPane.showMessageDialog(null,"Please enter valid mobile number!");
        }
        else if(mail.isEmpty() || !mail.matches(regexMail)){
            JOptionPane.showMessageDialog(null,"Please enter valid email!");             
        }
        else if(locat.isEmpty() && barber.isSelected()){
            JOptionPane.showMessageDialog(null,"Please enter location!");
        }
        else if(password.getPassword().length == 0){
            JOptionPane.showMessageDialog(null,"Please enter password!");
        }        
        else{   //If user have entered all the inputs rightly then           
            fullName = name.getText().trim();
            pass = password.getText();
            //If user have selected barber/hair-dresser (i.e user want to register as barber)
            if(barber.isSelected()){
                //Then update the database of barbers with user inputs
                PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO BARBERLOGIN values(?, ?, ?, ?, ?, ?)");
                preparedStmt.setString(1, fullName);
                preparedStmt.setString(2, mobile);
                preparedStmt.setString(3, mail);
                preparedStmt.setString(4, pass);           
                preparedStmt.setString(5, locat);
                preparedStmt.setString(6, null);
             
            preparedStmt.executeUpdate();  
            }
            else if(client.isSelected()){   //If user have selected client (i.e user want to register as client)
                //Then update the database of clients with user inputs
                PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO CLIENTLOGIN values(?, ?, ?, ?)");
                preparedStmt.setString(1, fullName);
                preparedStmt.setString(2, mobile);
                preparedStmt.setString(3, mail);
                preparedStmt.setString(4, pass);

                preparedStmt.executeUpdate();  
            }
            //And prompt the user about successfull registration
            JOptionPane.showMessageDialog(null,"You are Registered Successfully"); 
            this.dispose(); //Close this window
            //Thake the user to Login Screen
            LoginPage login = new LoginPage();
            }
        }
        } catch (SQLException ex) {
            Logger.getLogger(RegistrationPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
