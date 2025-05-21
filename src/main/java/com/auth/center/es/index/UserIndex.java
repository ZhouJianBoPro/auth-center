package com.auth.center.es.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/21 16:50
 **/
@Data
@Document(indexName = "sys_user", createIndex = false)
public class UserIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String username;


    @Field(type = FieldType.Text)
    private String realname;

    @Field(type = FieldType.Text, name = "create_by")
    private String createBy;

    @Field(type = FieldType.Date, name = "create_time")
    private String createTime;

    @Field(type = FieldType.Text, name = "update_by")
    private String updateBy;
}
