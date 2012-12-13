package test.example;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;


@Component
@Provides
@Instantiate
public class HelloImpl implements Hello {
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
