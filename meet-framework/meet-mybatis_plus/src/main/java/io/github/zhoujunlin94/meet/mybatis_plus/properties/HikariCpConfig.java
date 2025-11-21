package io.github.zhoujunlin94.meet.mybatis_plus.properties;

import lombok.Data;

import java.util.Properties;

/**
 * @author zhoujunlin
 * @date 2025年10月10日 14:11
 */
@Data
public class HikariCpConfig {

    /**
     * 连接池名称
     */
    private String poolName;
    /**
     * 最小空闲连接数
     */
    private Integer minimumIdle = 20;
    /**
     * 空闲连接存活最大时间，默认10分钟
     */
    private Long idleTimeout = 600000L;
    /**
     * 连接池最大连接数，默认是10
     */
    private Integer maximumPoolSize = 100;
    /**
     * 此属性控制从池返回的连接的默认自动提交行为,默认值：true
     */
    private Boolean autoCommit = true;
    /**
     * 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认30分钟
     */
    private Long maxLifetime = 1800000L;
    /**
     * 数据库连接超时时间,默认30秒
     */
    private Long connectionTimeout = 30000L;
    /**
     * 连接测试query
     */
    private String connectionTestQuery = "SELECT 1";

    public Properties buildProperties() {
        Properties properties = new Properties();
        properties.setProperty("poolName", poolName);
        properties.setProperty("minimumIdle", minimumIdle.toString());
        properties.setProperty("idleTimeout", idleTimeout.toString());
        properties.setProperty("maximumPoolSize", maximumPoolSize.toString());
        properties.setProperty("autoCommit", autoCommit.toString());
        properties.setProperty("maxLifetime", maxLifetime.toString());
        properties.setProperty("connectionTimeout", connectionTimeout.toString());
        properties.setProperty("connectionTestQuery", connectionTestQuery);
        return properties;
    }


}
