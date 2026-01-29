package io.github.zhoujunlin94.meet.web.example.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhoujunlin
 * @date 2025-04-14-14:05
 */
@Schema(description = "demo vo")
public record DemoVO(@Schema(description = "文件id") String id,
                     @Schema(description = "token") String token,
                     @Schema(description = "文件名") String name,
                     @Schema(description = "日期") Date date,
                     @Schema(description = "金额") BigDecimal amount
) implements Serializable {


}
