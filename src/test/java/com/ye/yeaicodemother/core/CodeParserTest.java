package com.ye.yeaicodemother.core;

import com.ye.yeaicodemother.ai.model.HtmlCodeResult;
import com.ye.yeaicodemother.ai.model.MultiFileCodeResult;
import com.ye.yeaicodemother.core.parser.CodeParser;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeParserTest {

    @Resource
    CodeParser<HtmlCodeResult> codeParserhtml;

    @Resource
    CodeParser<MultiFileCodeResult> codeParsermulti;

    @Test
    void HtmlCodeParser(){
        String codeContent = """
                给我一个网页
                ```html
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Login</title>
                    <style>
                        body { font-family: Arial; margin: 50px; }
                        input, button { display: block; margin: 10px 0; padding: 10px; width: 200px; }
                    </style>
                </head>
                <body>
                    <h2>Login</h2>
                    <input type="text" placeholder="Username">
                    <input type="password" placeholder="Password">
                    <button onclick="alert('Logged in!')">Login</button>
                </body>
                </html>                
                ```
                """;
        HtmlCodeResult result = codeParserhtml.parseCode(codeContent);
        assertNotNull(result);
        assertNotNull(result.getHtmlCode());
    }

    @Test
    void MultiCodeParser(){
        String codeContent = """
                给我一个网页
                ```html
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Login</title>
                    <style>
                        body { font-family: Arial; margin: 50px; }
                        input, button { display: block; margin: 10px 0; padding: 10px; width: 200px; }
                    </style>
                </head>
                <body>
                    <h2>Login</h2>
                    <input type="text" placeholder="Username">
                    <input type="password" placeholder="Password">
                    <button onclick="alert('Logged in!')">Login</button>
                </body>
                </html>                
                ```
                """;
        MultiFileCodeResult result = codeParsermulti.parseCode(codeContent);
        assertNotNull(result);
        assertNotNull(result.getHtmlCode());
    }

}