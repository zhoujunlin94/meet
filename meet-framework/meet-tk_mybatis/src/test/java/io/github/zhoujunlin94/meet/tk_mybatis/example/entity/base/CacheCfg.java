package io.github.zhoujunlin94.meet.tk_mybatis.example.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cache_config")
public class CacheCfg implements Serializable {
    /**
     * 主键,配置key
     */
    @Id
    @Column(name = "`key`")
    private String key;

    /**
     * 配置值
     */
    @Column(name = "`value`")
    private String value;

    /**
     * 配置描述
     */
    @Column(name = "`desc`")
    private String desc;

    private static final long serialVersionUID = 1L;
}