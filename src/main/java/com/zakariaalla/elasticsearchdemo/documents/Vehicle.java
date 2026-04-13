package com.zakariaalla.elasticsearchdemo.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import static com.zakariaalla.elasticsearchdemo.helper.Indices.VEHICLE_INDEX;

@Document(indexName = VEHICLE_INDEX)
@Setting(settingPath = "static/es-settings.json")
public class Vehicle {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String number;

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
