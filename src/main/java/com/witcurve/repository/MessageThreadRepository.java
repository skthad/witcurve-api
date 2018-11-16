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
    @Query("select mt from MessageThread mt where mt.courseTeacher.standard.id=?1 and mt.messageType=?2 " +
        "order by mt.lastModifiedDate desc")
    Page<MessageThread> findInboxMessageThreadsOfSubjectNote(Long standardId, MessageType messageType, Pageable pageable);

    @Query("select mt from MessageThread mt where mt.courseTeacher.standard.id=?1 and mt.messageType=?2 " +
        "and mt.approved=?3 order by mt.lastModifiedDate desc")
    Page<MessageThread> findInboxMessageThreadsOfSubjectNoteWithApproved(Long standardId, MessageType messageType, Boolean approved, Pageable pageable);

    @Query("select mt from MessageThread mt where mt.courseTeacher.standard.id=?1 and mt.messageType=?2" +
        " and mt.id <> all (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findReadInboxMessageThreadsOfSubjectNote(Long standardId, MessageType messageType, Pageable pageable);

    @Query("select mt from MessageThread mt where mt.courseTeacher.standard.id=?1 and mt.messageType=?2 " +
        "and mt.id = any (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findUnReadInboxMessageThreadsOfSubjectNote(Long standardId, MessageType messageType, Pageable pageable);

    @Query("select count(mt) from MessageThread mt where mt.courseTeacher.standard.id=?1 and mt.messageType=?2 " +
        "and mt.id = any (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Integer findUnReadInboxMessageThreadsOfSubjectNoteCount(Long standardId, MessageType messageType);

    @Query("select mt from MessageThread mt where mt.courseTeacher.standard.id=?1 and mt.messageType=?2 and mt.approved =?3" +
        " and mt.id <> all (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findReadInboxMessageThreadsOfSubjectNoteWithApproved(Long standardId, MessageType messageType, Boolean approved, Pageable pageable);

    @Query("select mt from MessageThread mt where mt.courseTeacher.standard.id=?1 and mt.messageType=?2 and mt.approved =?3 " +
        "and mt.id = any (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findUnReadInboxMessageThreadsOfSubjectNoteWithApproved(Long standardId, MessageType messageType, Boolean approved, Pageable pageable);



    // list of inbox message for user for type other than subject note
    @Query("select mt from MessageThread mt where mt.toUser.id=?1 and mt.messageType=?2 order by mt.lastModifiedDate desc")
    Page<MessageThread> findInboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select mt from MessageThread mt where mt.toUser.id=?1 and mt.messageType=?2 and mt.approved=?3" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findInboxMessageThreadsWithApproved(Long userId, MessageType messageType, Boolean approved, Pageable pageable);

    @Query("select mt from MessageThread mt where mt.toUser.id=?1 and mt.messageType=?2 " +
        "and mt.id <> all (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findReadInboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select mt from MessageThread mt where mt.toUser.id=?1 and mt.messageType=?2" +
        " and mt.id = any (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findUnReadInboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select count(mt) from MessageThread mt where mt.toUser.id=?1 and mt.messageType=?2" +
        " and mt.id = any (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Integer findUnReadInboxMessageThreadsCount(Long userId, MessageType messageType);

    @Query("select mt from MessageThread mt where mt.toUser.id=?1 and mt.messageType=?2 and mt.approved=?3" +
        " and mt.id <> all (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findReadInboxMessageThreadsWithApproved(Long userId, MessageType messageType, Boolean approved, Pageable pageable);

    @Query("select mt from MessageThread mt where mt.toUser.id=?1 and mt.messageType=?2 and mt.approved=?3" +
        " and mt.id = any (select m.messageThread.id from Message m where m.messageThread.id = mt.id and m.read=false)" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findUnReadInboxMessageThreadsWithApproved(Long userId, MessageType messageType, Boolean approved, Pageable pageable);


    //outbox for a user

    @Query("select mt from MessageThread mt where mt.fromUser.id=?1 and mt.messageType=?2 order by mt.lastModifiedDate desc")
    Page<MessageThread> findOutboxMessageThreads(Long userId, MessageType messageType, Pageable pageable);

    @Query("select mt from MessageThread mt where mt.fromUser.id=?1 and mt.messageType=?2 and mt.approved=?3" +
        " order by mt.lastModifiedDate desc")
    Page<MessageThread> findOutboxMessageThreadsWithApproved(Long userId, MessageType messageType, Boolean approved, Pageable pageable);

}
