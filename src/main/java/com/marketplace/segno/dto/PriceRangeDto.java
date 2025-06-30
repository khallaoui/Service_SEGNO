package com.marketplace.segno.dto;


public class PriceRangeDto {
    private Double min;
    private Double max;
    
    public PriceRangeDto() {}
    
    public PriceRangeDto(Double min, Double max) {
        this.min = min;
        this.max = max;
    }
    
    public Double getMin() { return min; }
    public void setMin(Double min) { this.min = min; }
    
    public Double getMax() { return max; }
    public void setMax(Double max) { this.max = max; }
}