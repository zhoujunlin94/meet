package io.github.zhoujunlin94.meet.tk_mybatis.properties;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Properties;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:42
 */
@Data
@Slf4j
public class DatasourceConfig {

    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private Properties hikari;

    private MybatisConfig mybatis;

    public boolean checkDatasource() {
        if (StrUtil.hasBlank(url, driverClassName, username, password)) {
            log.error("请检查数据源配置");
            return false;
        }

        return true;
    }

    public boolean checkMyBatis() {
        if (Objects.isNull(mybatis) || StrUtil.hasBlank(mybatis.getEntityPackage(), mybatis.getMapperPackage())) {
            log.error("请检查MyBatis配置");
            return false;
        }

        return true;
    }

}
