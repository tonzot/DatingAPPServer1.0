package model;

import model.abitur.netz.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DatingServer extends Server {

    private ArrayList<User> activeUsers;

    public DatingServer(int pPort) {
        super(pPort);
        activeUsers = new ArrayList<>();
    }

    @Override
    public void processNewConnection(String pClientIP, int pClientPort) {
        activeUsers.add(new User(pClientIP, pClientPort));
        System.out.println("Neue Verbindung: Ip: " + pClientIP + " Port: " + pClientPort);
    }

    @Override
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        String[] nachrichtenTeil = pMessage.split(";");
        //CONNECT;Benutzername;Passwort
        if(nachrichtenTeil[0].equals("CONNECT")){
            if(einloggen(nachrichtenTeil[1], nachrichtenTeil[2])){

            }
            processNewConnection(pClientIP, pClientPort);

        } else if(nachrichtenTeil[0].equals("PICK")){
            int x = Integer.parseInt(nachrichtenTeil[1]);
            int y = Integer.parseInt(nachrichtenTeil[2]);

        } else if(nachrichtenTeil[0].equals("LEAVE")){
            processClosingConnection(pClientIP, pClientPort);

        }else{
            send(pClientIP, pClientPort, "FALSECOMMAND;Geben Sie bitte einen richtigen Befehl ein.");
        }

    }

    @Override
    public void processClosingConnection(String pClientIP, int pClientPort) {
        Iterator activeUsersIterator = activeUsers.iterator();

        for(int i = 0; activeUsersIterator.hasNext(); i++){
            if(activeUsers.get(i).getClientIp().equals(pClientIP)){
                activeUsers.remove(i);
                System.out.println("Verbindung beendet: Ip: " + pClientIP + " Port: " + pClientPort);
                System.out.println("Benutzer aus der Arraylist entfernt");
            }
        }

    }

    public boolean einloggen(String benutzername, String passwort){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT *" +
                    "FROM user" +
                    "WHERE benutzername = " + benutzername + " & passwort = " + passwort + ";");

            if(rs != null) {
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }

}
