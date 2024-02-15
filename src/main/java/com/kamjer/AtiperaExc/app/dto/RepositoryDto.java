package com.kamjer.AtiperaExc.app.dto;

import lombok.Getter;
import lombok.Value;

import java.util.List;

@Value
@Getter
public class RepositoryDto {

    private String name;

    private OwnerDto owner;

    private boolean fork;
}
