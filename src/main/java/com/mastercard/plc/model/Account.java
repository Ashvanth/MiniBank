package com.mastercard.plc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ACCOUNT")
@Data
@ToString
public class Account {

    @Id
    @Column(name = "accountno")
    private int accountno;

    @Column(name = "ACCOUNTTYPE")
    private String accountType;

    @Column(name = "BALANCE")
    private String balance;

    @Column(name= "CURRENCY")
    private String currency;

    @Column(name="FUND_STATUS")
    private Boolean fundstatus;

    @OneToMany(targetEntity = Transaction.class,cascade=CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinColumn(name ="accountno_id",referencedColumnName = "accountno")
    private List<Transaction> transaction;

}
