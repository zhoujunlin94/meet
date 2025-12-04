package io.github.zhoujunlin94.meet.mybatis_plus.sqlinjector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import io.github.zhoujunlin94.meet.mybatis_plus.sqlinjector.insert.InsertIgnore;
import io.github.zhoujunlin94.meet.mybatis_plus.sqlinjector.insert.InsertOnDuplicateKeyUpdate;
import org.apache.ibatis.session.Configuration;

import java.util.List;

/**
 * @author zhoujunlin
 * @date 2025年12月04日 13:06
 * @desc
 */
public class MeetSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(configuration, mapperClass, tableInfo);
        methodList.add(new InsertIgnore());
        methodList.add(new InsertOnDuplicateKeyUpdate());
        return methodList;
    }

}
