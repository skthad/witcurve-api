package com.witcurve.repository;

import com.witcurve.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository  extends JpaRepository<Event, Long> {

    @Query("select eve from Event eve  where eve.academicSession.school.id = ?2 and (eve.eventDate = ?1 or (eve.fromDate >= ?1 and eve.toDate <= ?1))")
    List<Event> findEventsByEventDateAndSchoolId(LocalDate localDate, Long schoolId);
}

