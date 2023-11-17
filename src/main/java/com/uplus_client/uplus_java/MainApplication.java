package com.uplus_client.uplus_java;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.uplus_client.uplus_java.Service.Token.RequestTokenService;
import com.uplus_client.uplus_java.Service.U_Plus.AdmitService;
import com.uplus_client.uplus_java.Service.U_Plus.DischargeService;

@EnableScheduling
@SpringBootApplication
public class MainApplication {

	private boolean is_token_expire = true;
	private String access_token = null;

	@Autowired
	AdmitService admitService;

	@Autowired
	DischargeService dischargeService;

	@Autowired
	RequestTokenService requestTokenService;

	private final static Logger log = LogManager.getLogger(MainApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Scheduled(fixedDelayString = "${scheduled_repeat_time}")
	public void SchedulingSendData() throws InterruptedException, NullPointerException {
		log.info("\nSent data at: " + LocalDateTime.now());
		try {

			Map<String, Object> token = null;

			// Check token expire.
			if (is_token_expire) {
				token = requestTokenService.requestToken();
				access_token = token.get("access_token").toString();
				is_token_expire = false;
			}

			// Check 401 Unauthorized.
			if (!admitService.onAdmitService(access_token).getStatusCode().toString().equals("200 OK")
					|| !dischargeService.onDischargeService(access_token).getStatusCode().toString().equals("200 OK")) {
				log.info("\n Some data changed.");
				token = requestTokenService.requestToken();
				is_token_expire = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
