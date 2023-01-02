package com.mastercard.plc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "TRANSACTION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Transaction {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long transactionId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "accountno_id")
    @JsonIgnore
    private Account account;

    @Column(name = "AMOUNT")
    private String amount ;

    @Column(name = "TRANSACTION_DATE")
    private Date transactiondate;

    @Column(name = "TRANSACTION_TYPE")
    private  String transactiontype;

    @Column(name="ORIGIN_ACCOUNT")
    private String originaccount;
}
