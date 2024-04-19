package io.github.zhoujunlin94.meet.tk_mybatis.properties;

import cn.hutool.core.util.StrUtil;
import io.github.zhoujunlin94.meet.common.exception.MeetException;
import lombok.Data;

import java.util.Objects;
import java.util.Properties;

/**
 * @author zhoujunlin
 * @date 2024-04-19-14:42
 */
@Data
public class DatasourceConfig {

    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private Properties hikari;

    private MybatisConfig mybatis;

    public void checkDatasource() {
        if (StrUtil.hasBlank(url, driverClassName, username, password)) {
            throw MeetException.meet("请检查数据源配置");
        }
    }

    public void checkMyBatis() {
        if (Objects.isNull(mybatis) ||
                StrUtil.hasBlank(mybatis.getTypeAliasesPackage(), mybatis.getBasePackages(), mybatis.getMapperLocation())) {
            throw MeetException.meet("请检查MyBatis配置");
        }
    }

}
