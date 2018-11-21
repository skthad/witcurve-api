package com.witcurve.repository;

import com.witcurve.domain.MessageThread;
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
        "and m.messageThread.approved=?3 and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "order by m.createdDate desc")
    Page<MessageThread> findInboxMessageThreadsOfSubjectNoteWithApproved(Long standardId, MessageType messageType, Boolean approved, Pageable pageable);


    @Query("select m.messageThread from Message m where m.messageThread.courseTeacher.standard.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id <> all (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Page<MessageThread> findReadInboxMessageThreadsOfSubjectNote(Long standardId, MessageType messageType, Pageable pageable);

    @Query("select m.messageThread from Message m where m.messageThread.courseTeacher.standard.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id = any (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Page<MessageThread> findUnReadInboxMessageThreadsOfSubjectNote(Long standardId, MessageType messageType, Pageable pageable);

    @Query("select count(m.messageThread) from Message m where m.messageThread.courseTeacher.standard.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id = any (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Integer findUnReadInboxMessageThreadsOfSubjectNoteCount(Long standardId, MessageType messageType);

    @Query("select m.messageThread from Message m where m.messageThread.courseTeacher.standard.id=?1 " +
        "and m.messageThread.messageType=?2 and m.messageThread.approved=?3 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id <> all (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Page<MessageThread> findReadInboxMessageThreadsOfSubjectNoteWithApproved(Long standardId, MessageType messageType, Boolean approved, Pageable pageable);

    @Query("select m.messageThread from Message m where m.messageThread.courseTeacher.standard.id=?1 " +
        "and m.messageThread.messageType=?2 and m.messageThread.approved=?3 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id = any (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Page<MessageThread> findUnReadInboxMessageThreadsOfSubjectNoteWithApproved(Long standardId, MessageType messageType, Boolean approved, Pageable pageable);


    // list of inbox message for user for type other than subject note
    @Query("select m.messageThread from Message m where m.messageThread.toUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "order by m.createdDate desc")
    Page<MessageThread> findInboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select m.messageThread from Message m where m.messageThread.toUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.messageThread.approved=?3 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "order by m.createdDate desc")
    Page<MessageThread> findInboxMessageThreadsWithApproved(Long userId, MessageType messageType, Boolean approved, Pageable pageable);

    @Query("select m.messageThread from Message m where m.messageThread.toUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id <> all (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Page<MessageThread> findReadInboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select m.messageThread from Message m where m.messageThread.toUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id = any (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Page<MessageThread> findUnReadInboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select count(m.messageThread) from Message m where m.messageThread.toUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id = any (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Integer findUnReadInboxMessageThreadsCount(Long userId, MessageType messageType);

    @Query("select m.messageThread from Message m where m.messageThread.toUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.messageThread.approved=?3 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id <> all (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Page<MessageThread> findReadInboxMessageThreadsWithApproved(Long userId, MessageType messageType, Boolean approved, Pageable pageable);

    @Query("select m.messageThread from Message m where m.messageThread.toUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.messageThread.approved=?3 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "and m.messageThread.id = any (select m2.messageThread.id from Message m2 where m2.messageThread.id = m.messageThread.id and m2.read=false)" +
        "order by m.createdDate desc")
    Page<MessageThread> findUnReadInboxMessageThreadsWithApproved(Long userId, MessageType messageType, Boolean approved, Pageable pageable);


    //outbox for a user

    @Query("select m.messageThread from Message m where m.messageThread.fromUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "order by m.createdDate desc")
    Page<MessageThread> findOutboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select m.messageThread from Message m where m.messageThread.fromUser.id=?1 " +
        "and m.messageThread.messageType=?2 and m.messageThread.approved=?3 and m.lastModifiedDate= " +
        "(select max(m1.lastModifiedDate) from Message m1 where m1.messageThread.id=m.messageThread.id) " +
        "order by m.createdDate desc")
    Page<MessageThread> findOutboxMessageThreadsWithApproved(Long userId, MessageType messageType, Boolean approved, Pageable pageable);

}
