package com.java.xiongzeen.data;


import java.util.ArrayList;
import java.util.List;


public  class User {

    private final String name = "DemoUser";

    public  List<Category> selected = new ArrayList<>();
    public  List<Category> unselected = new ArrayList<>();

    public User() {
        selected.add(Category.教育);
        selected.add(Category.娱乐);
        selected.add(Category.科技);
        selected.add(Category.体育);
        selected.add(Category.健康);
        selected.add(Category.军事);
        selected.add(Category.文化);
        selected.add(Category.汽车);
        selected.add(Category.社会);
        selected.add(Category.财经);
    }
}
