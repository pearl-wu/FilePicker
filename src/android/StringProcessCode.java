package com.bais.filepicker;

import java.net.URL;
import java.net.URLEncoder;

public class StringProcessCode {
	public static String toBrowserCode(String word) {
	    String HEX_STRING = "0123456789ABCDEF";
        byte[] bytes = word.getBytes();

        //非中文處理
        if (bytes.length == word.length())
            return word;

        StringBuilder browserUrl = new StringBuilder();
        String tempStr = "";

        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);

            //過濾不需要處理
            if ((int) currentChar <= 256) {

                if (tempStr.length() > 0) {
                    byte[] cBytes = tempStr.getBytes();

                    for (int j = 0; j < cBytes.length; j++) {
                        browserUrl.append('%');
                        browserUrl.append(HEX_STRING.charAt((cBytes[j] & 0xf0) >> 4));
                        browserUrl.append(HEX_STRING.charAt((cBytes[j] & 0x0f) >> 0));
                    }
                    tempStr = "";
                }

                browserUrl.append(currentChar);
            } else {
                //處理後
                tempStr += currentChar;
            }
        }
        return browserUrl.toString();
    }
	
	
	public static String ecodeUrlWithUTf8(String urlpath) {
   	 
        String decodedurl = "";
        try {
            URL url = new URL(urlpath);
            decodedurl = "http://" + url.getHost() + ":" + url.getPort();
 
            String path = url.getPath();
            String[] paths = path.split("\\/");
            String decodepath = "";
 
            for (int i = 0; i < paths.length; i++) {
                if (paths[i] != null && paths[i].length() > 0) {
 
                    char[] chars = paths[i].toCharArray();
                    String str = "";
                    for (char c : chars) {
                        String s = String.valueOf(c);
                        byte[] bs = s.getBytes("utf-8");
                        if (bs.length == 3) {
                            str = str + URLEncoder.encode(s, "utf-8");
                        } else if (bs.length == 1) {
                            String ss = getSpecialChartAscll(bs[0]);
                            if (ss != null) {
                                s = ss;
                            }
                            str = str + s;
 
                        } else {
 
                            str = str + s;
                        }
                    }
                    decodepath = decodepath + "/" + str;
 
                }
            }
            decodedurl = decodedurl + decodepath;
        } catch (Exception e) {
 
            return null;
        }
        return decodedurl;
 
    }
 
    private static String getSpecialChartAscll(byte b) {
        if (b >= 32 && b <= 41) {
            return "%" + (b - 12);
        } else {
            String s = "" + b;
            switch (b) {	  
            case 42:
                s = "%2A";
                break;
            case 43:
                s = "%2B";
                break;
            case 44:
                s = "%2C";	  
                break;
            case 45:
                s = "%2D";
                break;
            case 46:
                s = "%2E";
                break;
            case 47:
                s = "%2F";
                break;
            case 58:
                s = "%3A";
                break;
            case 59:
                s = "%3B";
                break;
            case 60:
                s = "%3CF";
                break;
            case 61:
                s = "%3D";
                break;
            case 62:
                s = "%3E";
                break;
            case 63:
                s = "%3F";
                break;
            case 64:
                s = "%40";
                break;
            default:
                return null;	  
            }
            return s;
        }	  
    }  

}
