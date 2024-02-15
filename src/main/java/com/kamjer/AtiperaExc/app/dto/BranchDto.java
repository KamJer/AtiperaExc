package com.kamjer.AtiperaExc.app.dto;

import lombok.Value;

@Value
public class BranchDto {

    private String name;

    private CommitDto commit;
}
