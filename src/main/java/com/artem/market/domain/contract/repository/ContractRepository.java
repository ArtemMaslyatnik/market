package com.artem.market.domain.contract.repository;


import com.artem.market.domain.contract.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long>{
}


