package ntu.wg.cq.ibook.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字符串操作模块
 * Created by C_Q on 2018/1/28.
 */
public class StringUtils {
    /**
     * 输入流转字符串
     *
     * @param inputStream
     * @return
     */
    public static String getResponseResult(InputStream inputStream) {
        String s = "";
        if (null != inputStream) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String str = "";
            try {
                while (null != (str = bufferedReader.readLine())) {
                    stringBuffer.append(str);
                }
                s = stringBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                s = "{\"state\":0,\"message\":\"资源解析异常\"}";
            }
        }
        return s;
    }

    /**
     * 构造请求数据
     *
     * @param params:请求参数
     * @return
     */
    public static String getRequestData(Map<String, String> params) {
        String data;
        if (params.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        data = sb.toString();
        return data;
    }

    /**
     * 判断一组字符串是否有空值
     *
     * @param s
     * @return true 有 false 没有
     */
    public static boolean haveEmpty(String... s) {
        int n = 0;
        for (String v : s) {
            if (null == v || "".equals(v)) {
                n++;
                break;
            }
        }
        if (n == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 字符串分页
     *
     * @param txt   章节内容
     * @param lines 没页行数
     * @param count 每行字数
     * @return 每页内容
     */
    public static List<String> paging(StringBuffer txt, int lines, int count) {
        int l = 0; //行数计数器
        int c = 0; // 字数计数器
        List<String> list = new ArrayList<>();
        String s = "";
        for (int p = 0; p < txt.length(); p++) {
            if ('\n' != txt.charAt(p)) {
                if('\t' == txt.charAt(p)){
                    s += txt.charAt(p);
                }else {
                    s += txt.charAt(p);
                    c++;
                    if (c == count) {
                        s += "\n";
                        c=0;
                        l++;
                        if (l == lines) {
                            list.add(s);
                            c=0;
                            l=0;
                            s = "";
                        }
                    }
                }
            } else {
                s += txt.charAt(p);
                c=0;
                l++;
                if (l == lines) {
                    list.add(s);
                    c=0;
                    l=0;
                    s = "";
                }
            }
        }
        if(!haveEmpty(s)){
            list.add(s);
        }
        return list;
    }

    public static String str2MD5(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = str.getBytes();
            byte[] buffer = md.digest(input);
            return toHex(buffer);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String toHex(byte[] bytes){
        char[] HEX_TABLE="0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder(bytes.length*2);
        for (int i=0;i<bytes.length;i++){
            sb.append(HEX_TABLE[(bytes[i]>>4)&0x0F]);
            sb.append(HEX_TABLE[bytes[i]&0x0F]);
        }
        return sb.toString();
    }

    public static String urlEncoding(String s){
        try {
            return URLEncoder.encode(s,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String urlDecode(String s){
        try {
            return URLDecoder.decode(s,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}