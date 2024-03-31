/**
 */
package bank.impl;

import bank.Account;
import bank.Bank;
import bank.BankPackage;
import bank.Client;
import bank.Portfolio;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bank</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link bank.impl.BankImpl#getClients <em>Clients</em>}</li>
 *   <li>{@link bank.impl.BankImpl#getAccounts <em>Accounts</em>}</li>
 *   <li>{@link bank.impl.BankImpl#getPortfolios <em>Portfolios</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BankImpl extends MinimalEObjectImpl.Container implements Bank {
	/**
	 * The cached value of the '{@link #getClients() <em>Clients</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClients()
	 * @generated
	 * @ordered
	 */
	protected EList<Client> clients;

	/**
	 * The cached value of the '{@link #getAccounts() <em>Accounts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccounts()
	 * @generated
	 * @ordered
	 */
	protected EList<Account> accounts;

	/**
	 * The cached value of the '{@link #getPortfolios() <em>Portfolios</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortfolios()
	 * @generated
	 * @ordered
	 */
	protected EList<Portfolio> portfolios;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BankImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BankPackage.Literals.BANK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Client> getClients() {
		if (clients == null) {
			clients = new EObjectContainmentEList<Client>(Client.class, this, BankPackage.BANK__CLIENTS);
		}
		return clients;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Account> getAccounts() {
		if (accounts == null) {
			accounts = new EObjectContainmentEList<Account>(Account.class, this, BankPackage.BANK__ACCOUNTS);
		}
		return accounts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Portfolio> getPortfolios() {
		if (portfolios == null) {
			portfolios = new EObjectContainmentEList<Portfolio>(Portfolio.class, this, BankPackage.BANK__PORTFOLIOS);
		}
		return portfolios;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case BankPackage.BANK__CLIENTS:
			return ((InternalEList<?>) getClients()).basicRemove(otherEnd, msgs);
		case BankPackage.BANK__ACCOUNTS:
			return ((InternalEList<?>) getAccounts()).basicRemove(otherEnd, msgs);
		case BankPackage.BANK__PORTFOLIOS:
			return ((InternalEList<?>) getPortfolios()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case BankPackage.BANK__CLIENTS:
			return getClients();
		case BankPackage.BANK__ACCOUNTS:
			return getAccounts();
		case BankPackage.BANK__PORTFOLIOS:
			return getPortfolios();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case BankPackage.BANK__CLIENTS:
			getClients().clear();
			getClients().addAll((Collection<? extends Client>) newValue);
			return;
		case BankPackage.BANK__ACCOUNTS:
			getAccounts().clear();
			getAccounts().addAll((Collection<? extends Account>) newValue);
			return;
		case BankPackage.BANK__PORTFOLIOS:
			getPortfolios().clear();
			getPortfolios().addAll((Collection<? extends Portfolio>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case BankPackage.BANK__CLIENTS:
			getClients().clear();
			return;
		case BankPackage.BANK__ACCOUNTS:
			getAccounts().clear();
			return;
		case BankPackage.BANK__PORTFOLIOS:
			getPortfolios().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case BankPackage.BANK__CLIENTS:
			return clients != null && !clients.isEmpty();
		case BankPackage.BANK__ACCOUNTS:
			return accounts != null && !accounts.isEmpty();
		case BankPackage.BANK__PORTFOLIOS:
			return portfolios != null && !portfolios.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //BankImpl
