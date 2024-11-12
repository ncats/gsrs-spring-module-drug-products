package gov.hhs.gsrs.products.utils;

import gov.hhs.gsrs.products.product.utils.ShellCommandRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ShellCommandRunnerTests {
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
}
