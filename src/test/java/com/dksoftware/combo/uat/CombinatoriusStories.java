/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo.uat;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.web.selenium.WebDriverHtmlOutput.WEB_DRIVER_HTML;

import java.util.List;
import java.util.concurrent.Executors;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.PendingStepStrategy;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.web.selenium.ContextView;
import org.jbehave.web.selenium.LocalFrameContextView;
import org.jbehave.web.selenium.PerStoriesWebDriverSteps;
import org.jbehave.web.selenium.PropertyWebDriverProvider;
import org.jbehave.web.selenium.SeleniumConfiguration;
import org.jbehave.web.selenium.SeleniumContext;
import org.jbehave.web.selenium.SeleniumContextOutput;
import org.jbehave.web.selenium.SeleniumStepMonitor;
import org.jbehave.web.selenium.WebDriverProvider;
import org.jbehave.web.selenium.WebDriverScreenshotOnFailure;
import org.jbehave.web.selenium.WebDriverSteps;

public class CombinatoriusStories extends JUnitStories {

	private WebDriverProvider driverProvider = new PropertyWebDriverProvider();
	private WebDriverSteps lifecycleSteps = new LifecycleSteps(driverProvider);
	private PageFactory pageFactory = new PageFactory(driverProvider);
	private PendingStepStrategy pendingStepStrategy = new FailingUponPendingStep();
	private CrossReference crossReference = new CrossReference().withJsonOnly().withPendingStepStrategy(pendingStepStrategy) .withOutputAfterEachStory(true).excludingStoriesWithNoExecutedScenarios(true);
	private ContextView contextView = new LocalFrameContextView().sized(640, 80);
	private SeleniumContext seleniumContext = new SeleniumContext();
	private SeleniumStepMonitor stepMonitor = new SeleniumStepMonitor(contextView, seleniumContext, new SilentStepMonitor());
	private Format[] formats = new Format[] { new SeleniumContextOutput(seleniumContext), CONSOLE, WEB_DRIVER_HTML };
	private StoryReporterBuilder reporterBuilder = new StoryReporterBuilder()
			.withCodeLocation(codeLocationFromClass(CombinatoriusStories.class))
			.withFailureTrace(true)
			.withFailureTraceCompression(true)
			.withDefaultFormats()
			.withFormats(formats)
			.withCrossReference(crossReference);

	public CombinatoriusStories() {
		// If configuring lifecycle per-stories, you need to ensure that you a same-thread executor
		if (lifecycleSteps instanceof PerStoriesWebDriverSteps) {
			configuredEmbedder().useMetaFilters(asList("-skip"));
			configuredEmbedder().useExecutorService(Executors.newSingleThreadExecutor());
		}
	}

	@Override
	public Configuration configuration() {
		return new SeleniumConfiguration()
				.useSeleniumContext(seleniumContext)
				.useWebDriverProvider(driverProvider)
				.usePendingStepStrategy(pendingStepStrategy)
				.useStoryControls(new StoryControls().doResetStateBeforeScenario(false))
				.useStepMonitor(stepMonitor)
				.useStoryLoader(new LoadFromClasspath(this.getClass()))
				.useStoryReporterBuilder(reporterBuilder);
	}

	@Override
	public InjectableStepsFactory stepsFactory() {
		Configuration configuration = configuration();
		return new InstanceStepsFactory(configuration, new CombinatoriusSteps(pageFactory), lifecycleSteps,
				new WebDriverScreenshotOnFailure(driverProvider, configuration.storyReporterBuilder()));
	}

	@Override
	protected List<String> storyPaths() {
		return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()).getFile(),
				asList("**/" + System.getProperty("storyFilter", "*") + ".story"), null);
	}
	
	static class SameThreadEmbedder extends Embedder {
		public SameThreadEmbedder() {
			useExecutorService(Executors.newSingleThreadExecutor());
		}
	}
}
