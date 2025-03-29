/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amateam.crud;

/**
 *
 * @author Admin
 */

import java.util.ArrayList;

public class Student {
    int id;
    String first_name;
    String middle_name;
    String last_name;
    String birthday_month;
    String birthday_day;
    String birthday_year;
    String gender;
    String contact_number;
    String email_address;
    String street_address;
    String barangay;
    String city;
    String province;
    String zip_code;
    ArrayList<String> course_preference = new ArrayList<>();
}
