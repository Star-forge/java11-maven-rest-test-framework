package report;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author starforge
 */
public class TestLifecycle implements TestWatcher, AfterAllCallback, BeforeAllCallback, BeforeTestExecutionCallback {
    private static final Logger log = LoggerFactory.getLogger("test");
    private static final List<TestResultStatus> testAllResultsStatus = new ArrayList<>();
    private static int testCounter;
    private final List<TestResultStatus> testSuiteResultsStatus = new ArrayList<>();

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        log.info("ЗАПУЩЕН ТЕСТ №{}: [{}] с тегами {}", ++testCounter, context.getDisplayName(), context.getTags());
        log.debug("Test location = {}#{}", context.getRequiredTestClass(), context.getRequiredTestMethod());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String message = reason.orElse("Причина не укзана");
        log.info("ОТКЛЮЧЕН ТЕСТ №{}: [{}] ПО ПРИЧИНЕ: {}", testCounter, context.getDisplayName(), message);
        testSuiteResultsStatus.add(TestResultStatus.DISABLED);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("ЗАВЕРШЕН УСПЕШНО ТЕСТ №{}: [{}] \n", testCounter, context.getDisplayName());
        testSuiteResultsStatus.add(TestResultStatus.SUCCESSFUL);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log.info("ПРЕРВАН ТЕСТ №{}: [{}] ", testCounter, context.getDisplayName());
        testSuiteResultsStatus.add(TestResultStatus.ABORTED);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log.info("ПРОВАЛЕН ТЕСТ №{}: [{}] ", testCounter, context.getDisplayName());
        testSuiteResultsStatus.add(TestResultStatus.FAILED);
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        log.info("ЗАПУЩЕН ТЕСТОВЫЙ НАБОР: [{}]\n", context.getDisplayName());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        Map<TestResultStatus, Long> summary = testSuiteResultsStatus.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        log.info("РЕЗУЛЬТАТЫ ВЫПОЛНЕНИЯ ТЕСТОВОГО НАБОРА [{}] : {}", context.getDisplayName(), summary);

        testAllResultsStatus.addAll(testSuiteResultsStatus);
        Map<TestResultStatus, Long> allSummary = testAllResultsStatus.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        log.info("РЕЗУЛЬТАТЫ ВЫПОЛНЕНИЯ ВСЕХ ТЕСТОВ (В ЭТОЙ JVM): {}\n\n", allSummary);
    }

    private enum TestResultStatus {
        SUCCESSFUL, ABORTED, FAILED, DISABLED
    }
}
