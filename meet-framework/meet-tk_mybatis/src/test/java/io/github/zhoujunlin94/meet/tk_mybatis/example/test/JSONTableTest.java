package io.github.zhoujunlin94.meet.tk_mybatis.example.test;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.zhoujunlin94.meet.tk_mybatis.example.TkMapperApplication;
import io.github.zhoujunlin94.meet.tk_mybatis.example.entity.meet.JsonTable;
import io.github.zhoujunlin94.meet.tk_mybatis.example.mapper.meet.JsonTableMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhoujunlin
 * @date 2023年04月21日 10:12
 * @desc
 */
@Slf4j
// @ActiveProfiles(profiles = "dev")
@SpringBootTest(classes = TkMapperApplication.class)
public class JSONTableTest {

    @Resource
    private JsonTableMapper jsonTableMapper;

    @Test
    public void insert() {
        JSONObject json2 = new JSONObject();
        JSONArray json1 = new JSONArray();
        jsonTableMapper.insert(JsonTable.builder()
                .jsonStr(json2)
                .jsonObj(json1)
                .build());
    }

    @Test
    public void insert2() {
        jsonTableMapper.insertSelective(JsonTable.builder()
                .jsonStr(null)
                .jsonObj(null)
                .build());
    }

    @Test
    public void insert3() {
        JSONObject json1 = new JSONObject();
        json1.put("userName", "zhoujl");

        JSONObject json2 = new JSONObject();
        json2.put("userName", "zhoujl2");
        jsonTableMapper.insert(JsonTable.builder()
                .jsonStr(json1)
                .jsonObj(JSONArray.of(json1, json2))
                .build());
    }

    @Test
    public void insert4() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", "zhoujl");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        // 引用第一个元素  [{"userName":"zhoujl"},{"$ref":"$[0]"}]
        jsonArray.add(jsonObject);

        jsonTableMapper.insert(JsonTable.builder()
                .jsonStr(jsonObject)
                .jsonObj(jsonArray)
                .build());
    }

    @Test
    public void print() {

        JsonTable jsonTable = jsonTableMapper.selectByPrimaryKey(22);
        System.out.println(jsonTable.getJsonObj().toJSONString());
        System.out.println(jsonTable.getJsonStr().toJSONString());


        jsonTable = jsonTableMapper.selectByPrimaryKey(23);
        System.out.println(jsonTable.getJsonObj().toJSONString());
        System.out.println(jsonTable.getJsonStr().toJSONString());

        JSONArray jsonArray = jsonTable.getJsonObj();
        System.out.println(jsonArray.get(0));
        System.out.println(jsonArray.get(1));
    }


}
