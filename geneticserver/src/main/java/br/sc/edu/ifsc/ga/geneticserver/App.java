package br.sc.edu.ifsc.ga.geneticserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import br.sc.edu.ifsc.ga.algorithm.Algorithm;
import br.sc.edu.ifsc.ga.interfaces.IRating;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Inciando Servidor...");

		try {
			System.out.println("\nRegistrando serviÃ§o de seguranÃ§a");
			System.setSecurityManager(new SecurityManager());

			IRating rating = new Algorithm();

			System.setProperty("java.rmi.server.hostname", "127.0.0.1");
			LocateRegistry.createRegistry(1099);

			System.out.println("\n\tRegistrando objeto no RMIRegistry");
			Naming.rebind("rmi://127.0.0.1:1099/Evaluation", rating);

			System.out.println("\nAguardando RequisiÃ§Ãµes");

		} catch (RemoteException | MalformedURLException e) {
			System.err.println("\nErro: " + e.getMessage());
			System.exit(1);
		}

	}
}
