package io.github.zhoujunlin94.meet.tk_mybatis.entity.meet;

import com.alibaba.fastjson.JSON;
import io.github.zhoujunlin94.meet.tk_mybatis.handler.JsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "json_table")
public class JsonTable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * json类型字符串
     */
    @ColumnType(column = "json_str", typeHandler = JsonTypeHandler.class)
    private JSON jsonStr;

    /**
     * json类型
     */
    @ColumnType(column = "json_obj", typeHandler = JsonTypeHandler.class)
    private JSON jsonObj;

}