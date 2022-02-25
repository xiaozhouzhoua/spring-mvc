package com.spring.springmvc.model;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class User {
    @Email(message = "邮箱格式错误")
    private String email;

    @NotNull
    @Size(min = 2, max = 20, message = "名字长度在2到30之间")
    private String name;

    @NotNull
    @Min(18)
    private Integer age;

    @NotNull
    @Past
    private Data birthday;
}
