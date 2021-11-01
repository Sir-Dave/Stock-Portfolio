package com.sirdave.portfolio.portfolio;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "stock")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Stock {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_sequence")
    @SequenceGenerator(name = "stock_sequence", sequenceName = "stock_sequence", allocationSize = 1)
    private Long id;
    private String name;
    private String symbol;
    private String quantity;
    private String equityValue;
    private String pricePerShare;

}
