package io.github.zhoujunlin94.meet.tk_mybatis.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * @author zhoujunlin
 * @date 2024年03月08日 14:42
 * @desc
 */
public class BatchUpdateProvider extends MapperTemplate {

    public BatchUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String batchUpdateSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);

        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass))).append("\n");
        sql.append("<trim prefix=\"set\" suffixOverrides=\",\">").append("\n");

        //获取全部列并set
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        for (EntityColumn entityColumn : columnSet) {
            String column = entityColumn.getColumn();
            String columnProperty = entityColumn.getProperty();
            sql.append("    <trim prefix=\"").append(column).append(" = case\" suffix=\"end,\">").append("\n");
            sql.append("        <foreach collection=\"list\" index=\"index\" item=\"item\">").append("\n");
            sql.append("            <choose>").append("\n");
            sql.append("                <when test=\"item.").append(columnProperty).append(" != null\">").append("\n");
            sql.append("                    when id = #{item.id,jdbcType=INTEGER} then ").append("#{item.").append(columnProperty).append("}").append("\n");
            sql.append("                </when>").append("\n");
            sql.append("                <otherwise>").append("\n");
            sql.append("                    when id = #{item.id,jdbcType=INTEGER} then ").append(column).append("\n");
            sql.append("                </otherwise>").append("\n");
            sql.append("            </choose>").append("\n");
            sql.append("        </foreach>").append("\n");
            sql.append("    </trim>").append("\n");
        }
        sql.append("</trim>").append("\n");

        // where 条件
        sql.append(" where id in ").append("\n");
        sql.append("<foreach close=\")\" collection=\"list\" item=\"item\" open=\"(\" separator=\", \">").append("\n");
        sql.append("    #{item.id,jdbcType=INTEGER}").append("\n");
        sql.append("</foreach>");

        return sql.toString();
    }

}