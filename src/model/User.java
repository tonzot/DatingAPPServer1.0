package model;

public class User {

    public String clientIp;
    public int clientPort;

    public User(String clientIp, int clientPort){
        this.clientIp = clientIp;
        this.clientPort = clientPort;
    }

    public String getClientIp(){
        return clientIp;
    }

}
