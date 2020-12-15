//*************BY Willian Peter Marcal*************//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;
import javax.swing.table.DefaultTableModel;

//Class for user Barber's home page. 
public class BarberHomePage extends JFrame implements ActionListener {
    //Variables used
    private String name;
    private Container jPanel;
    private JLabel title;
    private JLabel confirmationL;
    private JLabel l1;
    private JLabel l2;
    private JSeparator sep ;   //Seperator
    private Color bg;   //Background color
    private Color c1;
    private Color c2;
    private JButton upComingApBtn;
    private JButton setAvailabilityBtn;
    private JButton setStatusBtn;
    private JButton confirmBtn;
    private JButton confirmBtn2; 
    private JButton confirmBtn3; 
    private JButton logout;   
    private JTextField clientConfiramtion;
    private JTextField setAvailability;
    private JTextField setUnAvailability;    
    static JTable table;
    static DefaultTableModel model;
    String clientName= "";
    String time= "";
    String date = "";
    String cName = "";
    String availability;
    String unavailability;
    String[] columnNames = {"CLIENT NAME", "APPOINTMENT TIME", "APPOINTMENT DATE"};    
    JFrame frame;
    
    public BarberHomePage(String barberName){
        this.name = barberName;
        
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
               
        title = new JLabel("Welcome " + name +"!!");
        title.setFont(new Font("Lucida Calligraphy", Font.BOLD, 30));
        title.setBounds(260,20,400,50);
        jPanel.add(title);
        
        sep = new JSeparator();
        sep.setOrientation(SwingConstants.HORIZONTAL);
        sep.setBackground(Color.BLACK);
        sep.setBounds(150,80,500,3);
        jPanel.add(sep);
        
        upComingApBtn = new JButton("Upcoming Appointments");
        upComingApBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
        upComingApBtn.setBackground(Color.gray);
        upComingApBtn.setForeground(Color.white);
        upComingApBtn.setBounds(120,220,170,40);
        upComingApBtn.addActionListener(this);
        jPanel.add(upComingApBtn);
        setAvailabilityBtn = new JButton("Set Availability");
        setAvailabilityBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
        setAvailabilityBtn.setBackground(Color.gray);
        setAvailabilityBtn.setForeground(Color.white);
        setAvailabilityBtn.setBounds(320,220,150,40);
        setAvailabilityBtn.addActionListener(this);
        jPanel.add(setAvailabilityBtn);
        setStatusBtn = new JButton("Set Status of Appointment");
        setStatusBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
        setStatusBtn.setBackground(Color.gray);
        setStatusBtn.setForeground(Color.white);
        setStatusBtn.setBounds(500,220,170,40);
        setStatusBtn.addActionListener(this);
        jPanel.add(setStatusBtn);
        logout = new JButton("Logout");
        logout.setFont(new Font("Arial", Font.PLAIN, 15)); 
        logout.setBackground(c2);
        logout.setBounds(320,320,150,40);
        logout.addActionListener(this);
        jPanel.add(logout);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        try {
            //Making connection with database
            Connection conn = DriverManager.getConnection("jdbc:mysql://apontejaj.com:3306/Willian_2019306?useSSL=false", "Willian_2019306", "2019306");  
            ResultSet rs;
            //Query to have all the appointment's data of barber(user)
            PreparedStatement st = conn.prepareStatement("SELECT * FROM APPOINTMENTS WHERE BARBERNAME LIKE ?");
            st.setString(1, name);
            rs = st.executeQuery();
            
            //If user has pressed Upcoming appointments
            if (e.getSource() == upComingApBtn) { 
                //Then display a JTable having Client name, time and date to the barber
                displayAppointments();
                while(rs.next()) {
                       clientName = rs.getString(2);                   
                       time = rs.getTime(3).toString();
                       date = rs.getDate(4).toString();
                       model.addRow(new String[]{clientName, time, date});                 
                    }            
            }
            //Barber can then enter the client name and confirm his appointments with the client
            else if(e.getSource() == confirmBtn){
                int i =0;
                //Update the database with barber inputs by checking if the client is there in the database or not
                cName = clientConfiramtion.getText().trim();
                while(rs.next()) {     
                    if(rs.getString(2).equals(cName)){
                        st = conn.prepareStatement("UPDATE APPOINTMENTS SET CONFIRMAPPOINTMENTS = true WHERE CLIENTNAME LIKE ?");
                        st.setString(1, cName);
                        st.executeUpdate();  
                        i++;
                       break;
                    }
                }
                if(i>0)
                    //Upon successfull confirmation update the barber about appointment is confirmed with client name entered by barber
                     JOptionPane.showMessageDialog(null,"Appointment confirmed with " + cName);                       
                else{
                    //If there is no any client matching the name entered by barber then update the barber about no client found
                   JOptionPane.showMessageDialog(null,"Client not fount!");
                }
            }
            //If user has pressed Set availability
            else if(e.getSource() == setAvailabilityBtn){
                setAvailablity(); //Then show GUI, to set availability                                
            }
            //When user/barber have inputted all the fields and pressed confirm on that screen (set availability screen)
            else if(e.getSource() == confirmBtn2){
                //Then update the database with barber inputs
                availability = setAvailability.getText().trim();
                unavailability = setUnAvailability.getText().trim();
                st = conn.prepareStatement("INSERT INTO SLOTS values(?, ?, ?)");
                st.setString(1, name);
                st.setString(2, availability);
                st.setString(3, unavailability);
                st.executeUpdate();
                //and prompt barber about successfull updation of availability
                JOptionPane.showMessageDialog(null,"Slots are updated successfully!");
                frame.dispose();
            }
            //If user has pressed set status of appointments
            else if(e.getSource() == setStatusBtn){               
                    setStatus(); //Display GUI, to set status
                while(rs.next()) {
                       //If user/barber have appointments with any client display them 
                       clientName = rs.getString(2);                   
                       time = rs.getTime(3).toString();
                       date = rs.getDate(4).toString();
                       model.addRow(new String[]{clientName, time, date});                 
                    }                              
            }
            //If user have pressed confirm on that screen (status of appointments screen)
            else if(e.getSource() == confirmBtn3){
                int i =0;
                //Then set the appointment as completed of barber's inputted client name 
                cName = clientConfiramtion.getText().trim();
                while(rs.next()) {     
                    if(rs.getString(2).equals(cName)){
                        st = conn.prepareStatement("UPDATE APPOINTMENTS SET APPOINTMENTSTATUS = 'COMPLETED' WHERE CLIENTNAME LIKE ?");
                        st.setString(1, cName);
                        st.executeUpdate();  
                        i++;
                       break;
                    }
                }                    
                if(i>0)
                    //Upon successfull updation, update the barber about appointment with client (name entered by barber) is marked completed 
                     JOptionPane.showMessageDialog(null,"Appointment with " + cName +" marked as completed!");                       
                else{
                    //If there is no any client matching the name entered by barber then update the barber about no client found
                   JOptionPane.showMessageDialog(null,"Client not fount!");
                }                
            }   
            //If user have pressed logout then logs him out and go back to login screen
            else if(e.getSource() == logout){
                this.dispose();
                LoginPage newLogin = new LoginPage();
            }
                                
        } catch (SQLException ex) {
            Logger.getLogger(BarberHomePage.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    //GUI to display appointments of baber with the clients
    public void displayAppointments(){
            frame = new JFrame("Upcoming Appointments of " + name);
            frame.setLayout(null);  
            frame.setBounds(400,200,600,300); 
            frame.setVisible(true);

            model = new DefaultTableModel();
            model.setColumnIdentifiers(columnNames);
            table = new JTable();
            table.setModel(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setBounds(0,0,600,100);
            frame.add(scroll);
            confirmationL = new JLabel("Enter client name to confirm appointment: ");
            confirmationL.setFont(new Font("Arial", Font.PLAIN, 12)); 
            confirmationL.setBounds(10,130,250,30);
            frame.add(confirmationL);
            clientConfiramtion = new JTextField();
            clientConfiramtion.setFont(new Font("Arial", Font.PLAIN, 15)); 
            clientConfiramtion.setBounds(240,130,150,25);
            frame.add(clientConfiramtion);
            confirmBtn = new JButton("Confirm");
            confirmBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
            confirmBtn.setBackground(Color.gray);
            confirmBtn.setForeground(Color.white);
            confirmBtn.setBounds(265,180,100,30);
            confirmBtn.addActionListener(this);
            frame.add(confirmBtn);
        }
    
    //GUI to display 'set Availability Screen'   
    public void setAvailablity(){
            frame = new JFrame("Set Available Slots");
            frame.setLayout(null);  
            frame.setBounds(400,200,400,300);
            frame.setVisible(true);
            
            l1 = new JLabel("Enter Available Slot:");
            l1.setFont(new Font("Arial", Font.PLAIN, 12)); 
            l1.setBounds(40,50,250,30);
            frame.add(l1);            
            l2 = new JLabel("Enter Unavailable Slot:");
            l2.setFont(new Font("Arial", Font.PLAIN, 12)); 
            l2.setBounds(40,90,250,30);
            frame.add(l2);
            
            setAvailability = new JTextField();
            setAvailability.setFont(new Font("Arial", Font.PLAIN, 15)); 
            setAvailability.setBounds(170,50,150,25);
            frame.add(setAvailability);
            setUnAvailability = new JTextField();
            setUnAvailability.setFont(new Font("Arial", Font.PLAIN, 15)); 
            setUnAvailability.setBounds(170,90,150,25);
            frame.add(setUnAvailability);
            
            confirmBtn2 = new JButton("Confirm");
            confirmBtn2.setFont(new Font("Arial", Font.BOLD, 10)); 
            confirmBtn2.setBackground(Color.gray);
            confirmBtn2.setForeground(Color.white);
            confirmBtn2.setBounds(200,160,100,30);
            confirmBtn2.addActionListener(this);
            frame.add(confirmBtn2);        
    }
    
    //GUI to display 'set status of appointment screen'
    public void setStatus(){
            frame = new JFrame("Set Status of Appointment");
            frame.setLayout(null);  
            frame.setBounds(400,200,600,300); 
            frame.setVisible(true);

            model = new DefaultTableModel();
            model.setColumnIdentifiers(columnNames);
            table = new JTable();
            table.setModel(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setBounds(0,0,600,100);
            frame.add(scroll);
            confirmationL = new JLabel("Enter client name to set appointment as completed: ");
            confirmationL.setFont(new Font("Arial", Font.PLAIN, 12)); 
            confirmationL.setBounds(10,130,300,30);
            frame.add(confirmationL);
            clientConfiramtion = new JTextField();
            clientConfiramtion.setFont(new Font("Arial", Font.PLAIN, 15)); 
            clientConfiramtion.setBounds(300,130,150,25);
            frame.add(clientConfiramtion);
            confirmBtn3 = new JButton("Confirm");
            confirmBtn3.setFont(new Font("Arial", Font.BOLD, 10)); 
            confirmBtn3.setBackground(Color.gray);
            confirmBtn3.setForeground(Color.white);
            confirmBtn3.setBounds(265,180,100,30);
            confirmBtn3.addActionListener(this);
            frame.add(confirmBtn3);       
    }
}
