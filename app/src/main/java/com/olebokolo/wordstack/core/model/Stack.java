package com.olebokolo.wordstack.core.model;

import com.orm.dsl.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stack {
    private Long id;
    private String name;
    private Long frontLangId;
    private Long backLangId;
}
