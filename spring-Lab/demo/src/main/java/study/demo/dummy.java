package study.demo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Table(name = "DummyTable")
@Entity
public class dummy {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(length = 36, nullable = false, updatable = false)
    private UUID id;

    @Column(length = 100)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 2000)
    private String bookContent;

    public dummy() {
    }

    public dummy(UUID id, String name, String address, String bookContent) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.bookContent = bookContent;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBookContent() {
        return bookContent;
    }

    public void setBookContent(String bookContent) {
        this.bookContent = bookContent;
    }
}
