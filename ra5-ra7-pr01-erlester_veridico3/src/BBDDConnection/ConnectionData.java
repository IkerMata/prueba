package src.BBDDConnection;

public class ConnectionData {
    private static String url = "jdbc:mysql://daw.inspedralbes.cat:3306/a24biedommar_Hotel";
    private static String usr = "a24biedommar_IkerBiel";
    private static String pwd = "P23cGNAQVYe|[=O)";

    public static String getUrl() {
        return url;
    }

    public static String getUsr() {
        return usr;
    }

    public static String getPwd() {
        return pwd;
    }
}