package io.github.zhoujunlin94.meet.mybatis_plus.sqlinjector.insert;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author zhoujunlin
 * @date 2025年12月04日 13:08
 * @desc
 */
public class InsertIgnore extends AbstractMethod {

    public InsertIgnore() {
        super("insertIgnore");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        String columnScript = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlColumnMaybeIf(null, false),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String valuesScript = LEFT_BRACKET + NEWLINE + SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlPropertyMaybeIf(null, false),
                null, null, null, COMMA) + NEWLINE + RIGHT_BRACKET;
        String keyProperty = tableInfo.getKeyProperty();
        String keyColumn = SqlInjectionUtils.removeEscapeCharacter(tableInfo.getKeyColumn());
        String sql = String.format("<script>\nINSERT IGNORE INTO %s %s VALUES %s\n</script>", tableInfo.getTableName(), columnScript, valuesScript);
        SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, methodName, sqlSource, keyGenerator, keyProperty, keyColumn);
    }

}

