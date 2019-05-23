package com.witcurve.repository;

import com.witcurve.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Query("delete from Message m where m.messageThread.id=?1")
    void deleteByMessageThreadId(Long messageThreadId);

}
