// modules/store/domain/Post.java
package com.example.project_al.modules.stores.domain;

import com.example.project_al.shared.kernel.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {

    @Column(name = "post_list_id")
    private Integer postListId;  // From UML: Post_Listnt

    @Column(name = "nom_post")
    private String nomPost;  // From UML: Nom_post.String

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;  // From UML: date_cretionDate

    @Column(name = "type")
    private String type;  // From UML: type.String

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> modClassList = new ArrayList<>();  // From UML: +mod_classlist<String>

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;  // From UML: -Description.String

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;  // From UML: -Content<String

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}