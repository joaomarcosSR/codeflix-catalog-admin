package codeflix.catalog.admin.infrastructure.category.persistence;

import codeflix.catalog.admin.MySQLGatewayTest;
import codeflix.catalog.admin.domain.category.entity.Category;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAnInvalidNullName_WhenCallsSave_ThenReturnError() {
        final String expectedPropertyName = "name";
        final String expectedMessage = "not-null property references a null or transient value : codeflix.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.name";

        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final CategoryJpaEntity anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

        Assertions.assertNull(anEntity.getName());

        DataIntegrityViolationException actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        PropertyValueException actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullCreatedAt_WhenCallsSave_ThenReturnError() {
        final String expectedPropertyName = "createdAt";
        final String expectedMessage = "not-null property references a null or transient value : codeflix.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final CategoryJpaEntity anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        Assertions.assertNull(anEntity.getCreatedAt());

        DataIntegrityViolationException actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        PropertyValueException actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullUpdatedAt_WhenCallsSave_ThenReturnError() {
        final String expectedPropertyName = "updatedAt";
        final String expectedMessage = "not-null property references a null or transient value : codeflix.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final CategoryJpaEntity anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        Assertions.assertNull(anEntity.getUpdatedAt());

        DataIntegrityViolationException actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        PropertyValueException actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }
}
