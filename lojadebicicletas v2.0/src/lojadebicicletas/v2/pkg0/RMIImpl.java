package lojadebicicletas.v2.pkg0;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIImpl extends UnicastRemoteObject implements RMIInterface{
     int count;
    public RMIImpl(String name) throws RemoteException {
        super();
        try {
            Naming.rebind(name, this);
        }
        catch (Exception e) {
            if (e instanceof RemoteException)
            throw (RemoteException)e;
            else
            throw new RemoteException(e.getMessage());
        }
    }
    
    public java.util.Date getDate() {
        System.out.println(" Método remoto -- RMIImpl.getDate()");
        count++;
        return new java.util.Date();
    }
    
    
    public int getCount(){
        return count;
    }
}

