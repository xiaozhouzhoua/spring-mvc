package com.spring.springmvc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STOCK")
@Data
@NoArgsConstructor
public class StockDO {
    @Id
    private String symbol;

    private String name;
}
