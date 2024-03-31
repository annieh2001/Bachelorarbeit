import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import bank.Account;
import bank.Bank;
import bank.BankFactory;
import bank.BankPackage;
import bank.Client;
import bank.Portfolio;

public class CreateBankModel {

	/**
	 * extends a given graph
	 * @param model contains the path of the model
	 * @param node number of nodes to be added
	 * @param path of the extended graph
	 */
	public static void model(String model, int node, String path) {
		BankPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("xmi", new XMIResourceFactoryImpl());

		BankFactory factory = BankFactory.eINSTANCE;
		
		ResourceSetImpl rs = new ResourceSetImpl();
		Resource resource = rs.getResource(URI.createURI(model), true);
		Bank bank = (Bank) resource.getContents().get(0);
		
		int counterClient = bank.getClients().size();
		int counterAccount = bank.getAccounts().size();

		for (int i = 0; i < node; i++) {
			Random random = new Random();
			int randomInt;

			randomInt = random.nextInt(3);
			if (randomInt == 0) {
				Account account = factory.createAccount();
				counterAccount += 1;
				account.setId(counterAccount);
				randomInt = random.nextInt(10);
				if (randomInt != 0) {
					randomInt = random.nextInt(2);
					if (randomInt == 0) {
						randomInt = random.nextInt(bank.getClients().size());
						Client tempClient = bank.getClients().get(randomInt);
						tempClient.getHas().add(account);
					} else {
						randomInt = random.nextInt(bank.getPortfolios().size());
						Portfolio tempPortfolio = bank.getPortfolios().get(randomInt);
						tempPortfolio.getAssociates().add(account);
					}
				}
				bank.getAccounts().add(account);
			} else if (randomInt == 1) {
				Client client = factory.createClient();
				counterClient += 1;
				client.setName("client_" + counterClient);
				randomInt = random.nextInt(10);
				if (randomInt != 0) {
					randomInt = random.nextInt(2);
					if (randomInt == 0) {
						randomInt = random.nextInt(bank.getAccounts().size());
						Account tempAccount = bank.getAccounts().get(randomInt);
						client.getHas().add(tempAccount);
					} else {
						randomInt = random.nextInt(bank.getPortfolios().size());
						Portfolio tempPortfolio = bank.getPortfolios().get(randomInt);
						client.getOwns().add(tempPortfolio);
					}
				}
				bank.getClients().add(client);
			} else {
				Portfolio portfolio = factory.createPortfolio();
				randomInt = random.nextInt(10);
				if (randomInt != 0) {
					randomInt = random.nextInt(2);
					if (randomInt == 0) {
						randomInt = random.nextInt(bank.getClients().size());
						Client tempClient = bank.getClients().get(randomInt);
						tempClient.getOwns().add(portfolio);
					} else {
						randomInt = random.nextInt(bank.getAccounts().size());
						Account tempAccount = bank.getAccounts().get(randomInt);
						portfolio.getAssociates().add(tempAccount);
					}
				}
				bank.getPortfolios().add(portfolio);
			}
		}

		ResourceSetImpl resourceSet = new ResourceSetImpl();
		Resource resource2 = resourceSet.createResource(URI.createURI(path));

		resource2.getContents().add(bank);
		
		try {
			resource2.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
