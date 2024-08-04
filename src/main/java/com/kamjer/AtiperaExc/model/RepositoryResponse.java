package com.kamjer.AtiperaExc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

import java.util.List;

@Data
@AllArgsConstructor
public class RepositoryResponse {

    private String name;

    private OwnerDto owner;

    private boolean fork;

    private Flux<BranchDto> branchDtoList;

    public RepositoryResponse(RepositoryDto repositoryDto, Flux<BranchDto> branchDtoList) {
        this.name = repositoryDto.getName();
        this.owner = repositoryDto.getOwner();
        this.fork = repositoryDto.isFork();
        this.branchDtoList = branchDtoList;
    }
}
