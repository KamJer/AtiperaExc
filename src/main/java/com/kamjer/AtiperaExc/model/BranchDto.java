package com.kamjer.AtiperaExc.model;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class BranchDto {

    String name;

    CommitDto commit;

}
