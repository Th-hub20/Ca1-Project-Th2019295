//*************BY Tiago Hubner*************//

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;
import javax.swing.table.DefaultTableModel;

class ClientHomePage extends JFrame implements ActionListener{
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
    private JButton searchBarberBtn;
    private JButton viewBookingsBtn;
    private JButton complaintBtn;
    private JButton okBtn;
    private JButton searchBtn; 
    private JButton cancelBtn;
    private JButton logout;   
    private JTextField Confiramtion;
    private JTextField search;   
    private JTextArea barberComplaint;
    static JTable tableC;
    static DefaultTableModel modelC;
    String barberSeaRch= "";
    String barberLocation = "";
    String barberName = "";
    String comp = "";
    String availability = "";
    String time= "";
    String date = "";
    String bName = "";
    String[] columnNames = {"BARBER NAME", "AVAILABLE AT"};    
    String[] columnNamesOfAptmnt = {"BARBER NAME", "APPOINTMENT TIME", "APPOINTMENT DATE"};
    JFrame frame;
    
    //Constructor
    public ClientHomePage(String clientName){
        this.name = clientName;
       
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
        
        searchBarberBtn = new JButton("Search Barber");
        searchBarberBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
        searchBarberBtn.setBackground(Color.gray);
        searchBarberBtn.setForeground(Color.white);
        searchBarberBtn.setBounds(120,220,170,40);
        searchBarberBtn.addActionListener(this);
        jPanel.add(searchBarberBtn);
        viewBookingsBtn = new JButton("View Bookings");
        viewBookingsBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
        viewBookingsBtn.setBackground(Color.gray);
        viewBookingsBtn.setForeground(Color.white);
        viewBookingsBtn.setBounds(320,220,150,40);
        viewBookingsBtn.addActionListener(this);
        jPanel.add(viewBookingsBtn);
        complaintBtn = new JButton("Complaint");
        complaintBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
        complaintBtn.setBackground(Color.gray);
        complaintBtn.setForeground(Color.white);
        complaintBtn.setBounds(500,220,170,40);
        complaintBtn.addActionListener(this);
        jPanel.add(complaintBtn);
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
        boolean found = false;
        try {
            //Making Connection with database
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/BarberShopDB;create=true");  
            //Preparing statement to have all the appointments details of client(user)
            PreparedStatement st = conn.prepareStatement("SELECT * FROM APPOINTMENTS WHERE CLIENTNAME LIKE ?");                   
            st.setString(1, name);
            ResultSet rs;
            rs = st.executeQuery();
            
            //If user have pressed search barber            
            if (e.getSource() == searchBarberBtn) {
                    searchBarber();   //Then display GUI to search barber                
                }
            //And if user have pressed search button on that screen 'search barber screen'
            else if(e.getSource() == searchBtn){
                //Then search for the barber entered by user (search can be done either by barber name or location)
                barberSeaRch = search.getText().trim();
                //Extracting barber name and location matching the barber's name entered by client/user
                st = conn.prepareStatement("SELECT BARBERNAME,LOCATION FROM BARBERLOGIN WHERE BARBERNAME LIKE ?");
                st.setString(1, barberSeaRch); 
                //Storing barber name and location in result set
                rs = st.executeQuery();
                PreparedStatement st2;
                //This will check if the barber with specific name is present in the system/database or not!
                    while(rs.next()) {
                        found = true;   //Barber found. Set fount = true
                        //Get his name and location from result set
                        barberName = rs.getString(1);
                        barberLocation = rs.getString(2);                        
                    }
                    //If barber has not fount
                    //  i.e either there is no barber matching the name inputted by user
                    //  or the user have searched through barber's  location not name 
                    if(!found){ 
                        //Extracting barber name and location matching the barber's location entered by client/user
                        st = conn.prepareStatement("SELECT BARBERNAME,LOCATION FROM BARBERLOGIN WHERE LOCATION LIKE ?");
                        st.setString(1, barberSeaRch);
                        rs = st.executeQuery();
                        //This will check if the barber with specific location is present in the system/database or not!
                        while(rs.next()) {
                            found = true;   //Barber found. Set fount = true
                            //Get his name and location from result set
                            barberName = rs.getString(1);
                            barberLocation = rs.getString(2);                            
                        }
                    }
                    //If the barber is found then display his available slots
                    if(found){
                        frame.dispose();
                        displayFreeSlots();
                        st2 = conn.prepareStatement("SELECT AVAILABLE_AT FROM SLOTS WHERE BARBERNAME LIKE ?");
                        st2.setString(1, barberName);
                        ResultSet rs2 = st2.executeQuery();
                        while(rs2.next()) {
                        availability = rs2.getString(1);
                        modelC.addRow(new String[]{barberName,availability});                 
                    }                        
                    }
                    //If barber has not been found then upate client/user about that
                    if(!found)
                        JOptionPane.showMessageDialog(null,"No hair-dresser found for particular search!");
                }
            //If user have pressed view bookings
            else if(e.getSource() == viewBookingsBtn){                                
                displayAppointments();  //Display GUI to view bookings
                while(rs.next()) {
                   barberName = rs.getString(1);
                   time = rs.getTime(3).toString();
                   date = rs.getDate(4).toString();
                   modelC.addRow(new String[]{barberName, time, date});                 
                } 
            }
            //If user have pressed cancel on that screen 'view bookings screen'
            else if(e.getSource() == cancelBtn){               
                int i =0;
                bName = Confiramtion.getText().trim();
                //Then find the barber whose name is inputted by user
                while(rs.next()) {     
                    //If barber is found then set status of appointment as cancelled
                    if(rs.getString(1).equals(bName)){
                        st = conn.prepareStatement("UPDATE APPOINTMENTS SET APPOINTMENTSTATUS = 'CANCELLED' WHERE BARBERNAME LIKE ?");
                        st.setString(1, bName);
                        st.executeUpdate();  
                        i++;
                       break;
                    }
                }       
                //Upon successfull cancellation, update user about that
                if(i>0)
                     JOptionPane.showMessageDialog(null,"Appointment with " + bName +" marked as cancelled!");                       
                else{
                    //Prompt user if no barber found against the name entered by user
                   JOptionPane.showMessageDialog(null,"Barber not fount!");
                }  
            }
            //If user have pressed complaints
            else if(e.getSource() == complaintBtn){
                complaints();   //Display GUI to write complaint             
            }
            //If user have inputted all fields and press okay button 
            else if(e.getSource()== okBtn){
                barberSeaRch = search.getText().trim();
                comp = barberComplaint.getText().trim();
                //Then first find the barber of whom the client have written complaint about          
                st = conn.prepareStatement("SELECT * FROM BARBERLOGIN WHERE BARBERNAME LIKE ?");
                st.setString(1, barberSeaRch);                    
                rs = st.executeQuery();
                PreparedStatement st2;
                    while(rs.next()) {
                        found = true;                       
                    }
                    //If that barber is found then store the user complaint against barber in database
                    if(found){
                        st2 = conn.prepareStatement("UPDATE BARBERLOGIN SET COMPLAINTS = ? WHERE BARBERNAME LIKE ?");
                        st2.setString(1, comp);
                        st2.setString(2, barberSeaRch);
                        st2.executeUpdate();      
                        //And update user about successfull submission of complain
                        JOptionPane.showMessageDialog(null,"Complaint has been submitted!!");
                    }
                    //If no barber found then update user about that
                    if(!found)
                        JOptionPane.showMessageDialog(null,"No hair-dresser found for particular search!"); 
            }
            //If user have pressed logout then logs him out and go back to login screen
            else if(e.getSource() == logout){
                this.dispose();
                LoginPage newLogin = new LoginPage();
            }           
        }
        catch (SQLException ex) {
            Logger.getLogger(BarberHomePage.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    //GUI for search barber screen
    public void searchBarber(){
            frame = new JFrame("Search Barber/Hair-dressers");
            frame.setLayout(null);  
            frame.setBounds(400,200,400,300);
            frame.setVisible(true);
            
            l1 = new JLabel("Enter Location or Name of Barber to Search For");
            l1.setFont(new Font("Arial", Font.PLAIN, 12)); 
            l1.setBounds(70,50,300,30);
            frame.add(l1);
            
            search = new JTextField();
            search.setFont(new Font("Arial", Font.PLAIN, 15)); 
            search.setBounds(120,90,150,25);
            frame.add(search);
            
            searchBtn = new JButton("Search");
            searchBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
            searchBtn.setBackground(Color.gray);
            searchBtn.setForeground(Color.white);
            searchBtn.setBounds(145,160,100,30);
            searchBtn.addActionListener(this);
            frame.add(searchBtn); 
        } 
    
    //GUI for displaying free slots
    public void displayFreeSlots(){
        frame = new JFrame("Upcoming Appointments of " + name);
        frame.setLayout(null);  
        frame.setBounds(400,200,600,300); 
        frame.setVisible(true);

        modelC = new DefaultTableModel();
        modelC.setColumnIdentifiers(columnNames);
        tableC = new JTable();
        tableC.setModel(modelC);
        tableC.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableC.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(tableC);
        scroll.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(0,0,600,100);
        frame.add(scroll);        
    } 
    
    //GUI for displaying appointments
    public void displayAppointments(){
            frame = new JFrame("Upcoming Appointments of " + name);
            frame.setLayout(null);  
            frame.setBounds(400,200,600,300); 
            frame.setVisible(true);

            modelC = new DefaultTableModel();
            modelC.setColumnIdentifiers(columnNamesOfAptmnt);
            tableC = new JTable();
            tableC.setModel(modelC);
            tableC.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            tableC.setFillsViewportHeight(true);
            JScrollPane scroll = new JScrollPane(tableC);
            scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setBounds(0,0,600,100);
            frame.add(scroll);
            confirmationL = new JLabel("Enter barber name to cancel booking with:");
            confirmationL.setFont(new Font("Arial", Font.PLAIN, 12)); 
            confirmationL.setBounds(10,130,250,30);
            frame.add(confirmationL);
            Confiramtion = new JTextField();
            Confiramtion.setFont(new Font("Arial", Font.PLAIN, 15)); 
            Confiramtion.setBounds(260,130,150,25);
            frame.add(Confiramtion);
            cancelBtn = new JButton("Cancel Booking");
            cancelBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
            cancelBtn.setBackground(Color.gray);
            cancelBtn.setForeground(Color.white);
            cancelBtn.setBounds(270,170,130,30);
            cancelBtn.addActionListener(this);
            frame.add(cancelBtn);
        }
    
    //GUI for comlaints screen
    public void complaints(){
            frame = new JFrame("Search Barber/Hair-dressers");
            frame.setLayout(null);  
            frame.setBounds(400,200,500,400);
            frame.setVisible(true);
            
            l1 = new JLabel("Enter barber name: ");
            l1.setFont(new Font("Arial", Font.PLAIN, 12)); 
            l1.setBounds(30,50,300,30);
            frame.add(l1);
            l2 = new JLabel("Enter Your Complaint here:");
            l2.setFont(new Font("Arial", Font.PLAIN, 12)); 
            l2.setBounds(30,80,300,30);
            frame.add(l2);
            search = new JTextField();
            search.setFont(new Font("Arial", Font.PLAIN, 15)); 
            search.setBounds(190,50,200,25);
            frame.add(search);
            barberComplaint = new JTextArea();
            barberComplaint.setFont(new Font("Arial", Font.PLAIN, 15)); 
            barberComplaint.setBounds(190,80,200,150);
            frame.add(barberComplaint);
            okBtn = new JButton("Done");
            okBtn.setFont(new Font("Arial", Font.BOLD, 10)); 
            okBtn.setBackground(Color.gray);
            okBtn.setForeground(Color.white);
            okBtn.setBounds(240,260,100,30);
            okBtn.addActionListener(this);
            frame.add(okBtn);
    }
}
