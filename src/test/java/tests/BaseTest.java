package tests;

import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import report.TestLifecycle;

/**
 * @author starforge
 */
@ExtendWith(TestLifecycle.class)
public abstract class BaseTest {
    protected static Logger mainLog = LoggerFactory.getLogger("main");

    @Step("{0}")
    public static void step(String title, Runnable code) {
        mainLog.info(title);
        code.run();
    }

    @Step("{0}")
    public static void subStep(String title, Runnable code) {
        mainLog.info("     " + title);
        code.run();
    }

    @Step("{0}")
    public static void subSubStep(String title, Runnable code) {
        mainLog.info("          " + title);
        code.run();
    }

    @Step("{0}")
    public void step(String title) {
        mainLog.info(title);
    }
}
