/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amateam.crud;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */

// Class that's responsible for manipulating data in the database
public class Database {
    
    // Connection global variable
    // This variables is can access everywhere in this class 
    Connection con;
    Statement statement;
    
    public Database () {
        
        // try catch to catch the error and the program is not terminate if erro's happend
        try {
            
            // Connection info that's need of the postgresql
            // host:                                   port database 
            String host = System.getenv("DB_HOST");
            
            // Database username
            String user = System.getenv("DB_USER");
            
            // Database password
            String password = System.getenv("DB_PASSWORD");           
            
            // Connect to the database
            con = DriverManager.getConnection(host, user, password);
            System.out.println("Successfully Connected to the Database.");
            
            // Create statement to query in the database
            statement = con.createStatement();
            
            // Create students table if is not exist in the database
            String query = "CREATE TABLE IF NOT EXISTS students("
                    + "id SERIAL PRIMARY KEY,"
                    + "first_name VARCHAR(255) NOT NULL,"
                    + "middle_name VARCHAR(255) DEFAULT '',"
                    + "last_name VARCHAR(255) NOT NULL,"
                    + "birthday_month VARCHAR(255) NOT NULL,"
                    + "birthday_day VARCHAR(255) NOT NULL,"
                    + "birthday_year VARCHAR(255) NOT NULL,"
                    + "gender VARCHAR(255) NOT NULL,"
                    + "contact_number VARCHAR(255) NOT NULL,"
                    + "email_address VARCHAR(255) NOT NULL,"
                    + "street_address VARCHAR(255) NOT NULL,"
                    + "barangay VARCHAR(255) NOT NULL,"
                    + "city VARCHAR(255) NOT NULL,"
                    + "province VARCHAR(255) NOT NULL,"
                    + "zip_code VARCHAR(255) NOT NULL,"
                    + "course_preference TEXT[] NOT NULL"
                    + ")";
            statement.executeUpdate(query);
            System.out.println("Successfully Created an students Table to the Database.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // CRUD Read: this function is read or get all the data in the students table
    public void Read() {
        try {
            
            // Query to get all the data in students table
            String query = "SELECT id, first_name, middle_name, last_name, email_address, barangay, city, province FROM public.students";
            ResultSet rs = statement.executeQuery(query);
            
            // Reset the table 
            Main.TableModel.setRowCount(0);
            
            // Display the data in the Table
            while (rs.next()) {
                
                int id = rs.getInt("id");
                String fullname;
                if (rs.getString("middle_name").equals("")) {
                    fullname = rs.getString("first_name") + " " + rs.getString("last_name");
                } else {
                    fullname = rs.getString("first_name") + " " + rs.getString("middle_name") + " " + rs.getString("last_name");
                }
                String address = rs.getString("barangay") + ", " + rs.getString("city") + ", " + rs.getString("province");
                String email =  rs.getString("email_address");
                
                Object[] row = {
                    id,
                    fullname,
                    email,
                    address
                };
                
                Main.TableModel.addRow(row);
                System.out.println("Successfully read the data of students table.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void Read(String _id) {
        try {
            
            // Query to get all the data in students table
            String query = "SELECT * FROM public.students WHERE id = " + _id + ";";
            ResultSet rs = statement.executeQuery(query);
            
            if (rs.next()) {
                // Display the data in the Form
                Form form = new Form(_id, true);
                form.TextFieldFirstName.setText(rs.getString("first_name"));
                form.TextFieldMiddleName.setText(rs.getString("middle_name"));
                form.TextFieldLastName.setText(rs.getString("last_name"));
                form.ComboBoxMonth.setSelectedItem(rs.getString("birthday_month"));
                form.ComboBoxDay.setSelectedItem(rs.getString("birthday_day"));
                form.ComboBoxYear.setSelectedItem(rs.getString("birthday_year"));
                if (rs.getString("gender").equalsIgnoreCase("male")) {
                    form.RadioButtonMale.setSelected(true);
                } else {
                    form.RadioButtonFemale.setSelected(true);
                }
                form.TextFieldContactNumber.setText(rs.getString("contact_number"));
                form.TextFieldEmailAddress.setText(rs.getString("email_address"));
                form.TextFieldStreetAddress.setText(rs.getString("street_address"));
                form.TextFieldBarangay.setText(rs.getString("barangay"));
                form.TextFieldCity.setText(rs.getString("city"));
                form.TextFieldProvince.setText(rs.getString("province"));
                form.TextFieldZipCode.setText(rs.getString("zip_code"));
                JCheckBox[] coursesCheckbox = {
                    form.jCheckBox1,
                    form.jCheckBox2,
                    form.jCheckBox3,
                    form.jCheckBox4,
                    form.jCheckBox5,
                    form.jCheckBox6,
                    form.jCheckBox7,
                    form.jCheckBox8,
                    form.jCheckBox9,
                    form.jCheckBox10,
                };
                // SQL Array
                Array sqlCoursesArray = rs.getArray("course_preference");
                // Convert the SQL Array into Java Array
                String[] stringCoursesArray = (String[]) sqlCoursesArray.getArray();

                // Check the course preference of the student
                for (String stringCourseArray : stringCoursesArray) {
                    for (JCheckBox courseCheckbox : coursesCheckbox) {
                        if (courseCheckbox.getText().equalsIgnoreCase(stringCourseArray)) {
                            courseCheckbox.setSelected(true);
                        }
                    }
                }
                form.setVisible(true);
                System.out.println("Successfully read the data of student with id" + _id + ".");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
     public void Create(Student student) {
        try {
            
            // Query to insert data in the students table
            String query = "INSERT INTO public.students(\n" +
                            "first_name, middle_name, last_name, birthday_month, birthday_day, birthday_year, gender, contact_number, email_address, street_address, barangay, city, province, zip_code, course_preference)\n" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement newStatement = con.prepareStatement(query);
            
            // Set all the data
            newStatement.setString(1, student.first_name);
            newStatement.setString(2, student.middle_name);
            newStatement.setString(3, student.last_name);
            newStatement.setString(4, student.birthday_month);
            newStatement.setString(5, student.birthday_day);
            newStatement.setString(6, student.birthday_year);
            newStatement.setString(7, student.gender);
            newStatement.setString(8, student.contact_number);
            newStatement.setString(9, student.email_address);
            newStatement.setString(10, student.street_address);
            newStatement.setString(11, student.barangay);
            newStatement.setString(12, student.city);
            newStatement.setString(13, student.province);
            newStatement.setString(14, student.zip_code);
            Array sqlCoursesArray = con.createArrayOf("text", student.course_preference.toArray());
            newStatement.setArray(15, sqlCoursesArray);
            
            int rowInserted = newStatement.executeUpdate();
            if (rowInserted > 0) {
                JOptionPane pane = new JOptionPane("Record Successfully Added.", JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = pane.createDialog("Success");
                dialog.setAlwaysOnTop(true);
                dialog.setVisible(true);
                Main main = new Main();
                main.setVisible(true);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     
     public void Update(String _id, Student student) {
        try {
            
            // Query to insert data in the students table
            String query = "UPDATE public.students\n" +
                            "SET first_name=?, middle_name=?, last_name=?, birthday_month=?, birthday_day=?, birthday_year=?, gender=?, contact_number=?, email_address=?, street_address=?, barangay=?, city=?, province=?, zip_code=?, course_preference=?\n" +
                            "WHERE id = " + _id + ";";
            PreparedStatement newStatement = con.prepareStatement(query);
            
            // Set all the data
            newStatement.setString(1, student.first_name);
            newStatement.setString(2, student.middle_name);
            newStatement.setString(3, student.last_name);
            newStatement.setString(4, student.birthday_month);
            newStatement.setString(5, student.birthday_day);
            newStatement.setString(6, student.birthday_year);
            newStatement.setString(7, student.gender);
            newStatement.setString(8, student.contact_number);
            newStatement.setString(9, student.email_address);
            newStatement.setString(10, student.street_address);
            newStatement.setString(11, student.barangay);
            newStatement.setString(12, student.city);
            newStatement.setString(13, student.province);
            newStatement.setString(14, student.zip_code);
            Array sqlCoursesArray = con.createArrayOf("text", student.course_preference.toArray());
            newStatement.setArray(15, sqlCoursesArray);
            
            int rowInserted = newStatement.executeUpdate();
            if (rowInserted > 0) {
                JOptionPane pane = new JOptionPane("Record Successfully Updated.", JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = pane.createDialog("Success");
                dialog.setAlwaysOnTop(true);
                dialog.setVisible(true);
                Main main = new Main();
                main.setVisible(true);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     
     public void Delete(String _id) {
        try {
            
            // Query to delete data in the students table
            String query = "DELETE FROM public.students\n" +
                            "WHERE id = " + _id + ";";
            statement.executeUpdate(query);
            
            JOptionPane pane = new JOptionPane("Record Successfully Deleted.", JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = pane.createDialog("Success");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
