package com.witcurve.repository;

import com.witcurve.domain.MessageThread;
import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.MessageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageThreadRepository extends JpaRepository<MessageThread, Long> {

    // list of inbox message for student user for type subject note
    @Query("select m.messageThread from Message m where m.messageThread.courseTeacher.standard.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "order by m.createdDate desc")
    Page<MessageThread> findInboxMessageThreadsOfSubjectNote(Long standardId, MessageType messageType, Pageable pageable);

    @Query("select m.messageThread from Message m where m.messageThread.courseTeacher.standard.id=?1 " +
        "and m.messageThread.status=?3 and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "order by m.createdDate desc")
    Page<MessageThread> findInboxMessageThreadsOfSubjectNoteWithStatus(Long standardId, MessageType messageType, ApprovalStatus status, Pageable pageable);

    @Query("select count(m) from Message m where m.messageThread.courseTeacher.standard.id=?1 " +
        "and m.read=false and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) ")
    Integer findInboxMessageThreadsOfSubjectNoteCount(Long standardId, MessageType messageType);


    // list of inbox message for user for type other than subject note
    @Query("select m.messageThread from Message m where m.toUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id and m1.toUser.id=?1) " +
        "order by m.createdDate desc")
    Page<MessageThread> findOtherInboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select m.messageThread from Message m where m.toUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.messageThread.status=?3 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id and m1.toUser.id=?1) " +
        "order by m.createdDate desc")
    Page<MessageThread> findOtherInboxMessageThreadsWithStatus(Long userId, MessageType messageType, ApprovalStatus status, Pageable pageable);

    @Query("select m.messageThread from Message m where m.toUser.id=?1 " +
        "and m.read=?3 and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id and m1.toUser.id=?1) " +
        "order by m.createdDate desc")
    Page<MessageThread> findOtherInboxMessageThreadsWithRead(Long userId, MessageType messageType, Boolean read, Pageable pageable);

    @Query("select count(m.messageThread) from Message m where m.toUser.id=?1 " +
        "and m.read=false and m.messageThread.messageType=?2")
    Integer findUnReadOtherInboxMessageThreadsCount(Long userId, MessageType messageType);

    @Query("select m.messageThread from Message m where m.toUser.id=?1 " +
        "and m.read=?4 and m.messageThread.messageType=?2 and m.messageThread.status=?3 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id and m1.toUser.id=?1) " +
        "order by m.createdDate desc")
    Page<MessageThread> findOtherInboxMessageThreadsWithStatusAndRead(Long userId, MessageType messageType, ApprovalStatus status, Boolean read, Pageable pageable);

    //outbox for a user

    @Query("select m.messageThread from Message m where m.fromUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id and m1.fromUser.id=?1) " +
        "order by m.createdDate desc")
    Page<MessageThread>
    findOutboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select m.messageThread from Message m where m.fromUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.messageThread.status=?3 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id and m1.fromUser.id=?1) " +
        "order by m.createdDate desc")
    Page<MessageThread> findOutboxMessageThreadsWithStatus(Long userId, MessageType messageType, ApprovalStatus status, Pageable pageable);

}
