import java.util.HashMap;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.henshin.model.compact.CModule;
import org.eclipse.emf.henshin.model.compact.CNode;
import org.eclipse.emf.henshin.model.compact.CRule;

import graph.Edge;
import graph.Graph;
import graph.Node;
import nestedcondition.EdgeMapping;
import nestedcondition.Formula;
import nestedcondition.Morphism;
import nestedcondition.NestedCondition;
import nestedcondition.NestedConstraint;
import nestedcondition.NodeMapping;
import nestedcondition.QuantifiedCondition;

/**
 * class for creating constraint rules, repair rules and check rules
 */
public class Rules {

	/**
	 * create constraint rules and repair rules from constraints
	 * 
	 * @param graph       is the given graph
	 * @param constraints that should be satisfied
	 * @return file path of the repair and constraint rules
	 */
	static String createConstraintRepairRules(Resource graph, EList<NestedConstraint> constraints, String rulePath) {
		CModule module = new CModule("Rules");

		EList<EdgeMapping> edgeMappings = new BasicEList<>();
		EList<NodeMapping> nodeMappings = new BasicEList<>();

		HashMap<Node, CNode> nodeMapConstraint = new HashMap<>();
		HashMap<Node, CNode> nodeMapRepair = new HashMap<>();
		HashMap<Node, CNode> nodeMapRepairNac = new HashMap<>();

		for (NestedConstraint constraint : constraints) {
			EList<EObject> condition = constraint.getCondition().eContents();

			CRule constraintRule = module.createRule("Constraint-Rule_" + constraint.getName());
			CRule repairRule = module.createRule("Repair-Rule_" + constraint.getName());

			// two-part constraints
			if (condition.get(0) instanceof QuantifiedCondition) {
				EList<EObject> condition2 = ((NestedCondition) condition.get(0)).eContents();

				for (EObject eObj : condition2) {
					if (eObj instanceof Morphism) {
						// get edge and node mappings of a constraint
						edgeMappings = ((Morphism) eObj).getEdgeMappings();
						nodeMappings = ((Morphism) eObj).getNodeMappings();
					}
					if (eObj instanceof Graph) {
						// get edges and nodes of the constraint
						EList<Node> nodes = ((Graph) eObj).getNodes();
						EList<Edge> edges = ((Graph) eObj).getEdges();

						// add nodes from the first part of a constraint into the rules
						for (NodeMapping nodeMapping : nodeMappings) {
							Node node = nodeMapping.getImage();
							nodeMapConstraint.put(node, constraintRule.createNode(node.getType()));
							nodeMapRepair.put(node, repairRule.createNode(node.getType()));
							nodes.remove(node);
						}

						// add remaining nodes from the constraint
						for (Node node : nodes) {
							nodeMapConstraint.put(node, constraintRule.createNode(node.getType(), "create"));
							nodeMapRepair.put(node, repairRule.createNode(node.getType(), "create"));
							nodeMapRepairNac.put(node, repairRule.createNode(node.getType(), "forbid"));

							EClass rootClass = graph.getContents().get(0).eClass();
							CNode tempNode = repairRule.createNode(rootClass);

							addContainment(node, rootClass, tempNode, nodeMapRepair);
						}

						// add edges from the first part of a constraint between nodes
						for (EdgeMapping edgeMapping : edgeMappings) {
							Edge edge = edgeMapping.getImage();
							Node source = edge.getSource();
							Node target = edge.getTarget();
							EReference type = edge.getType();
							(nodeMapConstraint.get(source)).createEdge(nodeMapConstraint.get(target), type);
							(nodeMapRepair.get(source)).createEdge(nodeMapRepair.get(target), type);
							edges.remove(edge);
						}

						// add remaining edges between nodes
						for (Edge edge : edges) {
							Node source = edge.getSource();
							Node target = edge.getTarget();
							EReference type = edge.getType();
							(nodeMapConstraint.get(source)).createEdge(nodeMapConstraint.get(target), type, "create");
							(nodeMapRepair.get(source)).createEdge(nodeMapRepair.get(target), type, "create");
							if (nodeMapRepairNac.get(source) == null) {
								nodeMapRepairNac.put(source, nodeMapRepair.get(source));
							}
							if (nodeMapRepairNac.get(target) == null) {
								nodeMapRepairNac.put(target, nodeMapRepair.get(target));
							}
							(nodeMapRepairNac.get(source)).createEdge(nodeMapRepairNac.get(target), type, "forbid");
						}
						nodeMapConstraint.clear();
						nodeMapRepair.clear();
						nodeMapRepairNac.clear();
					}
				}
			}

			// one-part constraint
			if (condition.get(0) instanceof Formula) {
				for (EObject eObj : condition) {
					if (eObj instanceof Graph) {
						// get edges and nodes of a constraint
						EList<Node> nodes = ((Graph) eObj).getNodes();
						EList<Edge> edges = ((Graph) eObj).getEdges();

						// add nodes into rules
						for (Node node : nodes) {
							nodeMapConstraint.put(node, constraintRule.createNode(node.getType()));
							nodeMapRepair.put(node, repairRule.createNode(node.getType()));
						}

						// add edges between nodes
						for (int l = 0; l < edges.size(); l++) {
							Edge edge = edges.get(l);
							Node source = edge.getSource();
							Node target = edge.getTarget();
							EReference type = edge.getType();

							if (l == edges.size() - 1) {
								// mark edge with delete
								(nodeMapConstraint.get(source)).createEdge(nodeMapConstraint.get(target), type,
										"delete");
								(nodeMapRepair.get(source)).createEdge(nodeMapRepair.get(target), type, "delete");
							} else {
								(nodeMapConstraint.get(source)).createEdge(nodeMapConstraint.get(target), type);
								(nodeMapRepair.get(source)).createEdge(nodeMapRepair.get(target), type);
							}
						}
					}
				}
			}
			nodeMapConstraint.clear();
			nodeMapRepair.clear();
		}
		module.save(rulePath);
		return rulePath + ".henshin";
	}

