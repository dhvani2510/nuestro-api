package com.example.nuestro.configurations;

import com.example.nuestro.services.SynchronizeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SynchronizationScheduler { //NOT USED
    @Autowired
    private SynchronizeService synchronizeService;
    private static final Logger logger= LoggerFactory.getLogger(SynchronizationScheduler.class);

    public  SynchronizationScheduler(){
    }
    @Scheduled(fixedRate = 60000) // Synchronize every minute, change the interval as needed
    public void synchronizeData() {
        try {
            logger.info("Synchronization is running");
            synchronizeService.synchronizeData();
        } catch (Exception e) {
            // Handle the exception appropriately
            logger.error("Error occurred during synchronization", e);
        }
    }
}
