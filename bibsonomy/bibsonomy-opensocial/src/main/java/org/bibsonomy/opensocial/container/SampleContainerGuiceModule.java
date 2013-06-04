package org.bibsonomy.opensocial.container;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

public class SampleContainerGuiceModule extends AbstractModule {
	protected void configure() {
		Multibinder handlerBinder = Multibinder.newSetBinder(binder(),
				Object.class, Names.named("org.apache.shindig.handlers"));
		handlerBinder.addBinding().toInstance(SampleContainerHandler.class);
	}
}