package com.kamjer.AtiperaExc.app.DTO;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class RepositoryDTO {

    private String name;

    private OwnerDTO owner;

    private boolean fork;

    private List<BranchDTO> branchDTOList;

}
