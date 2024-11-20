package gov.hhs.gsrs.products.utils;

import gov.hhs.gsrs.products.TestConfiguration;
import gov.hhs.gsrs.products.product.utils.ShellCommandRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = TestConfiguration.class)
@TestPropertySource("classpath:application-test.properties")
public class ShellCommandRunnerTests {

    @Autowired
    private TestConfiguration config;

    @Test
    void test1() throws IOException, InterruptedException {
        StringBuilder resultBuilder = new StringBuilder();
        String scriptPath =ShellCommandRunner.isWindows() ? "basicTest.bat" : "basicTest.sh";
        File runnableFile = new ClassPathResource(scriptPath).getFile();
        String input = "world";
        File catchDataFile = File.createTempFile("testOutput", "txt");
        System.out.printf("catchDataFile: %s\n", catchDataFile.getAbsolutePath());
        String expectedOutput = "input: " + input;
        ShellCommandRunner.Monitor monitor=(new ShellCommandRunner.Builder())
                .command(runnableFile.getAbsolutePath(), input, catchDataFile.getAbsolutePath() )
                .activeDir(System.getProperty("user.dir"))
                .build()
                .run()
                .onInput(i -> resultBuilder.append(i));

        long allowForCatchup = 20;
        Thread.sleep(allowForCatchup);
        List<String> fileData =Files.readAllLines(catchDataFile.toPath());
        Assertions.assertEquals(expectedOutput, fileData.get(0).trim());
    }

    @Test
    void callPython() throws IOException, InterruptedException {
        File tempTextFile =File.createTempFile("testOutput", "txt");
        String fileText = UUID.randomUUID().toString() + " text";

        String pythonPath = config.getPythonExecutablePath();
        String scriptPath ="scripts/writetext.py";
        File scriptFile = new ClassPathResource(scriptPath).getFile();

        System.out.printf("writing text to temporary file %s\n", tempTextFile.getAbsolutePath());
        ShellCommandRunner.Monitor monitor=(new ShellCommandRunner.Builder())
                .command(pythonPath, scriptFile.getAbsolutePath(), tempTextFile.getAbsolutePath(), fileText )
                .activeDir(System.getProperty("user.dir"))
                .build()
                .run();

        long allowForCatchup = 60;
        Thread.sleep(allowForCatchup);
        List<String> fileData =Files.readAllLines(tempTextFile.toPath());
        Assertions.assertEquals(fileText, fileData.get(0).trim());
    }
}
