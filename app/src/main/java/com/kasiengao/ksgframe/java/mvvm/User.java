package com.kasiengao.ksgframe.java.mvvm;

/**
 * @ClassName: User
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/11 14:34
 * @Description:
 */
public class User {

    private String name;

    private String content;

    public User(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
