package com.artem.market.domain.company.entity;

import com.artem.market.model.base.AbstractCatalog;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class Company extends AbstractCatalog {

}