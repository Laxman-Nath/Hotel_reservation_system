// Hotel reservation system in java
package hotel_reserv_system;
import  java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;

public class Hotel_Reserv_System {
 private static final String url ="jdbc:mysql://localhost:3306/hotel_db";
 private static final String username="root";
 private static final String password="siddha@123";
 

    public static void main(String[] args) throws ClassNotFoundException,SQLException {
      try{
          Class.forName("com.mysql.cj.jdbc.Driver");
      }
      catch(ClassNotFoundException e){
          System.out.println(e.getMessage());
      }
      try{
          Connection conn=DriverManager.getConnection(url, username, password);
          Statement stm=conn.createStatement();
      while(true){
          System.out.println("Hotel Reservation System:");
          Scanner sc=new Scanner(System.in);
          System.out.println("1.Reserve a room:");
          System.out.println("2.View reservation:");
          System.out.println("3.Get room number:");
          System.out.println("4.Update reservation:");
          System.out.println("5.Delete reservation:");
          System.out.println("0.Exit");
          System.out.println("Select an option:");
          int choice=sc.nextInt();
          switch(choice){
              case 1:
                  reserveRoom(conn,sc,stm);
                  break;
              case 2:
                  viewReservations(conn,sc,stm);
                  break;
              case 3:
                  getRoomNumber(conn,sc,stm);
                  break;
              case 4:
                  updateReservation(conn,sc,stm);
                  break;
              case 5:
                  deleteReservation(conn,sc,stm);
                  break;
              case 0:
                  exit();
                  sc.close();
                  return;
               default:
                   System.out.println("Invalid choice! Try again");
          }
      }
      }
      catch(SQLException e){
          System.out.println(e.getMessage());
    }
    catch(InterruptedException e){
        throw new RuntimeException();
    }
    }
    
    private static void reserveRoom(Connection conn,Scanner sc,Statement stm)
     throws SQLException{
        try{
            System.out.println("Enter guest name:");
            String gname=sc.next();
            sc.nextLine();
            System.out.println("Enter room number:");
            int rnumber=sc.nextInt();
            System.out.println("Enter contact number:");
            String contactNo=sc.next();
            
            String sql="INSERT INTO reservations (guest_name,room_number,contact"
                    + "_number) "+"VALUES('" +gname+ "'," +rnumber+ ",'" +contactNo+ "')";
                int affectedRows=stm.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Reservation successful");
                }
                else{
                    System.out.println("Reservation failed!");
                }
            
         }
                catch(SQLException e){
                   e.printStackTrace();
}

    }
    private static void viewReservations (Connection conn,Scanner sc,Statement stm)
    throws SQLException{
          String sql="SELECT * FROM reservations";
          ResultSet rs=stm.executeQuery(sql);
          System.out.println("Current reservations:");
          System.out.println("+------------------+--------------+--------------+-----------------+----------------------+");
          System.out.println("|  Reservation ID  | Guest        | Room number  | Contact number  |   Reservation Date   |");
          System.out.println("+------------------+--------------+--------------+-----------------+----------------------+");
          while(rs.next()){
              int id=rs.getInt(1);
              String gname=rs.getString("guest_name");
              int rno=rs.getInt(3);
              String contactno=rs.getString(4);
              String  rdate=rs.getTimestamp(5).toString();
              
              System.out.printf("| %-14d | %-15s|%-15d |%-16s|%-19s |\n",id,gname
              ,rno,contactno,rdate);
          }
          System.out.println("+------------------+--------------+--------------+-----------------+----------------------+");
    
        
    }
    

    private static void getRoomNumber(Connection conn,Scanner sc,Statement stm){  
        try{
        System.out.println("Enter reservation id:");
        int rid=sc.nextInt();
        System.out.println("Enter guest name:");
        String gname=sc.next();
        String sql="SELECT room_number FROM reservations "
                + " WHERE reservation_id = "+ rid + " AND guest_name ='"+ gname +"'";
        ResultSet rs=stm.executeQuery(sql);
        if(rs.next()){
            int rno=rs.getInt("room_number");
            System.out.println("Room number for Reservation Id "+rid+" and guest name "+
                    gname +" is"+rno);
        }
        else{
            System.out.println("Reservation not found for the given Id and guest name:");
        }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
        
        private static void updateReservation(Connection conn,Scanner sc,Statement stm){
           
            try{
                System.out.println("Enter reservation id to update:");
            int rid=sc.nextInt();
            
            if(!reservationExist(conn,rid,stm)){
                System.out.println("Reservation not found for the given ID");
                return ;
            }
            
              System.out.println("Enter new guest name:");
            String gname=sc.next();
            sc.nextLine();
            System.out.println("Enter new room number:");
            int rnumber=sc.nextInt();
            System.out.println("Enter new contact number:");
            String contactNo=sc.next();
            
            String sql="UPDATE reservations SET guest_name= '" + gname +"',"+
           "room_number = "+rnumber+",contact_number='" +contactNo+ "'"+" WHERE reservation_id = " + rid;
            int affectedRows=stm.executeUpdate(sql);
            if(affectedRows>0){
                System.out.println("Reservation is updated successfully!");
            }
            else{
                System.out.println("Failed to upadate reservation!");
            }
        }
       catch(SQLException e){
        e.printStackTrace();
    }
    }
   private static void deleteReservation(Connection conn,Scanner sc,Statement stm){
       try{
       System.out.println("Enter the reservation id to delete:");
       int rid=sc.nextInt();
      if(!reservationExist(conn,rid,stm)){
                System.out.println("Reservation not found for the given ID");
                return ;
            }
      String sql="DELETE FROM reservations WHERE reservation_id= "+rid;
      int affectedRows=stm.executeUpdate(sql);
      if(affectedRows>0){
                System.out.println("Reservation is deleted successfully!");
            }
            else{
                System.out.println("Failed to delete reservation!");
            }
      }
    catch(SQLException e){
      e.printStackTrace();
    }
   }
   
  private static boolean reservationExist(Connection conn,int rid,Statement stm){
//      boolean isExist=false;
      try{
          String sql="SELECT reservation_id FROM reservations WHERE reservation_id="+rid;
          ResultSet rs=stm.executeQuery(sql);
          
          return rs.next();
      }
      catch(SQLException e){
          e.printStackTrace();
          return false;
      }
//      return isExist;
  }
  
    public static void exit() throws InterruptedException{
        System.out.println("Exiting System");
        int i=5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using hotel reservation system");
    }

}