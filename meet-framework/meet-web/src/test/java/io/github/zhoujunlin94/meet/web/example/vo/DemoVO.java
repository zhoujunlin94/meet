package io.github.zhoujunlin94.meet.web.example.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zhoujunlin
 * @date 2025-04-14-14:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "demo vo")
public class DemoVO {

    @Schema(description = "文件id")
    private String id;

    @Schema(description = "token")
    private String token;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "日期")
    private Date date;

}
