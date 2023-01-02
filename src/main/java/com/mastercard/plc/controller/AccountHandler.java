package com.mastercard.plc.controller;


import com.mastercard.plc.dao.AccountRepository;
import com.mastercard.plc.model.Account;
import com.mastercard.plc.model.Transaction;
import com.mastercard.plc.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/account")
public class AccountHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;


    @GetMapping("/health")
    public String healthcheck()
    {
        System.out.println("------------");
        return "HEALHT IS FINE";
    }

    @PostMapping("/save")
    public String saveAccountInfo(@RequestBody Account account)
    {
        accountRepository.save(account);
        return "Account is saved with ID";
    }


    @GetMapping("/{accountNo}/balance")
    public Account accountBalance(@PathVariable int accountNo)
    {
        Account account = accountRepository.findById(accountNo).get();
        account.setTransaction(null);
        return account;
    }

    @Transactional
    @GetMapping("/{accountNo}/statement")
    public Stream<Object> transactionStatement(@PathVariable int accountNo)
    {
        return accountRepository.findByaccountno(accountNo)
                .map(account -> account.getTransaction());
    }

    @GetMapping("/deleteByid/{id}")
    public String deletebyID(@PathVariable  Integer id)
    {
        accountRepository.deleteById(id);
        return "Value is deleted" ;
    }

    @PostMapping("{fromAccount}/{toAccount}/{amount}")
    public String accountTransfer(@PathVariable Integer fromAccount,
                                  @PathVariable Integer toAccount,
                                  @PathVariable String amount)
    {

        boolean fundStatusIsPositive = accountService.validateFromAccountBalance(fromAccount);
        System.out.println("--------"+fundStatusIsPositive);
        if(fundStatusIsPositive==true)
        {
            accountService.transferAmount(fromAccount,toAccount,amount);
            return  "Money Can be Transferred";
        }
        else
        {
            return "Sorry! Insufficient funds in FROM ACCOUNT NO  "+fromAccount;
        }
    }

}
