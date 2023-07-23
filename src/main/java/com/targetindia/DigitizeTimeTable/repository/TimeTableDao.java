package com.targetindia.DigitizeTimeTable.repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.targetindia.DigitizeTimeTable.DbSettings;
import com.targetindia.DigitizeTimeTable.model.Instructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
public class TimeTableDao  {

    Connection conn = null;

    static String url="jdbc:postgresql://localhost:5433/DigitizeTimeTable";
    static String username="postgres";
    static String password="postgres@123";



    public TimeTableDao() {

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

    public ResultSet findInstructorById(int id){
        ResultSet result;
        try {
            String query = "select * from instructor_table where instructor_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            result = statement.executeQuery();
            return result;
        }catch (SQLException e) {
            throw new RuntimeException(String.format("No instructor available"));
        }
    }
    public ResultSet findAllInstructors() {
        ResultSet rs;
        try {
            String query = "SELECT * FROM Instructor_table";
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("No instructor available"));
        }
    }


    public void deleteById(int id) {
        try {
            String query = "Delete from instructor_table where instructor_id = " + id;

            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        }
        catch (Exception e){
            throw new RuntimeException(String.format("No instructor with id = {%d} to delete", id));
        }

    }

    public void addInstructor(Instructor newInstr) {

        try {
            String query = "insert into instructor_table(instructor_id,instructor_name,contact) values(?,?,?)";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1,newInstr.getInstructor_id());
            statement.setLong(3,newInstr.getInstructor_contact());
            statement.setString(2,newInstr.getInstructor_name());
            statement.executeQuery();
        }
        catch(SQLException e){
            throw new RuntimeException("Insertion failed");
        }
    }
    public void addAllInstructors(List<Instructor> instructors){
        int count = instructors.size();

        for(int i=0; i<count; i++){
            addInstructor(instructors.get(i));
        }
    }

//    public void updateInstructor(Instructor instr){
//        try{
//
//            findInstructorById(instr.getInstructor_id());
//            String query = "update instructor_table set contact=?, instructor_name=? where instructor_id=?";
//            PreparedStatement preparedStmt = conn.prepareStatement(query);
//            preparedStmt.setLong(1, instr.getInstructor_contact());
//            preparedStmt.setString(2, instr.getInstructor_name());
//            preparedStmt.setInt(3, instr.getInstructor_id());
//            preparedStmt.executeUpdate();
//
//        }
//        catch(SQLException e) {
//            throw new RuntimeException("Update Failed!");
//        }
//    }

    // slot ||intsruct_name(instr table)||   location,course_name(course table)


    public ResultSet getStudentWeeklyTimeTable(int classId){
        Statement statement;
        ResultSet rs=null;
        try{
            String query="select time_table.week,time_table.slot,course_table.course_name,instructor_table.instructor_name,course_table.location "+
                    "from time_table "+
                    "inner join course_table on time_table.course_id=course_table.course_id "+
                    "inner join instructor_table on time_table.instructor_id=instructor_table.instructor_id "+
                    "where time_table.class_id="+classId;
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return rs;
    }

    public ResultSet getStudentDayTimeTable(int class_id, String day){
        Statement statement;
        ResultSet rs=null;
        try{
            String query="select time_table.slot,course_table.course_name,instructor_table.instructor_name,course_table.location "+
                    "from time_table "+
                    "inner join course_table on time_table.course_id=course_table.course_id "+
                    "inner join instructor_table on time_table.instructor_id=instructor_table.instructor_id "+
                    "where time_table.class_id="+class_id+" and time_table.week='"+day+"'";
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return rs;
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
    public void updateInstructor(int id, String name, long contact){
        Statement statement;
        try{
            String query="update instructor_table " +
                    "set instructor_name='"+name+"', contact="+contact+" " +
                    "where instructor_id="+id+" ";
            statement=conn.createStatement();
            statement.executeQuery(query);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void replace_instructor(Connection con,int id, String name, long contact){
        Statement statement;
        ResultSet rs=null;
        try{
            String query1="select instructor_name,contact " +
                    "from instructor_table " +
                    "where instructor_id="+id+" ";
            statement=con.createStatement();
            rs=statement.executeQuery(query1);
            rs.next();
            String n=rs.getString("instructor_name");
            long l=rs.getLong("contact");
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            String query2=String.format("insert into delete_table(instructor_id,instructor_name,contact,deleted_time) values ('%d','%s','%d','%tF')",id,n,l,currentTimestamp);
            statement.executeUpdate(query2);
            String query="update instructor_table " +
                    "set instructor_name='"+name+"', contact="+contact+" " +
                    "where instructor_id="+id+" ";
//            statement=con.createStatement();
            statement.executeUpdate(query);
////            return rs;
//            while(rs.next()){
//                System.out.println(rs.getString("class"));
//            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void updatePeriodInfo(int classId, String dow, String slot) {

    }
}


