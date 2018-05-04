package lojadebicicletas.v2.pkg0;

import java.util.ArrayList;

public interface RMIServerInterface extends java.rmi.Remote{
    public boolean registerClient(String IP, int port, RMIClientInterface CliInterface) throws java.rmi.RemoteException;
    public boolean registerCategory(String ip, String category) throws java.rmi.RemoteException;
    public ArrayList getClientsSellingCategory(String category, String IP) throws java.rmi.RemoteException;
    public boolean logoutClient(String IP,int port) throws java.rmi.RemoteException;
}

