package com.example.btth04_vnexpress;

public class XMLParser {
    public String getXmlFromUrl(String url) {
        String xml = null;
        try {
// Tạo đối tượng Client và tạo Http Connetction
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) { e.printStackTrace();
        }
// return XML return xml;
    }