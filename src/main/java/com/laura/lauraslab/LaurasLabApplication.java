package com.laura.lauraslab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
public class LaurasLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaurasLabApplication.class, args);
	}

}
