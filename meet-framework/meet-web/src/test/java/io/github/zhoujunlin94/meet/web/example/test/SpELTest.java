package io.github.zhoujunlin94.meet.web.example.test;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Collections;
import java.util.Map;

/**
 * @author zhoujunlin
 * @date 2026年02月03日 13:31
 * @desc
 */
public class SpELTest {

    public static void main(String[] args) {
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setVariables(Collections.singletonMap("a", Map.of("b", 1)));

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Object value = spelExpressionParser.parseExpression("#a['b']")
                .getValue(standardEvaluationContext);
        System.out.println(value);

        String uuid = spelExpressionParser.parseExpression("T(java.util.UUID).randomUUID().toString()")
                .getValue(String.class);
        System.out.println(uuid);
    }


}
