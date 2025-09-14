package com.carolin.libraryproject.event.eventListener;

import com.carolin.libraryproject.event.UserRegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserRegistrationEventListener {

   private static final Logger logger = LoggerFactory.getLogger(UserRegistrationEventListener.class);

   @EventListener
   public void onUserRegistered(UserRegistrationEvent event) {

      var data = event.getUserData();

      Map<String, Object> logData = new HashMap<>();
      logData.put("event", "UserRegistered");
      logData.put("email", data.getEmail());
      logData.put("role", data.getRole());
      logData.put("regiseredAt", data.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

      logger.info("User registered: {}", logData);
   }

}
