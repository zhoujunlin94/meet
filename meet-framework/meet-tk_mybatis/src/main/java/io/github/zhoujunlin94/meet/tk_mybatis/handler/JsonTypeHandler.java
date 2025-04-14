package io.github.zhoujunlin94.meet.tk_mybatis.handler;

import com.alibaba.fastjson2.JSONObject;
import io.github.zhoujunlin94.meet.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhoujunlin
 * @date 2023年04月21日 9:37
 * @desc
 */
@Slf4j
@Alias(value = "JsonTypeHandler")
@MappedTypes({JSONObject.class})
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.JAVA_OBJECT})
public class JsonTypeHandler extends BaseTypeHandler<JSONObject> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, parameter.toJSONString());
        } catch (Exception e) {
            log.error("JsonTypeHandler.setNonNullParameter error", e);
            ps.setString(i, CommonConstant.EMPTY_JSON);
        }
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return convertStr2JSON(json);
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return convertStr2JSON(json);
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return convertStr2JSON(json);
    }

    private JSONObject convertStr2JSON(String json) {
        try {
            return JSONObject.parseObject(json);
        } catch (Exception e) {
            log.error("JsonTypeHandler.convertStr2JSON error", e);
        }
        return new JSONObject();
    }

}
