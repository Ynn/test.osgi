package fr.liglab.adele.test.pojosr;

import java.io.File;

import org.testng.annotations.Test;

import fr.liglab.adele.common.test.pojosr.AbstractOSGiTestCase;



public class UnitTest extends AbstractOSGiTestCase {

	@Test
	public void showTarget() {
		File target = new File("target");
		File[] files = target.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
		}
	}


}
