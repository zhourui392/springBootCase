package cn.zz.user.stream;

import lombok.Data;

/**
 * @author : zhourui
 * @version: 2019-07-22 19:07
 **/
@Data
public class Human {
    private String name;
    private String sex;
    private int age;
    private long id;

    public Human(String name, String sex, int age, long id) {
        super();
        this.name = name;
        this.age = age;
        this.id = id;
        this.sex = sex;
    }
}