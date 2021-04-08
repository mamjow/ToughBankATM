package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private int id;
    private String recipient;
    private String payer;
    private BigDecimal amount;
    private Date timestamp;
    private String description;


}