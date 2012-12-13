package fr.liglab.adele.test.pojosr;

import java.io.File;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import test.example.Hello;
import de.kalpatec.pojosr.framework.launch.ClasspathScanner;
import fr.liglab.adele.common.test.pojosr.AbstractOSGiTestCase;

/**
 * Test performed during the integration-test phases. Pay attention to the IT*
 * name.
 * 
 * @author yo
 * 
 */
public class ITTest extends AbstractOSGiTestCase {

	/**
	 * Customization of the set up Pay attention to the annotation :
	 */
	@Override
	@BeforeMethod
	public void setUp() throws Exception {
		System.out.println("==========================TEST SETUP");

		// add new ignored bundles to the list (i.e ipojo useless bundles)
		ignoredBundlesURLPatterns = ignoredBundlesURLPatterns
				+ "|.*ipojo\\.manipulator.*|.*ipojo\\.annotations.*";
		delayBetweenTestInMs = 200;

		// call the parent (!! don't forget that)
		super.setUp();
	}

	@Test(timeOut = 2000)
	public void showTarget() {
		File target = new File("target");
		File[] files = target.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
		}
	}

	@Test(timeOut = 2000)
	public void testOSGi() throws Exception {
		System.out.println(new ClasspathScanner().scanForBundles());

		context.getBundle();
		Bundle[] bundles = context.getBundles();
		for (Bundle bundle : bundles) {
			System.out.println(bundle.getBundleId() + " "
					+ bundle.getSymbolicName() + " " + bundle.getLocation());
		}

		System.out.println("OSGi is Running ?");
	}

	@Test(timeOut = 2000)
	public void testShowServices() throws InterruptedException {
		context.getBundle();
		Bundle[] bundles = context.getBundles();
		for (Bundle bundle : bundles) {
			System.out.println(bundle.getBundleId() + "========== "
					+ bundle.getSymbolicName() + "==============>");

			ServiceReference<?>[] refs = bundle.getRegisteredServices();
			if (refs != null) {
				System.out.println("There are " + refs.length
						+ " provided services");
				for (ServiceReference<?> serviceReference : refs) {
					for (String key : serviceReference.getPropertyKeys()) {
						System.out.println("\t" + key + " = "
								+ serviceReference.getProperty(key));
					}
					System.out.println("-----");
				}
			}
		}
	}

	@Test(timeOut = 2000)
	public void callService() throws InvalidSyntaxException,
			InterruptedException {
		System.out.println("=============================");

		ServiceTracker tracker = new ServiceTracker(context,
				context.createFilter("(objectClass=" + Hello.class.getName()
						+ ")"), null);
		tracker.open();

		System.out.println("Hello services count = "
				+ tracker.getTrackingCount());

		Hello hello = (Hello) tracker.waitForService(1500);
		System.out.println(hello.sayHello("World"));

		tracker.close();

		System.out.println("=============================");
	}

	@Test
	public void info() throws Exception {
		System.out.println("----- POJOSR PROPERTIES --------");
		Map<String, Object> config = getOSGiProperties();
		for (Map.Entry<String, Object> entry : config.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}

		System.out.println("----- JAVA PROPERTIES --------");
		String[] keys = new String[] { "org.osgi.framework.storage" };
		for (String key : keys) {
			System.out.println(key + "=" + System.getProperty(key));
		}

	}

}
