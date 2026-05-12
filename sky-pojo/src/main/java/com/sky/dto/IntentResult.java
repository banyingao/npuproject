package com.sky.dto;

public class IntentResult {

    private String action;     // search_dish / create_order
    private String category;   // chicken
    private Boolean spicy;     // true/false
    private Double maxPrice;   // 价格
    private Long dishId;       // 下单用

    public void setAction(String action) {
        this.action = action;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setSpicy(Boolean spicy) {
        this.spicy = spicy;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAction() {
        return action;
    }

    public String getCategory() {
        return category;
    }

    public Boolean getSpicy() {
        return spicy;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Long getDishId() {
        return dishId;
    }
}
