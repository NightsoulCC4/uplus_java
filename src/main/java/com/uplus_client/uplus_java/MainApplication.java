package com.uplus_client.uplus_java;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.uplus_client.uplus_java.Service.AdmitService;
import com.uplus_client.uplus_java.Service.DischargeService;

@EnableScheduling
@SpringBootApplication
public class MainApplication {

	@Autowired
	AdmitService admitService;

	@Autowired
	DischargeService dischargeService;

	private final static Logger logger = LogManager.getLogger(MainApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Scheduled(fixedDelayString = "${scheduled_repeat_time}")
	public void SchedulingSendData() throws InterruptedException {
		logger.info("\nSent data at: " + LocalDateTime.now());
		try {
			admitService.onAdmitService();
			dischargeService.onDischargeService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
