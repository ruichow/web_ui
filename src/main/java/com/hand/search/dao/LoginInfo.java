package com.hand.search.dao;

import lombok.Data;

/**
 * @author：关河九州
 * @date：2019/11/14 17:16
 * @version：1.0
 */

//Data注解是lombok.jar包下的注解，该注解通常用在实体bean上，不需要写出set和get方法，但是具备实体bean所具备的方法，简化编程提高编程速度
@Data
public class LoginInfo {
    private String accessToken;
    private String env;
}
