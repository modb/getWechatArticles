package wechatNews;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.sql.Date;


@Data
public class WechatNews {
    private Integer id;
    private String title;
    private String brief;
    private String source;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createdTime;
    private Integer type;
    private String url;
    private String imageUrl;
    private String imageId;
}
