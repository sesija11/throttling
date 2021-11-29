package com.throttling.task.access.interfaces;

public interface IRateController {

    Integer getBurstCount();

    Integer getAverageBurstCount();
    
    Integer getRate();
}
