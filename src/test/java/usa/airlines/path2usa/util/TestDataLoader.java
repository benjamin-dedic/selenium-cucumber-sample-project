package usa.airlines.path2usa.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestDataLoader {

    private static final String FILE_PATH = "/calendar-test-data.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Object[][] getTestData() {
        List<Object[]> testDataList = new ArrayList<>();

        try (InputStream input = TestDataLoader.class.getResourceAsStream(FILE_PATH)) {
            if (input != null) {
                JsonNode testDataNode = objectMapper.readTree(input);
                Iterator<JsonNode> testDataIterator = testDataNode.iterator();

                while (testDataIterator.hasNext()) {
                    JsonNode testDataItem = testDataIterator.next();
                    testDataList.add(new Object[]{
                            testDataItem.get("day").asInt(),
                            testDataItem.get("month").asText(),
                            testDataItem.get("year").asInt()
                    });
                }
            } else {
                System.err.println("Could not load test data. File not found: " + FILE_PATH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return testDataList.toArray(new Object[0][]);
    }
}
