package io.github.zhoujunlin94.meet.mybatis_plus.sqlinjector.insert;

import cn.hutool.core.util.StrUtil;
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
public class InsertOnDuplicateKeyUpdate extends AbstractMethod {

    public InsertOnDuplicateKeyUpdate() {
        super("insertOnDuplicateKeyUpdate");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;

        // 1. 构造 INSERT 部分
        String columnScript = SqlScriptUtils.convertTrim(
                tableInfo.getAllInsertSqlColumnMaybeIf(null, false),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA
        );

        String valuesScript = LEFT_BRACKET + NEWLINE +
                SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlPropertyMaybeIf(null, false),
                        null, null, null, COMMA) + NEWLINE + RIGHT_BRACKET;

        // 2. 构造 UPDATE 部分，只更新非空字段
        StringBuilder updateScript = new StringBuilder();
        updateScript.append("<trim prefix=\"\" suffixOverrides=\",\">");
        tableInfo.getFieldList().forEach(field -> {
            String property = field.getProperty();
            if (!StrUtil.equals(tableInfo.getKeyProperty(), property)) {
                String column = SqlInjectionUtils.removeEscapeCharacter(field.getColumn());
                updateScript.append(String.format(
                        "<if test=\"%s != null\">%s=#{%s},</if>\n", property, column, property
                ));
            }
        });
        updateScript.append("</trim>");

        // 3. 拼接完整 SQL 语句
        String sql = String.format(
                "<script>\nINSERT INTO %s %s VALUES %s ON DUPLICATE KEY UPDATE %s\n</script>",
                tableInfo.getTableName(), columnScript, valuesScript, updateScript
        );

        // 4. 创建 SqlSource
        SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);

        // 5. 添加 MappedStatement
        String keyProperty = tableInfo.getKeyProperty();
        String keyColumn = SqlInjectionUtils.removeEscapeCharacter(tableInfo.getKeyColumn());
        return this.addInsertMappedStatement(
                mapperClass, modelClass, "insertOnDuplicateKeyUpdate", sqlSource,
                keyGenerator, keyProperty, keyColumn
        );

    }

}

