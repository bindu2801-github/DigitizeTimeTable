package com.targetindia.DigitizeTimeTable.repository;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
@Repository
public class InstructorDao {
    Connection conn = null;

    static String url="jdbc:postgresql://localhost:5432/dtt";
    static String username="postgres";
    static String password="krishna";



    public  InstructorDao() {

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }


    public ResultSet getInstructorDayTimeTable(int instructor_id, String week){
        Statement statement;
        ResultSet rs=null;
        try{

            String query="select time_table.slot,class_table.class,class_table.section,course_table.location "+
                    "from time_table "+
                    "inner join course_table on time_table.course_id=course_table.course_id "+
                    "inner join class_table on time_table.class_id=class_table.class_id "+
                    "where time_table.instructor_id="+instructor_id+" and time_table.week='"+week+"'";
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
//            return rs;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return rs;
    }
    public ResultSet getInstructorWeeklyTimeTable(int instructor_id){
        Statement statement;
        ResultSet rs=null;
        try{

            String query="select time_table.week, time_table.slot,class_table.class,class_table.section,course_table.location "+
                    "from time_table "+
                    "inner join course_table on time_table.course_id=course_table.course_id "+
                    "inner join class_table on time_table.class_id=class_table.class_id "+
                    "where time_table.instructor_id="+instructor_id;
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return rs;
    }
}