package com.mastercard.plc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.plc.dao.AccountRepository;
import com.mastercard.plc.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class PlcApplication {

	@Autowired
	AccountRepository accountRepository;

	public static void main(String[] args) {
		SpringApplication.run(PlcApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(AccountRepository accountDAO){
	    return args -> {
			// read JSON and load json
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			TypeReference<List<Account>> typeReference = new TypeReference<List<Account>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/Accounts.json");
			try {
				List<Account> account = mapper.readValue(inputStream,typeReference);
				 accountRepository.saveAll(account);
				System.out.println("Accounts Saved!");
			} catch (IOException e){
				System.out.println("Unable to save Accounts: " + e.getMessage());
			}
	    };
	}
}