	/**
	 * create check rules of a constraint for the dependency analysis
	 * 
	 * @param constraint to create check rules
	 * @return check rules for a constraint
	 */
	static CModule createCheckRule(NestedConstraint constraint) {
		CModule module = new CModule("Check-Rules");

		EList<EObject> condition = constraint.getCondition().eContents();
		HashMap<Node, CNode> nodeMap = new HashMap<>();
		// create rule for sequential dependency
		if (condition.get(2) instanceof Graph) {
			CRule checkRuleSequential = module.createRule("Check-Rule_sequential_" + constraint.getName());
			EList<Node> nodes = ((Graph) condition.get(2)).getNodes();
			EList<Edge> edges = ((Graph) condition.get(2)).getEdges();
			// add nodes into rule
			for (Node node : nodes) {
				nodeMap.put(node, checkRuleSequential.createNode(node.getType()));
			}
			// add edges between nodes
			for (Edge edge : edges) {
				Node source = edge.getSource();
				Node target = edge.getTarget();
				EReference type = edge.getType();
				(nodeMap.get(source)).createEdge(nodeMap.get(target), type);
			}
			nodeMap.clear();
		}

		EList<EdgeMapping> edgeMappings = new BasicEList<>();
		EList<NodeMapping> nodeMappings = new BasicEList<>();

		// two-part constraints
		if (condition.get(0) instanceof QuantifiedCondition) {
			EList<EObject> condition2 = ((NestedCondition) condition.get(0)).eContents();
			EList<Node> nodes = new BasicEList<Node>();
			EList<Edge> edges = new BasicEList<Edge>();

			// get edges and nodes that only part of the second graph of a constraints
			for (EObject eObj : condition2) {
				if (eObj instanceof Morphism) {
					edgeMappings = ((Morphism) eObj).getEdgeMappings();
					nodeMappings = ((Morphism) eObj).getNodeMappings();
				}
				if (eObj instanceof Graph) {
					nodes = ((Graph) eObj).getNodes();
					edges = ((Graph) eObj).getEdges();
					for (NodeMapping nodeMapping : nodeMappings) {
						Node node = nodeMapping.getImage();
						nodes.remove(node);
					}
					for (EdgeMapping edgeMapping : edgeMappings) {
						Edge edge = edgeMapping.getImage();
						edges.remove(edge);
					}
				}
			}

			EList<EList<Node>> subsetNode = createSubset(nodes);
			EList<EList<Edge>> subsetEdge = createSubset(edges);
			// create rules for parallel dependency of two-part constraints
			for (EList<Node> subsetNode2 : subsetNode) {
				for (EList<Edge> subsetEdge2 : subsetEdge) {
					if (subsetNode2.isEmpty() && subsetEdge2.isEmpty()) continue;
					else {
						CRule checkRule = module.createRule("Check-Rule_parallel_" + constraint.getName());
						// add nodes to rule
						for (Node node : subsetNode2) {
							nodeMap.put(node, checkRule.createNode(node.getType()));
						}
						for (Edge edge : subsetEdge2) {
							Node source = edge.getSource();
							Node target = edge.getTarget();
							EReference type = edge.getType();
							// create source node of an edge if it does not exist in the rule
							if (nodeMap.get(source) == null) {
								nodeMap.put(source, checkRule.createNode(source.getType()));
							}
							// create target node of an edge if it does not exist in the rule
							if (nodeMap.get(target) == null) {
								nodeMap.put(target, checkRule.createNode(target.getType()));
							}
							(nodeMap.get(source)).createEdge(nodeMap.get(target), type);
						}
						nodeMap.clear();
					}
				}
			}
		}
		return module;
	}

	/**
	 * add a source node of a containment reference
	 * 
	 * @param node of the constraint
	 * @param rootClass EClass of the containment
	 * @param cNode of the repair rules
	 * @param nodeMapRepair contains the nodes of a CRule
	 */
	private static void addContainment(Node node, EClass rootClass, CNode cNode, HashMap<Node, CNode> nodeMapRepair) {
		// check if node is containment
		if (!node.eContainmentFeature().isContainment())
			return;
		// add edge between the nodes
		for (EObject eClass : node.getType().eContainer().eContents()) {
			if (eClass != rootClass)
				continue;
			for (EObject edge : eClass.eContents()) {
				if (!edge.eCrossReferences().contains(node.getType()))
					continue;
				EReference type = (EReference) edge;
				cNode.createEdge(nodeMapRepair.get(node), (EReference) type, "create");
				return;
			}
			return;
		}
	}

	/**
	 * create all subset of a set of elements
	 * 
	 * @param element is a set
	 * @return subsets of a set
	 */
	private static <T> EList<EList<T>> createSubset(EList<T> element) {
		EList<EList<T>> subset = new BasicEList<EList<T>>();
		int max = 1 << element.size();
		for (int i = 1; i < max; i++) {
			EList<T> tempList = new BasicEList<T>();
			for (int j = 0; j < max; j++) {
				if ((i & (1 << j)) != 0) {
					tempList.add(element.get(j));
				}
			}
			subset.add(tempList);
		}
		subset.add(new BasicEList<T>());
		return subset;
	}

}
