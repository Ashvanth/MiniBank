package com.mastercard.plc.dao;

import com.mastercard.plc.model.Account;
import com.mastercard.plc.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface AccountRepository extends CrudRepository<Account,Integer> {

    @Transactional
   Stream <Account> findByaccountno(int accountno);

    Transaction findBytransaction(int accountno);

}
