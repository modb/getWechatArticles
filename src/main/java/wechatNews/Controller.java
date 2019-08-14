package wechatNews;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author steven
 * @date 2018/11/7
 * @desc
 */
public class Controller {
    public static void main(String[] args) throws Exception {

        // get token
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + args[0] + "&secret=" + args[1]);
        CloseableHttpResponse responseToken = httpclient.execute(httpGet);
        try {
            System.out.println(responseToken.getStatusLine());
            HttpEntity entityToken = responseToken.getEntity();
            JSON tkjson = JSON.parseObject(EntityUtils.toString(entityToken));
            String tkurl = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + ((com.alibaba.fastjson.JSONObject) tkjson).getString("access_token");
            EntityUtils.consume(entityToken);

            //get news
            HttpPost httpPost = new HttpPost(tkurl);
            String nvps = "{\"type\": \"news\",\"offset\": " + args[2] + ",\"count\": " + args[3] + "}";
            httpPost.setEntity(new StringEntity(nvps, Charset.forName("utf-8")));
            httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            CloseableHttpResponse responseNews = httpclient.execute(httpPost);
            try {
                HttpEntity entityNews = responseNews.getEntity();
                JSON newsjson = JSON.parseObject(EntityUtils.toString(entityNews, "utf-8"));
                Integer item_count = Integer.valueOf(((com.alibaba.fastjson.JSONObject) newsjson).getInteger("item_count"));
                List<WechatNews> wechatNewsList = new ArrayList<>();
                //i为item数，j为item下的文章数
                for (int i = 0; i < item_count; i++) {
                    Long update_time = ((com.alibaba.fastjson.JSONObject) newsjson).getJSONArray("item").getJSONObject(i).getLong("update_time");
                    Integer news_count = ((com.alibaba.fastjson.JSONObject) newsjson).getJSONArray("item").getJSONObject(i).getJSONObject("content").getJSONArray("news_item").size();
                    for (int j = 0; j < news_count; j++) {
                        WechatNews wechatNews = new WechatNews();
                        wechatNews.setCreatedTime(new Date(update_time * 1000));
                        wechatNews.setTitle(String.valueOf(((com.alibaba.fastjson.JSONObject) newsjson).getJSONArray("item").getJSONObject(i).getJSONObject("content").getJSONArray("news_item").getJSONObject(j).get("title")));
                        wechatNews.setSource(String.valueOf(((com.alibaba.fastjson.JSONObject) newsjson).getJSONArray("item").getJSONObject(i).getJSONObject("content").getJSONArray("news_item").getJSONObject(j).get("author")));
                        wechatNews.setUrl(String.valueOf(((com.alibaba.fastjson.JSONObject) newsjson).getJSONArray("item").getJSONObject(i).getJSONObject("content").getJSONArray("news_item").getJSONObject(j).get("url")));
                        wechatNews.setImageUrl(String.valueOf(((com.alibaba.fastjson.JSONObject) newsjson).getJSONArray("item").getJSONObject(i).getJSONObject("content").getJSONArray("news_item").getJSONObject(j).get("thumb_url")));
                        wechatNews.setBrief(String.valueOf(((com.alibaba.fastjson.JSONObject) newsjson).getJSONArray("item").getJSONObject(i).getJSONObject("content").getJSONArray("news_item").getJSONObject(j).get("digest")));
                        wechatNews.setImageId(String.valueOf(((com.alibaba.fastjson.JSONObject) newsjson).getJSONArray("item").getJSONObject(i).getJSONObject("content").getJSONArray("news_item").getJSONObject(j).get("thumb_media_id")));
                        wechatNews.setType(Integer.valueOf(args[4]));
                        System.out.println(wechatNews);
                        wechatNewsList.add(wechatNews);
                    }
                }
                System.out.println("get data from wechat size : " + item_count);
                PostgreSQLUtil.insertNews(wechatNewsList);
                EntityUtils.consume(entityNews);
            } finally {
                responseNews.close();
            }

        } finally {
            responseToken.close();
        }

    }

}
