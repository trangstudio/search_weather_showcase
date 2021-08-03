package core.util.reporting.listener;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class NABListener extends RunListener {
    protected static final Logger LOGGER = LoggerFactory.getLogger(NABListener.class);
    //Start and End time of the tests
    long startTime;
    long endTime;

    @Override
    public void testRunStarted(Description description) throws Exception {
        //Start time of the tests
        startTime = new Date().getTime();
        //Print the number of tests before the all tests execution.
        LOGGER.info("Tests started! Number of Test case: " + description.testCount() + "\n");
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        //End time of the tests
        endTime = new Date().getTime();
        //Print the below lines when all tests are finished.
        LOGGER.info("Tests finished! Number of test case: " + result.getRunCount());
        long elapsedSeconds = (endTime-startTime)/1000;
        LOGGER.info("Elapsed time of tests execution: " + elapsedSeconds +" seconds");
    }

    @Override
    public void testStarted(Description description) throws Exception {
        //Write the test name when it is started.
        LOGGER.info(description.getMethodName() + " test is starting...");
    }

    @Override
    public void testFinished(Description description) throws Exception {
        //Write the test name when it is finished.
        LOGGER.info(description.getMethodName() + " test is finished...\n");
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        //Write the test name when it is failed.
        LOGGER.info(failure.getDescription().getMethodName() + " test FAILED!!!");
    }

    //O.B: IntelliJ ignored by default. I did not succeed to run this method.
    //If you know any way to accomplish this, please write a comment.
    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
        Ignore ignore = description.getAnnotation(Ignore.class);
        String ignoreMessage = String.format(
                "@Ignore test method '%s()': '%s'",
                description.getMethodName(), ignore.value());
        LOGGER.info(ignoreMessage + "\n");
    }

}
