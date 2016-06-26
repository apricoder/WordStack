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
@AllArgsConstructor(suppressConstructorProperties = true)
public class Language {
    private Long id;
    private String name;
    private String shortName;
}
