/**
 */
package bank;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bank</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link bank.Bank#getClients <em>Clients</em>}</li>
 *   <li>{@link bank.Bank#getAccounts <em>Accounts</em>}</li>
 *   <li>{@link bank.Bank#getPortfolios <em>Portfolios</em>}</li>
 * </ul>
 *
 * @see bank.BankPackage#getBank()
 * @model
 * @generated
 */
public interface Bank extends EObject {
	/**
	 * Returns the value of the '<em><b>Clients</b></em>' containment reference list.
	 * The list contents are of type {@link bank.Client}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Clients</em>' containment reference list.
	 * @see bank.BankPackage#getBank_Clients()
	 * @model containment="true"
	 * @generated
	 */
	EList<Client> getClients();

	/**
	 * Returns the value of the '<em><b>Accounts</b></em>' containment reference list.
	 * The list contents are of type {@link bank.Account}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accounts</em>' containment reference list.
	 * @see bank.BankPackage#getBank_Accounts()
	 * @model containment="true"
	 * @generated
	 */
	EList<Account> getAccounts();

	/**
	 * Returns the value of the '<em><b>Portfolios</b></em>' containment reference list.
	 * The list contents are of type {@link bank.Portfolio}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Portfolios</em>' containment reference list.
	 * @see bank.BankPackage#getBank_Portfolios()
	 * @model containment="true"
	 * @generated
	 */
	EList<Portfolio> getPortfolios();

} // Bank
