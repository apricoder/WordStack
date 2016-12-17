package com.olebokolo.wordstack.core.model;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Side extends SugarRecord implements Serializable{
    private Long id;
    private Long languageId;
    private String content;
}
