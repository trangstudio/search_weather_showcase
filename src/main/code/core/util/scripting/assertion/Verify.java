package core.util.scripting.assertion;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Verify {
    private static final Logger LOGGER = LoggerFactory.getLogger(Verify.class);

    final static String TEST_PASS_MESSAGE_LOG_SINGLE = "<sub>PASS. Actual result matches expectation<br/></sub>%s";
    final static String TEST_FAILED_MESSAGE_LOG_SINGLE = "<sub>FAIL. Actual result does NOT match expectation<br/></sub>%s";
    final static String TEST_PASS_MESSAGE_LOG_DOUBLE = "<sub>PASS. They matched.<br/></sub>Actual: %s<br/>Expect: %s<br/>";
    final static String TEST_FAILED_MESSAGE_LOG_DOUBLE = "<sub>FAIL. They did NOT match.<br/></sub>Actual: %s<br/>Expect: %s<br/>";

    private Verify() {
    }

    /**
     * Hard Assertion
     */
    public static void hardAssertTrue(boolean condition, String message) {
        if (condition) {
            LOGGER.info(String.format(TEST_PASS_MESSAGE_LOG_SINGLE, message));
        } else {
            LOGGER.error(String.format(TEST_FAILED_MESSAGE_LOG_SINGLE, message));
        }
        Assert.assertTrue(condition);
    }

    public static void hardAssertFalse(boolean condition, String message) {
        if (!condition) {
            LOGGER.info(String.format(TEST_PASS_MESSAGE_LOG_SINGLE, message));
        } else {
            LOGGER.error(String.format(TEST_FAILED_MESSAGE_LOG_SINGLE, message));
        }
        Assert.assertFalse(condition);
    }

    public static void hardAssertEqual(Object actual, Object expected) {
        boolean status;
        if (actual instanceof String && expected instanceof String) {
            if (actual.equals(expected)) {
                actual = actual.toString().trim();
                expected = expected.toString().trim();
                status = (actual.equals(expected));
            } else {
                status = false;
            }
            if (status) {
                LOGGER.info(String.format(TEST_PASS_MESSAGE_LOG_DOUBLE, actual, expected));
            } else {
                LOGGER.error(String.format(TEST_FAILED_MESSAGE_LOG_DOUBLE, actual, expected));
            }
        }
        Assert.assertEquals(actual, expected);
    }

    /**
     * Soft Assertion for input condition expected to be true, logged with provided messages
     *
     * @param condition the condition to verify, e.g ("01".equals("01"))
     * @param message   the message to log, e.g. "1 is equal to 01"
     */
    public static boolean softAssertTrue(boolean condition, String message) {
        boolean pass = true;
        try {
            if (condition)
                LOGGER.info(String.format(TEST_PASS_MESSAGE_LOG_SINGLE, message));
            else
                LOGGER.error(String.format(TEST_FAILED_MESSAGE_LOG_SINGLE, message));
            Assert.assertTrue(condition);
        } catch (Throwable e) {
            pass = false;
//            TODO Collect test report here
//            LOGGER.error(String.valueOf(Reporter.getCurrentTestResult()), e);
//            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }


    /**
     * Soft Assertion for input condition expected to be false, logged with provided messages
     *
     * @param condition the condition to verify, e.g ("1".equals("01"))
     * @param message   the message to log, e.g. "1 is NOT equal to 01 in String comparision"
     */
    public static boolean softAssertFalse(boolean condition, String message) {
        boolean pass = true;
        try {
            if (!condition)
                LOGGER.info(String.format(TEST_PASS_MESSAGE_LOG_SINGLE, message));
            else
                LOGGER.error(String.format(TEST_FAILED_MESSAGE_LOG_SINGLE, message));
            Assert.assertFalse(condition);
        } catch (Throwable e) {
            pass = false;
            //            TODO Collect test report here

//            LOGGER.error(String.valueOf(Reporter.getCurrentTestResult()), e);
//            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    public static boolean softAssertEqual(Object actual, Object expected) {
        boolean pass = true;
        boolean status;
        try {
            if (actual instanceof String && expected instanceof String) {
                actual = actual.toString().trim();
                expected = expected.toString().trim();
                status = (actual.equals(expected));
            } else {
                status = (actual.equals(expected));
            }
            if (status) {
                LOGGER.info(String.format(TEST_PASS_MESSAGE_LOG_DOUBLE, actual, expected));
            } else {
                LOGGER.error(String.format(TEST_FAILED_MESSAGE_LOG_DOUBLE, actual, expected));
            }
            Assert.assertEquals(actual, expected);
        } catch (Throwable e) {
            pass = false;
            //            TODO Collect test report here
//            LOGGER.error(String.valueOf(Reporter.getCurrentTestResult()), e);
//            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }
}
