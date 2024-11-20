package gov.hhs.gsrs.products;

import gov.hhs.gsrs.products.product.tasks.DataDirMonitorTask;
import gov.nih.ncats.common.stream.StreamUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataDirMonitorTaskTests {

    @Test
    void getFileNameTest() {
        String fullFilePath = "D:\\temp\\product_data\\23dbde51-ded9-505e-e063-6294a90a46aa.xml";
        String expected = "23dbde51-ded9-505e-e063-6294a90a46aa.xml";
        String fileName= DataDirMonitorTask.getFileName(fullFilePath);
        Assertions.assertEquals(expected, fileName);
    }

    @Test
    void testSubstitution() {
        String fullFilePath = "D:\\temp\\product_data\\23dbde51-ded9-505e-e063-6294a90a46aa.xml";
        String result = fullFilePath.replace("\\", "\\\\");
        int totalBackSlash = 0;
        for(char c : result.toCharArray() ) {
            if(c == '\\') totalBackSlash++;
        }
        Assertions.assertEquals(6, totalBackSlash);
    }
}
