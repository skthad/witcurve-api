package com.witcurve.repository;

import com.witcurve.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("Select album from Album album where album.schoolInfo.id = ?1 order by album.createdDate desc")
    List<Album> getBySchoolInfoId(Long schoolInfoId);


}
