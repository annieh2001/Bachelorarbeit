import java.util.HashMap;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.compact.CModule;
import org.eclipse.emf.henshin.multicda.cda.ConflictAnalysis;
import org.eclipse.emf.henshin.multicda.cda.DependencyAnalysis;
import org.eclipse.emf.henshin.multicda.cda.MultiGranularAnalysis;
import org.eclipse.emf.henshin.multicda.cda.units.Span;

import nestedcondition.NestedConstraint;

/**
 * class for computing the conflict graph
 */
public class Dependency {

	/**
	 * compute matrix with dependencies between constraints
	 * 
	 * @param constraints     that should be satisfied
	 * @param constraintRules rules to satisfied a constraint
	 * @return matrix with dependencies between constraints
	 */
	static boolean[][] computeDependencyMatrix(EList<NestedConstraint> constraints,
			HashMap<NestedConstraint, EList<Rule>> constraintRules) {
		boolean[][] dependencyConstraints = new boolean[constraints.size()][constraints.size()];
		for (int i = 0; i < constraints.size(); i++) {
			EList<Rule> tempConstraintRules = constraintRules.get(constraints.get(i));
			for (int j = 0; j < tempConstraintRules.size(); j++) {
				for (int k = 0; k < constraints.size(); k++) {
					// checks, whether k-th constraint is dependent on i-th constraint
					if (isDependency(tempConstraintRules.get(j), constraints.get(k))) {
						dependencyConstraints[j][i] = true;
					}
				}
			}
		}
		return dependencyConstraints;
	}

	/**
	 * checks, whether the constraint of the rule is dependent to other constraints
	 * 
	 * @param rule       of a constraint
	 * @param constraint that should be satisfied
	 * @return true if the rule is dependent to other constraints, otherwise false
	 */
	private static boolean isDependency(Rule rule, NestedConstraint constraint) {
		CModule module = Rules.createCheckRule(constraint);
		module.addRule(rule);
		EList<Rule> rules = module.getModule().getAllRules();
		EList<Rule> checkRule_parallel = new BasicEList<Rule>();
		EList<Rule> checkRule_sequential = new BasicEList<Rule>();
		Rule constraintRule = null;
		// split rules for the parallel and sequential dependency
		for (Rule rule2 : rules) {
			if (rule2.getName().contains("parallel")) {
				checkRule_parallel.add(rule2);
			} else if (rule2.getName().contains("sequential")) {
				checkRule_sequential.add(rule2);
			} else {
				constraintRule = rule2;
			}
		}
		if(analyze(checkRule_sequential, constraintRule, true)) return true;
		return analyze(checkRule_parallel, constraintRule, false);
	}
	
	/**
	 * checks for either sequential or parallel dependency between constraints
	 * @param rules		List of constraints
	 * @param constraintRule rule to analyze dependency
	 * @param seq	true, check for sequential dependency, false, check for parallel
	 * @return true, when there is a dependency, otherwise false
	 */
	private static boolean analyze(EList<Rule> rules, Rule constraintRule, boolean seq) {
		for (Rule rule : rules) {
			MultiGranularAnalysis analysis;
			if(seq) analysis = new DependencyAnalysis(constraintRule, rule);
			else analysis = new ConflictAnalysis(constraintRule, rule);
			Span result = analysis.computeResultsBinary();
			if (result != null) return true;
		}
		return false;
	}
}
