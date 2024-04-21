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
public class BatchInsertProvider extends MapperTemplate {

    public BatchInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String batchInsertSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(insertColumns(entityClass));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnList) {
            sql.append("<if test=\" ").append("list[0].").append(column.getProperty()).append(" != null \">")
                    .append(column.getColumnHolder("record")).append(",")
                    .append("</if>");
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }


    private static String insertColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnSet) {
            sql.append(getIfNotNull(column, column.getColumn() + ","));
        }
        sql.append("</trim>");
        return sql.toString();
    }

    private static String getIfNotNull(EntityColumn column, String contents) {
        return "<if test=\" list[0]." + column.getProperty() + " != null \">" +
                contents +
                "</if>";
    }

}