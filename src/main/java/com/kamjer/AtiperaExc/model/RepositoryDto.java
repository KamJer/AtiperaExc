package com.kamjer.AtiperaExc.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class RepositoryDto {

    String name;

    OwnerDto owner;

    boolean fork;
}
