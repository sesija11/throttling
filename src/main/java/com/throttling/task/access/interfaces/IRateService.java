package com.throttling.task.access.interfaces;

public interface IRateService {

    Integer getBurstCount();

    Integer getAverageBurstCount();
    
    Integer getRate();
}
