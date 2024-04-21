package io.github.zhoujunlin94.meet.tk_mybatis.example.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.zhoujunlin94.meet.tk_mybatis.example.ExampleApp;
import io.github.zhoujunlin94.meet.tk_mybatis.example.entity.meet.JsonTable;
import io.github.zhoujunlin94.meet.tk_mybatis.example.mapper.meet.JsonTableMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author zhoujunlin
 * @date 2023年04月21日 10:12
 * @desc
 */
@Slf4j
@RunWith(SpringRunner.class)
// @ActiveProfiles(profiles = "dev")
@SpringBootTest(classes = ExampleApp.class)
public class JSONTableTest {

    @Resource
    private JsonTableMapper jsonTableMapper;

    @Test
    public void insert() {
        JSON json2 = new JSONObject();
        JSON json1 = new JSONObject();
        jsonTableMapper.insert(JsonTable.builder()
                .jsonStr(json1)
                .jsonObj(json2)
                .build());
    }

    @Test
    public void insert2() {
        JSON json2 = null;
        JSON json1 = null;
        jsonTableMapper.insertSelective(JsonTable.builder()
                .jsonStr(json1)
                .jsonObj(json2)
                .build());
    }

    @Test
    public void insert3() {
        JSONObject json1 = new JSONObject();
        json1.put("userName", "zhoujl");

        JSONObject json2 = new JSONObject();
        json2.put("userName", "zhoujl");
        jsonTableMapper.insert(JsonTable.builder()
                .jsonStr(json1)
                .jsonObj(json2)
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
                .jsonStr(jsonArray)
                .jsonObj(jsonArray)
                .build());
    }

    @Test
    public void print() {

        JsonTable jsonTable = jsonTableMapper.selectByPrimaryKey(3);
        System.out.println(jsonTable.getJsonObj().toJSONString());
        System.out.println(jsonTable.getJsonStr().toJSONString());


        jsonTable = jsonTableMapper.selectByPrimaryKey(4);
        System.out.println(jsonTable.getJsonObj().toJSONString());
        System.out.println(jsonTable.getJsonStr().toJSONString());

        JSONArray jsonArray = (JSONArray) jsonTable.getJsonObj();
        System.out.println(jsonArray.get(0));
        System.out.println(jsonArray.get(1));


    }


}
