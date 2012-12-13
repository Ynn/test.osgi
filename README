  #Common functionality for PojoSR-based tests.
  
  
  ##Usage
  Extend AbstractOSGiTestCase to ease the writing of POJOSR testng tests. See
  pojosr-example for an example of use.
  
  ##Configuration
  You should have a properly configured pom to use this. Configuration implies
  :
  +importing test-pojosr as a test dependency
  +importing each wanted bundles as a test dependency
  +properly configuring the surefire and failsafe plugins (just copy the
  pom given in the example)
  
  ##Customizations :
  
  +setUp and tearDown : just make sure you put the duplicate the annotations
  when overriding.
  +delayBetweenTestInMs : delay before each test
  +ignoredBundlesURLPatterns: A pattern used to filter bundles URL (each
  bundle's URL is tested against this pattern)
  +ignoredBundlesSymbolicNamePatterns : A pattern used to filter bundles
  symbolic name (each bundle's name is tested against this pattern)
  
  ##Limitations :
  
  +This project is designed for light and small tests. Using pojosr in a
  highly multi-threaded environment is unforseable.
  +You should not consider that all bundle have been started when the first
  test is runned. This is why we use a delay between tests.
  +POJO-SR use a unique classloader. Pay attention to versions and
  overlapping names.
  
  
