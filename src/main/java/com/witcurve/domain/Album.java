package com.witcurve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "album")
public class Album extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50, message="The field name must be less than {max} characters")
    @Column(nullable = false)
    private String name;

    @NotNull
    @Size(max = 255, message="The field description must be less than {max} characters")
    @Column(nullable = false)
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SchoolInfo schoolInfo;

    @NotNull
    @JsonIgnore
    @OneToMany
    @JoinTable(
        name = "album_attachments",
        joinColumns = {@JoinColumn(name = "album_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "attachment_id", referencedColumnName = "id")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Attachment> photos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public List<Attachment> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Attachment> photos) {
        this.photos = photos;
    }

    public void addAttachment(List<Attachment> photo) {
        if (this.photos == null) {
            this.photos = new ArrayList<>();
        }
        this.photos.addAll(photo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(id, album.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Album{" +
            "id=" + id +
            '}';
    }
}
