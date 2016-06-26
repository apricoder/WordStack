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
public class Side {
    private Long id;
    private Long languageId;
    private String content;
}
