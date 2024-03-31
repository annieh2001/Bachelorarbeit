import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class GraphRepairAlgorithmTest {

	@Test
	void repairGraphTestNoInteraction() {
		ByteArrayInputStream testInput;
		String input = "y \n";
		testInput = new ByteArrayInputStream(input.getBytes());
		System.setIn(testInput);

		File graph = new File("model\\bank.xmi");
		File constraints = new File("model\\bank_constraints.nestedconstraintmodel");
		String result = GraphRepairAlgorithm.repairGraph(graph, constraints);

		byte[] actualResultBytes = null;
		byte[] expectedResultBytes = null;
		try {
			actualResultBytes = Files.readAllBytes(Paths.get(result));
			expectedResultBytes = Files.readAllBytes(Paths.get("model\\bank_result_no_interaction.xmi"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String actualResult = new String(actualResultBytes, StandardCharsets.UTF_8);
		String expectedResult = new String(expectedResultBytes, StandardCharsets.UTF_8);

		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void repairGraphTestNoInteractionConsistency() {
		ByteArrayInputStream testInput;
		String input = "y \n";
		testInput = new ByteArrayInputStream(input.getBytes());
		System.setIn(testInput);
		
		File graph = new File("model\\bank.xmi");
		File constraints = new File("model\\bank_constraints.nestedconstraintmodel");
		String result = GraphRepairAlgorithm.repairGraph(graph, constraints);

		String rulePath = constraints.getAbsolutePath();
		rulePath = rulePath.replace(".nestedconstraintmodel", ".henshin");
		
		boolean expectedResult = GraphRepairAlgorithm.isConsistency(new File(result), new File(rulePath));
		assertTrue(expectedResult);
	}

	@Test
	void repairGraphTestInteraction() {
		ByteArrayInputStream testInput;
		String input = "n \n 2 \n";
		testInput = new ByteArrayInputStream(input.getBytes());
		System.setIn(testInput);

		File graph = new File("model\\bank.xmi");
		File constraints = new File("model\\bank_constraints.nestedconstraintmodel");
		String result = GraphRepairAlgorithm.repairGraph(graph, constraints);

		byte[] actualResultBytes = null;
		byte[] expectedResultBytes = null;
		try {
			actualResultBytes = Files.readAllBytes(Paths.get(result));
			expectedResultBytes = Files.readAllBytes(Paths.get("model\\bank_result_interaction.xmi"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String actualResult = new String(actualResultBytes, StandardCharsets.UTF_8);
		String expectedResult = new String(expectedResultBytes, StandardCharsets.UTF_8);

		assertEquals(expectedResult, actualResult);
	
	}
	
	@Test
	void repairGraphTestInteractionConsistency() {
		ByteArrayInputStream testInput;
		String input = "n \n 2 \n";
		testInput = new ByteArrayInputStream(input.getBytes());
		System.setIn(testInput);

		File graph = new File("model\\bank.xmi");
		File constraints = new File("model\\bank_constraints.nestedconstraintmodel");
		String result = GraphRepairAlgorithm.repairGraph(graph, constraints);

		String rulePath = constraints.getAbsolutePath();
		rulePath = rulePath.replace(".nestedconstraintmodel", ".henshin");
		
		boolean expectedResult = GraphRepairAlgorithm.isConsistency(new File(result), new File(rulePath));
		assertTrue(expectedResult);
	}
	
	@Test
	void repairGraphTestInvalideInteraction() {
		ByteArrayInputStream testInput;
		String input = "n \n 12 \n";
		testInput = new ByteArrayInputStream(input.getBytes());
		System.setIn(testInput);

		File graph = new File("model\\bank.xmi");
		File constraints = new File("model\\bank_constraints.nestedconstraintmodel");
		String result = GraphRepairAlgorithm.repairGraph(graph, constraints);

		byte[] actualResultBytes = null;
		byte[] expectedResultBytes = null;
		try {
			actualResultBytes = Files.readAllBytes(Paths.get(result));
			expectedResultBytes = Files.readAllBytes(Paths.get("model\\bank_result_invalide_interaction.xmi"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String actualResult = new String(actualResultBytes, StandardCharsets.UTF_8);
		String expectedResult = new String(expectedResultBytes, StandardCharsets.UTF_8);

		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void repairGraphTestInvalideInteractionConsistency() {
		ByteArrayInputStream testInput;
		String input = "n \n 12 \n";
		testInput = new ByteArrayInputStream(input.getBytes());
		System.setIn(testInput);

		File graph = new File("model\\bank.xmi");
		File constraints = new File("model\\bank_constraints.nestedconstraintmodel");
		String result = GraphRepairAlgorithm.repairGraph(graph, constraints);
		
		String rulePath = constraints.getAbsolutePath();
		rulePath = rulePath.replace(".nestedconstraintmodel", ".henshin");
		
		boolean expectedResult = GraphRepairAlgorithm.isConsistency(new File(result), new File(rulePath));
		assertTrue(expectedResult);
	}

	@Test
	void repairGraphTestCircularConstraints() {
		ByteArrayInputStream testInput;
		String input = "y \n";
		testInput = new ByteArrayInputStream(input.getBytes());
		System.setIn(testInput);

		File graph = new File("model\\bank.xmi");
		File constraints = new File("model\\bank_circular_constraints.nestedconstraintmodel");

		Exception e = Assert.assertThrows(RuntimeException.class, () -> GraphRepairAlgorithm.repairGraph(graph, constraints));
		Assert.assertEquals(e.getMessage(), "Cycle between constraints");

	}

}
