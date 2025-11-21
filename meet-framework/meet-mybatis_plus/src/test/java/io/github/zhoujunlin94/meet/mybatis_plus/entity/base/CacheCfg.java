package io.github.zhoujunlin94.meet.mybatis_plus.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName(value = "cache_config")
public class CacheCfg implements Serializable {
    /**
     * 主键,配置key
     */
    @TableId(value = "`key`")
    private String key;

    /**
     * 配置值
     */
    @TableField(value = "`value`")
    private String value;

    /**
     * 配置描述
     */
    @TableField(value = "`desc`")
    private String desc;
}