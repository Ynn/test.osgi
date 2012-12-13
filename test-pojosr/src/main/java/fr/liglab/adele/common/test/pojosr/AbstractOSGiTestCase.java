/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.liglab.adele.common.test.pojosr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.UUID;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import de.kalpatec.pojosr.framework.launch.BundleDescriptor;
import de.kalpatec.pojosr.framework.launch.ClasspathScanner;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistry;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistryFactory;

/**
 * Common functionality for PojoSR-based tests.
 * 
 * <p>
 * Extend this class to ease the writing of POJOSR testng tests. See
 * pojosr-example for an example of use.
 * </p>
 * 
 * <p>
 * You should have a properly configured pom to use this. Configuration implies
 * :
 * <ul>
 * <li>importing test-pojosr as a test dependency</li>
 * <li>importing each wanted bundles as a test dependency</li>
 * <li><properly configuring the surefire and failsafe plugins (just copy the
 * pom given in the example)</li>
 * </ul>
 * 
 * </p>
 * 
 * <p>
 * Customizations :
 * <ul>
 * <li>setUp and tearDown : just make sure you put the duplicate the annotations
 * when overriding.</li>
 * <li>delayBetweenTestInMs : delay before each test</li>
 * <li>ignoredBundlesURLPatterns: A pattern used to filter bundles URL (each
 * bundle's URL is tested against this pattern)</li>
 * <li>ignoredBundlesSymbolicNamePatterns : A pattern used to filter bundles
 * symbolic name (each bundle's name is tested against this pattern)
 * <li>
 * </ul>
 * </ul>
 * 
 * <p>
 * Limitations :
 * <ul>
 * <li>This project is designed for light and small tests. Using pojosr in a
 * highly multi-threaded environment is unforseable.</li>
 * <li>You should not consider that all bundle have been started when the first
 * test is runned. This is why we use a delay between tests.</li>
 * <li>POJO-SR use a unique classloader. Pay attention to versions and
 * overlapping names.</li>
 * </ul>
 * </p>
 * 
 * @author yoann.maurel@imag.fr
 */
public abstract class AbstractOSGiTestCase {

	/**
	 * The registry used to register services
	 */
	protected PojoServiceRegistry registry;

	/**
	 * The framework bundle Context
	 */
	protected BundleContext context;

	/**
	 * A pattern used to filter bundles URL (each bundle's URL is tested against
	 * this pattern)
	 */
	protected String ignoredBundlesURLPatterns = ".*jcommander.*|.*testng.*|.*snakeyaml.*|.*beanshell.*|.*surefire.*|.*junit.*|.*asm.*";

	/**
	 * A pattern used to filter bundles symbolic name (each bundle's name is
	 * tested against this pattern)
	 */
	protected String ignoredBundlesSymbolicNamePatterns = "";

	/**
	 * Delay to wait between each test (can be customized by overriding the
	 * setUp() method.
	 */
	protected long delayBetweenTestInMs = 200;

	/**
	 * This property is used by POJOSR to know where to store the cache
	 */
	private static final String OSGI_FRAMEWORK_STORAGE = "org.osgi.framework.storage";

	/**
	 * The default build dir of the project (maven use target by default)
	 */
	private static final String BUILD_DIR = "target";

	@BeforeMethod
	public void setUp() throws Exception {
		configureJAVAProperties();

		// Initialize service registry
		ServiceLoader<PojoServiceRegistryFactory> loader = ServiceLoader
				.load(PojoServiceRegistryFactory.class);

		registry = loader.iterator().next()
				.newPojoServiceRegistry(getOSGiProperties());

		context = registry.getBundleContext();
		Thread.sleep(delayBetweenTestInMs);
	}

	@AfterMethod
	public void tearDown() throws Exception {
		context.getBundle().stop();
	}

	protected void configureJAVAProperties() {
		UUID randomUUID = UUID.randomUUID();
		System.setProperty(AbstractOSGiTestCase.OSGI_FRAMEWORK_STORAGE,
				AbstractOSGiTestCase.BUILD_DIR + "/osgi-cache/" + randomUUID);

	}

	protected List<BundleDescriptor> getBundleList() throws Exception {
		List<BundleDescriptor> bundleDescriptors = new ClasspathScanner()
				.scanForBundles();
		for (Iterator<BundleDescriptor> iterator = bundleDescriptors.iterator(); iterator
				.hasNext();) {
			BundleDescriptor bundleDescriptor = iterator.next();
			String bundleURL = bundleDescriptor.getUrl().toString();
			String bundleSymbolicName = bundleDescriptor.getHeaders().get(
					Constants.BUNDLE_SYMBOLICNAME);

			if (((bundleURL != null) && bundleURL
					.matches(ignoredBundlesURLPatterns))
					|| ((bundleSymbolicName != null) && bundleSymbolicName
							.matches(ignoredBundlesSymbolicNamePatterns))) {
				iterator.remove();
			}

		}
		return bundleDescriptors;
	}

	protected Map<String, Object> getOSGiProperties() throws Exception {
		Map<String, Object> config = new HashMap<String, Object>();
		config.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS,
				getBundleList());
		return config;
	}

}