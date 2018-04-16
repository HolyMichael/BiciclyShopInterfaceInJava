package lojadebicicletas.v2.pkg0;

public interface RMIInterface extends java.rmi.Remote{
    public java.util.Date getDate() throws java.rmi.RemoteException;
    public boolean registerClient(String IP) throws java.rmi.RemoteException;
    public int getCount() throws java.rmi.RemoteException;
}



