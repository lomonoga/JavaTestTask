package lmg;

import lmg.Exceptions.EmptyFileException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class Test {
    String path = "src/test/java/lmg/filesForTests";
    @Test
    void testDefaultInputFile() throws Exception {
        var expect = 9;
        var actual = new Solution().determineNumberOfRequests(Boolean.TRUE, path, "input.txt");
        assertEquals(expect, actual);
    }

    @Test
    void testNotExistenceInputFile() {
        assertThrows(FileNotFoundException.class,
                () -> new Solution().determineNumberOfRequests(Boolean.TRUE, path, "_input.txt"));
    }

    @Test
    void testFileIsEmpty() {
        assertThrows(EmptyFileException.class,
                () -> new Solution().determineNumberOfRequests(Boolean.TRUE, path, "input_empty.txt"));
    }

    @Test
    void testAllRequestsAreSame() throws Exception {
        var expect = 1;
        var actual = new Solution().determineNumberOfRequests(Boolean.TRUE, path, "input_all_1.txt");
        assertEquals(expect, actual);
    }

    @Test
    void testCashIsNotExistence() throws Exception {
        var expect = 9;
        var actual = new Solution().determineNumberOfRequests(Boolean.TRUE, path, "input_cash_0.txt");
        assertEquals(expect, actual);
    }

    @Test
    void testOneDifferent() throws Exception {
        var expect = 2;
        var actual = new Solution().determineNumberOfRequests(Boolean.TRUE, path, "input_one_different.txt");
        assertEquals(expect, actual);
    }

    @Test
    void testInputFile0() throws Exception {
        var expect = 5;
        var actual = new Solution().determineNumberOfRequests(Boolean.TRUE, path, "input_0.txt");
        assertEquals(expect, actual);
    }

    @Test
    void testInputFile1() throws Exception {
        var expect = 4;
        var actual = new Solution().determineNumberOfRequests(Boolean.TRUE, path, "input_1.txt");
        assertEquals(expect, actual);
    }

    @Test
    void testInputCash1_V1() throws Exception {
        var expect = 2;
        var actual = new Solution().determineNumberOfRequests(Boolean.TRUE, path, "input_cash_1.txt");
        assertEquals(expect, actual);
    }

    @Test
    void testInputCash1_V2() throws Exception {
        var expect = 4;
        var actual = new Solution().determineNumberOfRequests(Boolean.TRUE, path, "input_cash_1_V2.txt");
        assertEquals(expect, actual);
    }
}
