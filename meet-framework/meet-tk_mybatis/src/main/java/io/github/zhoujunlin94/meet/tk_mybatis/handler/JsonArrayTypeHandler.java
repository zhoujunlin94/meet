package io.github.zhoujunlin94.meet.tk_mybatis.handler;

import com.alibaba.fastjson.JSONArray;
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
@Alias(value = "JsonArrayTypeHandler")
@MappedTypes({JSONArray.class})
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.ARRAY})
public class JsonArrayTypeHandler extends BaseTypeHandler<JSONArray> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONArray parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, parameter.toJSONString());
        } catch (Exception e) {
            log.error("JsonArrayTypeHandler.setNonNullParameter error", e);
            ps.setString(i, "[]");
        }
    }

    @Override
    public JSONArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return convertStr2JSONArray(json);
    }

    @Override
    public JSONArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return convertStr2JSONArray(json);
    }

    @Override
    public JSONArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return convertStr2JSONArray(json);
    }

    private JSONArray convertStr2JSONArray(String json) {
        try {
            return JSONArray.parseArray(json);
        } catch (Exception e) {
            log.error("JsonArrayTypeHandler.convertStr2JSONArray error", e);
        }
        return new JSONArray();
    }

}
