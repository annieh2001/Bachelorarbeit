import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.Match;
import org.eclipse.emf.henshin.interpreter.RuleApplication;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.eclipse.emf.henshin.interpreter.impl.RuleApplicationImpl;
import org.eclipse.emf.henshin.interpreter.util.InterpreterUtil;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.resource.HenshinResourceFactory;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import nestedcondition.NestedConstraint;
import nestedcondition.NestedconditionPackage;
import nestedconstraintmodel.NestedConstraintModel;

/**
 * class to repair the graph and compute a topological order
 */
public class GraphRepairAlgorithm {

	/**
	 * repair a given graph and saved in a new file
	 * 
	 * @param modelFile      contains the graph that should be repaired
	 * @param constraintFile contains the constraints that should be satisfied
	 * @param return         storage path of the result file
	 */
	public static String repairGraph(File modelFile, File constraintFile) {

		URI result = createResultFile(modelFile);

		if (constraintFile != null && modelFile != null) {

			// Initialize the relevant packages
			NestedconditionPackage.eINSTANCE.eClass();
			EcorePackage.eINSTANCE.eClass();
			// Register relevant resource factories for model loading
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new HenshinResourceFactory());
			// create a resource set
			HenshinResourceSet resourceSet = new HenshinResourceSet();

			// Load the constraints
			String constraintsPath = constraintFile.getAbsolutePath();
			URI constraintsUri = URI.createFileURI(constraintsPath);
			Resource nestedConstraintModel = resourceSet.getResource(constraintsUri, true);
			NestedConstraintModel nestedConstraints = (NestedConstraintModel) nestedConstraintModel.getContents()
					.get(0);
			EList<NestedConstraint> constraints = nestedConstraints.getNestedconstrainmodels();

			// Load the graph
			Resource graph = resourceSet.getResource(result, true);

			constraintsPath = constraintsPath.replace(".nestedconstraintmodel", "");
			String rulePath = Rules.createConstraintRepairRules(graph, constraints, constraintsPath);

			// Load the henshin module
			Module module = resourceSet.getModule(rulePath, true);

			EList<HashMap<NestedConstraint, EList<Rule>>> rules = splitRules(constraints, module);
			HashMap<NestedConstraint, EList<Rule>> constraintRule = rules.get(0);
			HashMap<NestedConstraint, EList<Rule>> repairRule = rules.get(1);

			boolean[][] dependencyConstraints = Dependency.computeDependencyMatrix(constraints, constraintRule);

			EList<NestedConstraint> topologicalOrder = computeTopologicalOrder(constraints, dependencyConstraints);

			transformGraph(graph, repairRule, topologicalOrder);

			try {
				graph.save(null);
				System.out.println("Repaired graph saved in " + (new File(result.toString())).getName());
				return result.toFileString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * split rules into constraint rules and repair rules
	 * 
	 * @param constraints that should be satisfied
	 * @param module      contains all rules that are required to repair the graph
	 * @return list with a map of constraint rule and a map of repair rules
	 */
	private static EList<HashMap<NestedConstraint, EList<Rule>>> splitRules(EList<NestedConstraint> constraints,
			Module module) {
		HashMap<NestedConstraint, EList<Rule>> constraintRule = new HashMap<>();
		HashMap<NestedConstraint, EList<Rule>> repairRule = new HashMap<>();
		EList<Rule> rules = module.getAllRules();

		for (Rule rule : rules) {
			for (NestedConstraint constraint : constraints) {
				String name = rule.getName();
				if (name.contains(constraint.getName())) {
					// rule is a constraint rule
					if (name.contains("Constraint-Rule")) {
						updateMap(constraintRule, constraint, rule);
					}
					// rule is a repair rule
					else {
						updateMap(repairRule, constraint, rule);
					}
				}
			}
		}
		EList<HashMap<NestedConstraint, EList<Rule>>> result = new BasicEList<>();
		result.add(constraintRule);
		result.add(repairRule);
		return result;
	}

	/**
	 * update map with rules
	 * @param rulesMap contains the rules of the constraints
	 * @param constraint value that should be updated
	 * @param rule to be added to the constraint value 
	 */
	private static void updateMap(HashMap<NestedConstraint, EList<Rule>> rulesMap, NestedConstraint constraint,
			Rule rule) {
		// looks whether the constrain exist as key in constraintRule
		EList<Rule> tempRuleList = rulesMap.get(constraint);
		// key does not exist
		if (tempRuleList == null) {
			tempRuleList = new BasicEList<>();
		}
		// update the list for the constraint
		tempRuleList.add(rule);
		rulesMap.put(constraint, tempRuleList);
	}

	/**
	 * create file for result
	 * 
	 * @param graph is the given graph
	 * @return file for results
	 */
	private static URI createResultFile(File graph) {
		String graphPath = graph.getAbsolutePath();
		graphPath = graphPath.replace(".xmi", "_result.xmi");
		File resFile = new File(graphPath);
		URI graphUri = URI.createFileURI(resFile.getAbsolutePath());
		try {
			FileUtils.copyFile(graph, resFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return graphUri;
	}

	/**
	 * compute a topological order of the constraints
	 * 
	 * @param constraint            that should be satisfied
	 * @param dependencyConstraints matrix with dependencies between constraints
	 * @throws RuntimeException if there is a cycle between the constraints
	 * @return a topological order of the constraints
	 */
	private static EList<NestedConstraint> computeTopologicalOrder(EList<NestedConstraint> constraint,
			boolean[][] dependencyConstraints) {
		int[] count = new int[dependencyConstraints.length];
		EList<NestedConstraint> topologicalOrder = new BasicEList<>();
		// list with numbers of dependences
		for (int i = 0; i < dependencyConstraints.length; i++) {
			for (int j = 0; j < dependencyConstraints.length; j++) {
				if (dependencyConstraints[i][j]) {
					count[i] += 1;
				}
			}
		}
		Deque<NestedConstraint> qu = new ArrayDeque<>();
		// qu contains constraints without dependency
		for (int i = 0; i < dependencyConstraints.length; i++) {
			if (count[i] == 0) {
				qu.add(constraint.get(i));
			}
		}
		while (!qu.isEmpty()) {
			NestedConstraint tempConstraint = qu.pop();
			topologicalOrder.add(tempConstraint);
			int index = constraint.indexOf(tempConstraint);
			for (int i = 0; i < dependencyConstraints.length; i++) {
				// reduces the count for all adjacent nodes by 1
				if (dependencyConstraints[i][index]) {
					count[i] -= 1;
					// if there is no dependency -> add to queue
					if (count[i] == 0) {
						qu.add(constraint.get(i));
					}
				}
			}
		}
		if (topologicalOrder.size() != constraint.size()) {
			throw new RuntimeException("Cycle between constraints");
		} else {
			return topologicalOrder;
		}
	}

	/**
	 * transform the graph so that there are no more violations of the constraints
	 * 
	 * @param graph            that should be repaired
	 * @param repairRuleMap    contains the rules to repair the graph
	 * @param topologicalOrder to repair the constraints
	 */
	private static void transformGraph(Resource graph, HashMap<NestedConstraint, EList<Rule>> repairRuleMap,
			EList<NestedConstraint> topologicalOrder) {
		// prepare the engine
		Engine engine = new EngineImpl();
		// initialize the graph
		EGraph repairedGraph = new EGraphImpl(graph);

		Scanner scanner = new Scanner(System.in);
		Random random = new Random();
		int randomInt;

		System.out.println("automatic repair? (y/n)");
		boolean interaction = false;
		if (scanner.next().toLowerCase().equals("n")) {
			interaction = true;
		}
		for (int i = 0; i < topologicalOrder.size(); i++) {
			EList<Rule> rules = repairRuleMap.get(topologicalOrder.get(i));
			while (true) {
				EList<Rule> tempRules = new BasicEList<Rule>();
				List<Match> matches;
				RuleApplication rp;
				// rules that can be used to repair the graph
				for (Rule rule : rules) {
					matches = InterpreterUtil.findAllMatches(engine, rule, repairedGraph, null);
					if (!matches.isEmpty())
						tempRules.add(rule);
				}
				// no violation of a constraint
				if (tempRules.isEmpty())
					break;
				Rule rule = null;
				if (tempRules.size() > 1 && interaction) rule = chooseT(tempRules, scanner);
				if (rule == null) {
					randomInt = random.nextInt(rules.size());
					rule = rules.get(randomInt);
				}
				// get all violating subgraphs of the graph
				matches = InterpreterUtil.findAllMatches(engine, rule, repairedGraph, null);
				Match subgraph = null;
				if (matches.size() > 1 && interaction) subgraph = chooseT(matches, scanner);
				if (subgraph == null) subgraph = matches.get(0);
				// transform graph
				rp = new RuleApplicationImpl(engine, repairedGraph, rule, subgraph);
				rp.execute(null);
			}
		}
	}

	/**
	 * user choose a element of a list
	 * 
	 * @param <T>     is the type of the list
	 * @param list    from which the element is chosen
	 * @param scanner is the user input
	 * @return the chosen element or null if the input is invalid
	 */
	private static <T> T chooseT(List<T> list, Scanner scanner) {
		T t = null;
		// user selects a rule
		String className = list.get(0).getClass().getInterfaces()[0].getSimpleName();
		System.out.println("Choose a " + className + ":");
		for (int j = 1; j <= list.size(); j++) {
			System.out.println(j + ". " + list.get(j - 1));
		}
		try {
			int randomInt = scanner.nextInt();
			t = list.get(randomInt - 1);
		} catch (InputMismatchException | IndexOutOfBoundsException e) {
			if (className.equals("Rule")) {
				System.out.println("Invalid input. A random Rule is chosen.");
			} else {
				System.out.println("Invalid input. The first Match is chosen.");
			}
		}
		return t;
	}

	/**
	 * check whether a graph satisfied the constraints
	 * @param graphFile the given graph
	 * @param ruleFile constraint rules of the constraints
	 * @return true, if the graph satisfied the constraints, otherwise false
	 */
	public static boolean isConsistency(File graphFile, File ruleFile) {
		HenshinResourceSet henshinResourceSet = new HenshinResourceSet();
		EcorePackage.eINSTANCE.eClass();
		henshinResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi",
				new XMIResourceFactoryImpl());
		henshinResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
		henshinResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*",
				new HenshinResourceFactory());

		String rulePath = ruleFile.getAbsolutePath();
		Module module = henshinResourceSet.getModule(rulePath, true);
		EList<Rule> rules = module.getAllRules();

		Resource graph2 = henshinResourceSet.getResource(graphFile.getAbsolutePath());
		EGraph repairedGraph = new EGraphImpl(graph2);
		Engine engine = new EngineImpl();

		for (int i = 1; i < rules.size(); i = i + 2) {
			List<Match> match = InterpreterUtil.findAllMatches(engine, rules.get(i), repairedGraph, null);
			if (!match.isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
