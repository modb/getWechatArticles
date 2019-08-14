package wechatNews;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author steven
 * @date 2018/11/12
 * @desc
 */
public class PostgreSQLUtil {
    public static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager
                    .getConnection("jdbc:postgresql://192.168.1.1:5432/emcs",
                            "root", "c123456");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void insertNews(List<WechatNews> wechatNewsList) {
        Connection conn = getConn();
        String sql = "insert into wechat_news(id,title, created_time, source, type, url, image_url, brief, image_id) values" +
                "(nextval('seq_wechat_news'),?,?,?,?,?,?,?,?) ON CONFLICT (image_id) do nothing";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            for (int i = 0; i < wechatNewsList.size(); i++) {
                pstmt.setObject(1, wechatNewsList.get(i).getTitle());
                pstmt.setObject(2, wechatNewsList.get(i).getCreatedTime());
                pstmt.setObject(3, wechatNewsList.get(i).getSource());
                pstmt.setObject(4, wechatNewsList.get(i).getType());
                pstmt.setObject(5, wechatNewsList.get(i).getUrl());
                pstmt.setObject(6, wechatNewsList.get(i).getImageUrl());
                pstmt.setObject(7, wechatNewsList.get(i).getBrief());
                pstmt.setObject(8, wechatNewsList.get(i).getImageId());
                pstmt.addBatch();
            }
            int rs[] = pstmt.executeBatch();
            System.out.println("Insert news count : " + Arrays.stream(rs).reduce(0, Integer::sum));
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
