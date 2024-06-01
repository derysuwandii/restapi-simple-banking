package com.derysuwandi.restapisimplebanking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "account")
public class Transaction extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "amount")
    private double amount;

    @Column(name ="transaction_type")
    private String transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
}