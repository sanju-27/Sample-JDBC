import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Calendar.DAY_OF_YEAR;

public class Main {
    public static void main(String args[]) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/t1", "root", "");
//here sonoo is database name, root is username and password
//            Statement stmt=con.createStatement();
//            ResultSet rs=stmt.executeQuery("select * from tt");
//            while(rs.next())
//                System.out.println(rs.getInt(1)+"  "+rs.getString(2));
//            con.close();

            SimpleDateFormat dx = new SimpleDateFormat("dd-MM-yyyy");
            Calendar c = Calendar.getInstance();
            c.add(DAY_OF_YEAR, 1);
            java.util.Date da = c.getTime();
//                    String ada = dx.format(da);
            System.out.println(da);
            java.util.Date date = new java.util.Date();
            SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");

            Date sqlDate = new Date(date.getTime());
            String dateStr = d.format(date);
            System.out.println(dateStr);
            Timestamp sqlTime = new Timestamp(date.getTime());
            List<Integer> ls1 = new ArrayList<>();
            List<Integer> ls2 = new ArrayList<>();
            ls1.add(1);
            ls1.add(2);
            ls1.add(3);
            ls2.add(10);
            ls2.add(20);
            ls2.add(30);
            java.util.Date dxx = new java.util.Date();
//            dbWrite(dxx,ls1);
            getFromDb(dxx);
            dxx = c.getTime();
            getFromDb(dxx);
//            dbWrite(dxx,ls2);


        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static void dbWrite(java.util.Date dateD, List l) {
        String dbURL = "jdbc:mysql://localhost:3306/t1";
        String dbUser = "root";
        String dbPwd = "";
        String select = "SELECT * FROM test WHERE date=?";
        String insert = "INSERT INTO test(object,date) VALUES (?,?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbURL, dbUser, dbPwd);
//            SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date sqlDate = new java.sql.Date(dateD.getTime());
            PreparedStatement ps = con.prepareStatement(insert, RETURN_GENERATED_KEYS);
            ps.setObject(1, l);
            ps.setDate(2, sqlDate);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int serialized_id = -1;
            if (rs.next()) {
                serialized_id = rs.getInt(1);
            }
            if (serialized_id > 0)
                System.out.println("Success!");
            else
                System.out.println("Fail  :(");
            rs.close();
            ps.close();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void getFromDb(java.util.Date date) {
        String dbURL = "jdbc:mysql://localhost:3306/t1";
        String dbUser = "root";
        String dbPwd = "";
        String select = "SELECT * FROM test WHERE date=?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbURL, dbUser, dbPwd);
            SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
            PreparedStatement ps = con.prepareStatement(select);
            String sqlDate = d.format(date);
            ps.setString(1, sqlDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                byte[] buf = rs.getBytes(2);
                ObjectInputStream objectIn = null;
                if (buf != null) {
                    objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
                }

                Object deSerializedObject = objectIn.readObject();
                List ls = (List) deSerializedObject;
                System.out.println(ls);

            }
            rs.close();
            ps.close();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
