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
public class InsertProvider extends MapperTemplate {

    public InsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insertOrUpdate(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        StringBuilder columns = new StringBuilder();
        columns.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\"> ");
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnSet) {
            columns.append("<if test=\"").append(column.getProperty()).append("!= null\"> ");
            columns.append(column.getColumn()).append(", ");
            columns.append("</if> ");
        }
        columns.append("</trim> ");
        sql.append(columns);
        sql.append(" VALUES ");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\"> ");
        for (EntityColumn column : columnSet) {
            sql.append("<if test=\"").append(column.getProperty()).append("!= null\"> ");
            sql.append("#{").append(column.getProperty()).append("}, ");
            sql.append("</if> ");
        }
        sql.append("</trim> ");
        sql.append("on duplicate key update ");
        sql.append("<trim suffixOverrides=\",\">");
        for (EntityColumn column : columnSet) {
            sql.append("<if test=\"").append(column.getProperty()).append("!= null\"> ");
            sql.append(column.getColumn()).append(" = ").append("#{").append(column.getProperty()).append("}, ");
            sql.append("</if> ");
        }
        sql.append("</trim>");
        return sql.toString();
    }

}