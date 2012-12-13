package fr.liglab.adele.test.pojosr;

import java.io.File;

import org.testng.annotations.Test;

/**
 * Stupid unit testing
 * 
 * @author yo
 * 
 */
public class UnitTest {

	@Test
	public void showTarget() {
		File target = new File("target");
		File[] files = target.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
		}
	}

}
