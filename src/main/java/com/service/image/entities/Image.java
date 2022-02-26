package com.service.image.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private Long size;

    @Column(name = "hash", nullable = false, unique = true, length = 44)
    private String hash;

    @Lob
    @Column(name = "photo", columnDefinition="mediumblob", nullable = false)
    private byte[] photo;

    protected Image() {
    }

    public Image(String name, String type, Long size, String hash, byte[] photo) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.hash = hash;
        this.photo = photo;
    }

    public Image(Long id, String name, String type, Long size, String hash, byte[] photo) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.hash = hash;
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Long getSize() {
        return size;
    }

    public String getHash() {
        return hash;
    }

    public byte[] getPhoto() {
        return photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image1 = (Image) o;
        return id.equals(image1.id) &&
                Objects.equals(name, image1.name) &&
                Objects.equals(type, image1.type) &&
                Objects.equals(size, image1.size) &&
                hash.equals(image1.hash) &&
                Arrays.equals(photo, image1.photo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, type, size, hash);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", hash='" + hash + '\'' +
                ", image=" + Arrays.toString(photo) +
                '}';
    }

}
