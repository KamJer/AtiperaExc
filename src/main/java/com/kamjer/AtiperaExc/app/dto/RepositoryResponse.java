package com.kamjer.AtiperaExc.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class RepositoryResponse {

    private String name;

    private OwnerDto owner;

    private boolean fork;

    private List<BranchDto> branchDTOList;
}
