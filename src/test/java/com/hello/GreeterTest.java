package com.hello;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class GreeterTest {
	private Greeter greeter = new Greeter();

	@Test
	public void greeterSaysHello() {
		assertTrue(greeter.sayHello().contains("Hello"));
	}

}
