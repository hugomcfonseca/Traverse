package feup.traverse;

import android.database.SQLException;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

/**
 * Created by Filipe on 07/01/2016.
 */
public class ExternalDB extends AsyncTask<String,Void,String> {
    public String username;
    public String table;
    public String column;
    private String result;
    private String operation;
    private String name;
    private String email;
    private  String date;
    private String persona;
    private String password;
    private Integer status;
    private double progress;

    public String SingleValue(String Username,String Table , String Column, String Operation) throws ExecutionException, InterruptedException {
        username=Username;
        column =Column;
        table = Table;
        operation= Operation;

        execute().get();
       return result;
    }
    public void insertuser(String Table,String Operation, String Username, String Name, String Email, String Date, String Persona, int Status, double Progress, String Password) throws ExecutionException, InterruptedException {
        table = Table;
        operation= Operation;
        username=Username;
        name=Name;
        email=Email;
        date=Date;
        persona=Persona;
        password=Password;
        status=Status;
        progress=Progress;
        //chapter_score=Chapter_score;
        execute().get();
    }
    public String verifydata(String Email, String Table,String Column, String Operation) throws ExecutionException, InterruptedException {
        email=Email;
        table = Table;
        operation= Operation;
        column=Column;

        execute().get();
        return result;
    }



    @Override
    protected String doInBackground(String... params) {
        String retval = "";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            retval = e.toString();
        }
        String url = "jdbc:postgresql://ec2-54-83-204-228.compute-1.amazonaws.com:5432/d31sdik2uip6li?" +
                "user=yghxnskjzqrpvp&password=EFOJeYOjNRTXXEBSbllNee1oqV&ssl=true" +
                "&sslfactory=org.postgresql.ssl.NonValidatingFactory";

        Connection conn;
        DriverManager.setLoginTimeout(5);
        if (operation=="SELECT") {
            try {
                conn = DriverManager.getConnection(url);
                Statement st = conn.createStatement();
                String sql;
                sql = "SELECT * FROM " + table + " WHERE '" + username + "'= username";
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    retval = rs.getString(column);
                }
                result = retval;
                rs.close();
                st.close();
                conn.close();
            } catch (java.sql.SQLException e1) {
                e1.printStackTrace();
                retval = e1.toString();
            }
        }
        else if (operation=="INSERT"){
            try {
                conn = DriverManager.getConnection(url);
                Statement st = conn.createStatement();
                String sql;

                sql= "INSERT  INTO traverseusers (name,username,email,date,persona,status,progress,password)" +
                       " VALUES ('"+name+"','"+username+"','"+email+"','"+date+"','"+persona+"',"+status+","+progress+",'"+password+"')";

                ResultSet rs = st.executeQuery(sql);

                rs.close();
                st.close();
                conn.close();
            } catch (java.sql.SQLException e1) {
                e1.printStackTrace();
                retval = e1.toString();
            }

        }
        else if(operation=="VERIFY"){
            try {
                conn = DriverManager.getConnection(url);
                Statement st = conn.createStatement();
                String sql, sql2;

                sql= "SELECT * FROM "+table+" WHERE '"+email+"'="+column;

                ResultSet rs = st.executeQuery(sql);

                if(!rs.next())
                {
                    result="empty";
                }else result="exist";

                rs.close();
                st.close();
                conn.close();
            } catch (java.sql.SQLException e1) {
                e1.printStackTrace();
                result = e1.toString();
            }

        }
        else if(operation=="UPDATE"){
            //TODO  ----   for updates on database

        }
        return retval;
    }
    @Override
    protected void onPostExecute(String value) {

    }
    protected void onProgressUpdate(Integer... progress) {

    }


}

