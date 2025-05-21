package com.chello.milkdelivery.repository;

import com.chello.milkdelivery.model.Message;
import com.chello.milkdelivery.dto.MessageDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByCustomerId(Long customerId);
}