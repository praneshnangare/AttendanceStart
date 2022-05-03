package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.ItemEntry;

public interface ItemEntryDAO extends JpaRepository<ItemEntry, Integer>{

}
