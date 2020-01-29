package com.witcurve.service.mapper;

import com.witcurve.domain.Album;
import com.witcurve.service.dto.AlbumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SchoolInfoMapper.class)
public interface AlbumMapper extends EntityMapper<AlbumDTO, Album> {

    @Mapping(source = "schoolInfo.id", target = "schoolInfoId")
    AlbumDTO toDto(Album album);

    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    Album toEntity(AlbumDTO attributeDTO);

    default Album fromId(Long id) {
        if (id == null) {
            return null;
        }
        Album album = new Album();
        album.setId(id);
        return album;
    }
}
