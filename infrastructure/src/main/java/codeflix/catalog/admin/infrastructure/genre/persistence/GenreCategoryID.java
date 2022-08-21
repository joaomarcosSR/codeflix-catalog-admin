package codeflix.catalog.admin.infrastructure.genre.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GenreCategoryID implements Serializable {

    @Column(name = "genre_id", nullable = false)
    private String genreId;

    @Column(name = "category_Id", nullable = false)
    private String categoryId;

    private GenreCategoryID() {
    }

    private GenreCategoryID(final String aGenreId, final String aCategoryId) {
        this.genreId = aGenreId;
        this.categoryId = aCategoryId;
    }

    public static GenreCategoryID from(final String aGenreId, final String aCategoryId) {
        return new GenreCategoryID(aGenreId, aCategoryId);
    }

    public String getGenreId() {
        return this.genreId;
    }

    public void setGenreId(final String genreId) {
        this.genreId = genreId;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(this.getGenreId(), that.getGenreId()) && Objects.equals(this.getCategoryId(), that.getCategoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getGenreId(), this.getCategoryId());
    }
}
