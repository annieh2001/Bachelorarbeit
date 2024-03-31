/**
 */
package bank;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Portfolio</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link bank.Portfolio#getAssociates <em>Associates</em>}</li>
 * </ul>
 *
 * @see bank.BankPackage#getPortfolio()
 * @model
 * @generated
 */
public interface Portfolio extends EObject {
	/**
	 * Returns the value of the '<em><b>Associates</b></em>' reference list.
	 * The list contents are of type {@link bank.Account}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Associates</em>' reference list.
	 * @see bank.BankPackage#getPortfolio_Associates()
	 * @model
	 * @generated
	 */
	EList<Account> getAssociates();

} // Portfolio
