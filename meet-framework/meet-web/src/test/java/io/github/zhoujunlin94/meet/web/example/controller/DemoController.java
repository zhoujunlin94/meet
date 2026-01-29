package io.github.zhoujunlin94.meet.web.example.controller;

import com.alibaba.fastjson2.JSONObject;
import io.github.zhoujunlin94.meet.common.constant.CommonConstant;
import io.github.zhoujunlin94.meet.web.example.vo.DemoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Valid
@RestController
@RequestMapping("/demo")
@Tag(name = "demo")
public class DemoController {

    @Operation(summary = "demo get")
    @Parameters({
            @Parameter(name = "id", description = "文件id", in = ParameterIn.PATH),
            @Parameter(name = "token", description = "请求token", required = true, in = ParameterIn.HEADER),
            @Parameter(name = "name", description = "文件名称", required = true, in = ParameterIn.QUERY)
    })
    @PostMapping("/{id}/post")
    public DemoVO demo(@PathVariable("id") String id,
                       @RequestHeader("token") String token,
                       @RequestParam("name") String name,
                       @RequestBody JSONObject requestBody) {
        log.warn(Objects.isNull(requestBody) ? CommonConstant.EMPTY_JSON : requestBody.toJSONString());
        return new DemoVO(id, token, name, new Date(), new BigDecimal("99.99"));
    }

}
