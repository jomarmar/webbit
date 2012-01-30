package org.webbitserver.helpers;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.webbitserver.helpers.Hex.fromHex;
import static org.webbitserver.helpers.Hex.toHex;

public class UTF8OutputTest {
    private UTF8Output utf8Output = new UTF8Output();

    @Test
    public void decodesLatin() throws IOException {
        assertUtf8("Hellesøy");
    }

    @Test
    public void decodesChinese() throws IOException {
        assertUtf8("我希望有人告诉我数字相加的结果");
    }

    @Test
    public void decodesManyTimes() throws IOException {
        assertUtf8("Hellesøy 我希望有人告诉我数字相加的结果");
        assertUtf8("Hellesøy");
        assertUtf8("我希望有人告诉我数字相加的结果");
    }

    @Test
    public void acceptsCompleteUtf8Char() throws UnsupportedEncodingException {
        assertUtf8(fromHex("ceba"));
    }

    @Test(expected = UTF8Exception.class)
    public void throwsOnIncompleteUtf8Char() throws UnsupportedEncodingException {
        utf8Output.write(fromHex("ce"));
        utf8Output.getStringAndRecycle();
    }

    @Test
    public void autobahn_6_22_1() throws UnsupportedEncodingException {
        assertUtf8(fromHex("efbfbe"));
    }

    @Test
    public void autobahn_6_22_2() throws UnsupportedEncodingException {
        assertUtf8(fromHex("efbfbf"));
    }

    @Test
    public void autobahn_6_22_3() throws UnsupportedEncodingException {
        assertUtf8(fromHex("f09fbfbe"));
    }

    @Test(expected = UTF8Exception.class)
    public void throwsOnNonUTF8Bytes() {
        utf8Output.write(fromHex("CEBAE1BDB9CF83CEBCCEB5EDA080656469746564"));
    }

    private void assertUtf8(byte[] bytes) throws UnsupportedEncodingException {
        utf8Output.write(bytes);
        assertEquals(new String(bytes, "UTF-8"), utf8Output.getStringAndRecycle());
    }

    private void assertUtf8(String s) throws IOException {
        utf8Output.write(s.getBytes("UTF-8"));
        assertEquals(s, utf8Output.getStringAndRecycle());
    }
}
