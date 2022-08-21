package codeflix.catalog.admin.infrastructure.genre.persistence;

import codeflix.catalog.admin.domain.category.value.object.CategoryID;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "genres_categories")
public class GenreCategoryJpaEntity {

    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJpaEntity genre;

    private GenreCategoryJpaEntity() {
    }

    private GenreCategoryJpaEntity(final GenreJpaEntity aGenre, final CategoryID aCategoryID) {
        this.id = GenreCategoryID.from(aGenre.getId(), aCategoryID.getValue());
        this.genre = aGenre;
    }

    public static GenreCategoryJpaEntity from(final GenreJpaEntity aGenre, final CategoryID aCategoryID) {
        return new GenreCategoryJpaEntity(aGenre, aCategoryID);
    }

    public GenreCategoryID getId() {
        return this.id;
    }

    public void setId(final GenreCategoryID id) {
        this.id = id;
    }

    public GenreJpaEntity getGenre() {
        return this.genre;
    }

    public void setGenre(final GenreJpaEntity genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
        return Objects.equals(this.getId(), that.getId()) && Objects.equals(this.getGenre(), that.getGenre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getGenre());
    }
}
