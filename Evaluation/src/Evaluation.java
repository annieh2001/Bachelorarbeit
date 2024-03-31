import java.io.ByteArrayInputStream;
import java.io.File;

public class Evaluation {

	public static void main(String[] args) {
		long start, end;
		ByteArrayInputStream testInput;
		String input = "y \n";
		long sum = 0;
		long time = 0;

		for (int i = 0; i < 5; i++) {
			testInput = new ByteArrayInputStream(input.getBytes());
			System.setIn(testInput);
			GraphRepairAlgorithm.repairGraph(new File("model\\bank_100_nodes.xmi"),
					new File("model\\bank_constraints.nestedconstraintmodel"));
		}
		
		System.out.println("100 Nodes: ");
		for (int i = 0; i < 5; i++) {
			testInput = new ByteArrayInputStream(input.getBytes());
			System.setIn(testInput);
			start = System.currentTimeMillis();
			GraphRepairAlgorithm.repairGraph(new File("model\\bank_100_nodes.xmi"),
					new File("model\\bank_constraints.nestedconstraintmodel"));
			end = System.currentTimeMillis();
			time = end - start;
			System.out.println(time);
			sum += time;
		}
		sum /= 5;
		System.out.println("Laufzeit 100 Nodes: " + sum + "ms \n");

		sum = 0;
		System.out.println("250 Nodes: ");
		for (int i = 0; i < 5; i++) {
			testInput = new ByteArrayInputStream(input.getBytes());
			System.setIn(testInput);
			start = System.currentTimeMillis();
			GraphRepairAlgorithm.repairGraph(new File("model\\bank_250_nodes.xmi"),
					new File("model\\bank_constraints.nestedconstraintmodel"));
			end = System.currentTimeMillis();
			time = end - start;
			System.out.println(time);
			sum += time;
		}
		sum /= 5;
		System.out.println("Laufzeit 250 Nodes: " + sum + "ms \n");

		sum = 0;
		System.out.println("500 Nodes: ");
		for (int i = 0; i < 5; i++) {
			testInput = new ByteArrayInputStream(input.getBytes());
			System.setIn(testInput);
			start = System.currentTimeMillis();
			GraphRepairAlgorithm.repairGraph(new File("model\\bank_500_nodes.xmi"),
					new File("model\\bank_constraints.nestedconstraintmodel"));
			end = System.currentTimeMillis();
			time = end - start;
			System.out.println(time);
			sum += time;
		}
		sum /= 5;
		System.out.println("Laufzeit 500 Nodes: " + sum + "ms \n");

		sum = 0;
		System.out.println("750 Nodes: ");
		for (int i = 0; i < 5; i++) {
			testInput = new ByteArrayInputStream(input.getBytes());
			System.setIn(testInput);
			start = System.currentTimeMillis();
			GraphRepairAlgorithm.repairGraph(new File("model\\bank_750_nodes.xmi"),
					new File("model\\bank_constraints.nestedconstraintmodel"));
			end = System.currentTimeMillis();
			time = end - start;
			System.out.println(time);
			sum += time;
		}
		sum /= 5;
		System.out.println("Laufzeit 750 Nodes: " + sum + "ms \n");

		sum = 0;
		System.out.println("1000 Nodes: ");
		for (int i = 0; i < 5; i++) {
			testInput = new ByteArrayInputStream(input.getBytes());
			System.setIn(testInput);
			start = System.currentTimeMillis();
			GraphRepairAlgorithm.repairGraph(new File("model\\bank_1000_nodes.xmi"),
					new File("model\\bank_constraints.nestedconstraintmodel"));
			end = System.currentTimeMillis();
			time = end - start;
			System.out.println(time);
			sum += time;
		}
		sum /= 5;
		System.out.println("Laufzeit 1000 Nodes: " + sum + "ms");
		
		
		System.out.println(GraphRepairAlgorithm.isConsistency(new File("model\\bank_100_nodes_result.xmi"),
				new File("model\\bank_constraints.henshin")));
		System.out.println(GraphRepairAlgorithm.isConsistency(new File("model\\bank_250_nodes_result.xmi"),
				new File("model\\bank_constraints.henshin")));
		System.out.println(GraphRepairAlgorithm.isConsistency(new File("model\\bank_500_nodes_result.xmi"),
				new File("model\\bank_constraints.henshin")));
		System.out.println(GraphRepairAlgorithm.isConsistency(new File("model\\bank_750_nodes_result.xmi"),
				new File("model\\bank_constraints.henshin")));
		System.out.println(GraphRepairAlgorithm.isConsistency(new File("model\\bank_1000_nodes_result.xmi"),
				new File("model\\bank_constraints.henshin")));
	}
}



