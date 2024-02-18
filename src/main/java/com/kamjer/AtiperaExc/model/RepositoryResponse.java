package com.kamjer.AtiperaExc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RepositoryResponse {

    private String name;

    private OwnerDto owner;

    private boolean fork;

    private List<BranchDto> branchDtoList;

    public RepositoryResponse(RepositoryDto repositoryDto, List<BranchDto> branchDtoList) {
        this.name = repositoryDto.getName();
        this.owner = repositoryDto.getOwner();
        this.fork = repositoryDto.isFork();
        this.branchDtoList = branchDtoList;
    }
}
