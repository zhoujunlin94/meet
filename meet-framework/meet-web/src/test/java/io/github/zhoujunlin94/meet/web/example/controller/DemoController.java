package io.github.zhoujunlin94.meet.web.example.controller;

import io.github.zhoujunlin94.meet.web.example.vo.DemoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
    @GetMapping("/get/{id}")
    public DemoVO bodyParamHeaderPath(@PathVariable("id") String id, @RequestHeader("token") String token, @RequestParam("name") String name) {
        return new DemoVO(id, token, name, new Date());
    }

}
