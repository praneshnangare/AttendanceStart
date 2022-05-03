package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Item;

public interface ItemDAO extends JpaRepository<Item, Integer>{

	Item findItemByItemId(Integer id);

}
