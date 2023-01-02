package com.mastercard.plc.service;

import com.mastercard.plc.dao.AccountRepository;
import com.mastercard.plc.model.Account;
import com.mastercard.plc.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private void saveAccounts(List<Account> accounts)
    {
      //   accountRepository.save(accounts);
    }

    public Boolean validateFromAccountBalance(Integer fromAccount)
    {
        return accountRepository.findById(fromAccount)
                .stream().anyMatch(account -> account.getFundstatus().equals(Boolean.TRUE));
    }

    @Transactional
    public Integer transferAmount(Integer fromAccount , Integer toAccount , String amount)
    {
        String balance = accountRepository.findByaccountno(fromAccount)
                               .map(acc -> (acc.getBalance()))
                               .collect(Collectors.joining());
        int newBalance = Integer.valueOf(balance)-Integer.valueOf(amount);
        updatefromAccountWithNewBalance(fromAccount,newBalance,amount);


        String toAccountbalance = accountRepository.findByaccountno(toAccount)
                .map(acc -> (acc.getBalance()))
                .collect(Collectors.joining());
        int newBalanceToAccount = Integer.valueOf(toAccountbalance)+Integer.valueOf(amount);
        updateToAccountWithNewBalance(toAccount ,newBalanceToAccount,amount);
        return newBalance;
    }

    public void updatefromAccountWithNewBalance(int fromAccountno , int newBalance,String amount)
    {
        Account fromAccount = accountRepository.findById(fromAccountno).get();
        fromAccount.setBalance(String.valueOf(newBalance));
        accountRepository.save(fromAccount);
        updateDebitTransaction(fromAccountno,fromAccount,amount);
    }

    public void updateToAccountWithNewBalance(int toAccountno, int newBalance,String amount)
    {
        Account toAccount = accountRepository.findById(toAccountno).get();
        toAccount.setBalance(String.valueOf(newBalance));
        accountRepository.save(toAccount);
        updateCreditTransaction(toAccountno,toAccount,amount);
    }

    public void updateDebitTransaction(int fromAccount, Account account, String amount)
    {
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();
        transaction.setOriginaccount(String.valueOf(fromAccount));
        transaction.setTransactiontype("DEBIT");
        transaction.setTransactiondate(new Date());
        transaction.setAmount(amount);
        transactionList.add(transaction);
        account.setTransaction(transactionList);
        accountRepository.save(account);
    }

    public void updateCreditTransaction(int fromAccount, Account toAccount, String amount)
    {
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();
        transaction.setOriginaccount(String.valueOf(fromAccount));
        transaction.setTransactiontype("CREDIT");
        transaction.setTransactiondate(new Date());
        transaction.setAmount(amount);
        transactionList.add(transaction);
        toAccount.setTransaction(transactionList);
        accountRepository.save(toAccount);
    }
}
