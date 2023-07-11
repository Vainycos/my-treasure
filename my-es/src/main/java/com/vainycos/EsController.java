package com.vainycos;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author: Vainycos
 * @description
 * @date: 2023/6/1 16:03
 */
@RestController
@RequestMapping("/es")
public class EsController {

    /**
     * 测试索引
     */
    private String indexName = "megacorp";

    /**
     * 类型
     */
    private String esType = "employee";

    /**
     * 创建索引
     * http://127.0.0.1:8080/es/createIndex
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "/createIndex")
    public String createIndex(HttpServletRequest request, HttpServletResponse response) {
        if (!ElasticsearchUtil.isIndexExist(indexName)) {
            ElasticsearchUtil.createIndex(indexName);
        } else {
            return "索引已经存在";
        }
        return "索引创建成功";
    }

    /**
     * 新增对象
     *
     * @return
     */
    @GetMapping(value = "/insertModelObj")
    public Result insertModelObj(Integer id,String name,String pwd) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("用户名:"+name);
        employee.setPwd("密码:"+pwd);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(employee);
        String idsa = ElasticsearchUtil.addData(jsonObject, indexName, esType, jsonObject.getString("id"));
        return Result.OK(idsa);
    }


    /**
     * 插入记录
     *
     * @return
     */
    @GetMapping(value = "/insertModel")
    public Result insertModel() {
        for (int i = 1; i < 100; i++) {
            Employee employee = new Employee();
            employee.setId(i);
            employee.setName("用户名:_"+i);
            employee.setPwd("密码:_"+i);
            List<EmployeeClass> employe = new ArrayList<>();
            EmployeeClass employeeClass = new EmployeeClass();
            employeeClass.setPhone(String.valueOf(new Random(10000).nextInt()));
            employe.add(employeeClass);
            EmployeeClass employeeClasssa = new EmployeeClass();
            employeeClasssa.setPhone(String.valueOf(new Random(10000).nextInt()));
            employe.add(employeeClasssa);
            employee.setEmploye(employe);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(employee);
            String id = ElasticsearchUtil.addData(jsonObject, indexName, esType, jsonObject.getString("id"));
        }
        return Result.OK();
    }




    /**
     * 删除记录
     *
     * @return
     */
    @GetMapping(value = "/delete")
    public Result delete(String id) {
        if (StringUtils.isNotBlank(id)) {
            Integer status =  ElasticsearchUtil.deleteDataById(indexName, esType, id);
            if (status==404){
                return Result.error("id="+id+"不存在,删除失败!");
            }
            return Result.OK("id="+id+"删除成功!");
        } else {
            return Result.error("id为空");
        }
    }

    /**
     * 更新数据
     *
     * @return
     */
    @GetMapping(value = "/update")
    public Result update(Integer id) {
        if (StringUtils.isNotBlank(String.valueOf(id))) {
            Employee employee = new Employee();
            employee.setId(id);
            employee.setName("用户名:_张三");
            employee.setPwd("密码:_张三的密码");
            List<EmployeeClass> employe = new ArrayList<>();
            EmployeeClass employeeClass = new EmployeeClass();
            employeeClass.setPhone("1125154125");
            employe.add(employeeClass);
            EmployeeClass employeeClasssa = new EmployeeClass();
            employeeClasssa.setPhone("18524715241");
            employe.add(employeeClasssa);
            employee.setEmploye(employe);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(employee);
            Integer status = ElasticsearchUtil.updateDataById(jsonObject, indexName, esType, String.valueOf(id));
            if (status==404){
                return Result.error("id="+id+"不存在,修改失败!");
            }
            return Result.OK("id="+id+"修改成功!");
        } else {
            return Result.error("id为空");
        }
    }




    /**
     * 获取数据
     * http://127.0.0.1:8080/es/getData?id=2018-04-25%2016:33:44
     * @param id
     * @return
     */
    @GetMapping(value = "/getData")
    public Result getData(String id) {
        if (StringUtils.isNotBlank(id)) {
            Map<String, Object> map = ElasticsearchUtil.searchDataById(indexName, esType, id, null);
            return Result.OK(map);
        } else {
            return Result.error("id为空");
        }
    }

    /**
     * 查询分页
     *
     * @param pageno 第几条记录开始
     *                  第1页 ：http://127.0.0.1:8080/es/queryPage?startPage=0&pageSize=2
     *                  第2页 ：http://127.0.0.1:8080/es/queryPage?startPage=2&pageSize=2
     * @param pageSize  每页大小
     * @return
     */
    @GetMapping(value = "/queryPage")
    public Result queryPage(Integer pageno, Integer pageSize,String name,Long isf) {
        if (StringUtils.isNotBlank(String.valueOf(pageno)) && StringUtils.isNotBlank(String.valueOf(pageSize))) {

            if (pageno==1){
                pageno = 0;
            }else{
                pageno = pageno * pageSize;
            }
            if(pageSize<=0){
                pageSize = 1;
            }

            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            if (StringUtils.isNotBlank(name)){
                if (isf==1L){
                    boolQuery.must(QueryBuilders.matchQuery("name", name));
                }else{
                    boolQuery.must(QueryBuilders.matchPhraseQuery("name", name));
                }
            }

//            boolQuery.must(QueryBuilders.rangeQuery("age").from("20").to("100"));//闭区间
//            boolQuery.must(QueryBuilders.rangeQuery("age").from(20,false).to(100,false));//开区间
//                                                             索引名称    索引类型      当前页                 显示页                        查询条件    需要显示的字段，逗号分隔（缺省为全部字段）  排序字段   高亮字段   高亮值
            EsPage list = ElasticsearchUtil.searchDataPage(SortOrder.ASC,indexName, esType, Integer.parseInt(String.valueOf(pageno)), Integer.parseInt(String.valueOf(pageSize)), boolQuery, null, "id", "name","用户");
//            return JSONObject.toJSONString(list);
            return Result.OK(list);
        } else {
            return Result.error( "startPage或者pageSize缺失");
        }
    }



}