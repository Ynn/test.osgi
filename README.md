#Common functionality for PojoSR-based tests.


##Usage
Extend AbstractOSGiTestCase to ease the writing of POJOSR testng tests. See
pojosr-example for an example of use.

##Configuration 

### Maven 
You should have a properly configured pom to use this. Configuration implies
:
+ importing test-pojosr as a test dependency
+ importing each wanted bundles as a test dependency
+ properly configuring the surefire and failsafe plugins (just copy the
pom given in the example)



```xml
<dependencies>

  <!-- Use POJOSR to test -->
  <dependency>
    <groupId>fr.liglab.adele.common</groupId>
    <version>1.0.0-SNAPSHOT</version>
    <artifactId>test-pojosr</artifactId>
    <scope>test</scope>
  </dependency>
  
  <dependency>
    <groupId>the.project.i.want.to.deploy.as.a.bundle</groupId>
    <artifactId>in-pojosr</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>test</scope>
  </dependency>

[...]

	<!-- TEST -->
	<!-- *** Surefire plugin: run unit and exclude integration tests *** -->
	<plugin>
		<artifactId>maven-surefire-plugin</artifactId>
		<configuration>
			<!-- All Integration tests must be ignored during the test phases and 
				be postponed to the integration-test phases -->
			<excludes>
				<!-- Integration tests begin with IT -->
				<exclude>**/IT*.java</exclude>
			</excludes>
		</configuration>
	</plugin>

	<!-- *** Failsafe plugin: run integration tests *** -->
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-failsafe-plugin</artifactId>
		<version>2.12.4</version>
		<configuration>
			<additionalClasspathElements>
				<!-- Get the bundle and add it to the classpath (it will be discovered 
					by pojosr) -->
				<additionalClasspathElement>${build.directory}/${build.finalName}.jar</additionalClasspathElement>
			</additionalClasspathElements>
			<!-- /!\ hack to avoid the compiled non-manipulated classes of the bundle 
				to be in the classpath (they would have precedence over the bundle otherwise) -->
			<classesDirectory>${build.directory}/null</classesDirectory>
		</configuration>
		<executions>
			<execution>
				<goals>
					<goal>integration-test</goal>
					<goal>verify</goal>
				</goals>
			</execution>
		</executions>
	</plugin>
</plugins>
</build>
```



### Abstract class :
Some value of the 
+ setUp and tearDown : just make sure you put the duplicate the annotations
when overriding.
+ delayBetweenTestInMs : delay before each test
+ ignoredBundlesURLPatterns: A pattern used to filter bundles URL (each
bundle's URL is tested against this pattern)
+ ignoredBundlesSymbolicNamePatterns : A pattern used to filter bundles
symbolic name (each bundle's name is tested against this pattern)


```java
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
```


##Limitations :

+ This project is designed for light and small tests. Using pojosr in a
highly multi-threaded environment is unforseable.
+ You should not consider that all bundle have been started when the first
test is runned. This is why we use a delay between tests.
+ POJO-SR use a unique classloader. Pay attention to versions and
overlapping names.


