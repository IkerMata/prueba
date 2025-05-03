package BBDDConnection;
import src.BBDDConnection.ConnectionData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexioBD {

    private static Connection connection;

    private ConexioBD(){

    }

    private static void openConnection() {
        String usr = ConnectionData.getUsr();
        String pwd = ConnectionData.getPwd();
        String url = ConnectionData.getUrl();
        try{
            connection = DriverManager.getConnection(url,usr,pwd);
        }catch (SQLException e) {
            System.out.println("ERROR! al establir la connexió: " + e.getMessage() );
        }
    }

    public static Connection getInstance(){
        if(connection == null){
            openConnection();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("ERROR! No s'ha pogut tancar la connexió: "+ e.getMessage());
        }
    }
}