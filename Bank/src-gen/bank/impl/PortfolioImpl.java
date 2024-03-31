/**
 */
package bank.impl;

import bank.Account;
import bank.BankPackage;
import bank.Portfolio;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Portfolio</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link bank.impl.PortfolioImpl#getAssociates <em>Associates</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortfolioImpl extends MinimalEObjectImpl.Container implements Portfolio {
	/**
	 * The cached value of the '{@link #getAssociates() <em>Associates</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssociates()
	 * @generated
	 * @ordered
	 */
	protected EList<Account> associates;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortfolioImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BankPackage.Literals.PORTFOLIO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Account> getAssociates() {
		if (associates == null) {
			associates = new EObjectResolvingEList<Account>(Account.class, this, BankPackage.PORTFOLIO__ASSOCIATES);
		}
		return associates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case BankPackage.PORTFOLIO__ASSOCIATES:
			return getAssociates();
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
		case BankPackage.PORTFOLIO__ASSOCIATES:
			getAssociates().clear();
			getAssociates().addAll((Collection<? extends Account>) newValue);
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
		case BankPackage.PORTFOLIO__ASSOCIATES:
			getAssociates().clear();
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
		case BankPackage.PORTFOLIO__ASSOCIATES:
			return associates != null && !associates.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //PortfolioImpl
