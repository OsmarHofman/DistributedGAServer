package br.sc.edu.ifsc.ga.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.sc.edu.ifsc.ga.util.DTORating;
import br.sc.edu.ifsc.ga.util.DTOServerData;

public interface IRating extends Remote {

	public List<DTORating> rate(DTOServerData serverData) throws RemoteException;
}
