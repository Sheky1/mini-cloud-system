package raf.projekat_back.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String value;
    @ManyToMany(mappedBy = "permissions")
//    @JoinTable(
//            name = "user_permissions_table",
//            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"),
//            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
//    )
    private List<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
