package com.vainycos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author: Vainycos
 * @description
 * @date: 2023/6/1 16:04
 */
@Data
@ToString
@NoArgsConstructor
public class Employee {
    private Integer id;
    private String name;
    private String pwd;
    private List<EmployeeClass> employe;
}
