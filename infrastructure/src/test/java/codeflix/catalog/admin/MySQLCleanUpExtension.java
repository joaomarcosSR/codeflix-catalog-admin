package codeflix.catalog.admin;

import codeflix.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import codeflix.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import codeflix.catalog.admin.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final ApplicationContext appContext = SpringExtension.getApplicationContext(context);

        this.cleanUp(List.of(
                appContext.getBean(VideoRepository.class),
                appContext.getBean(CastMemberRepository.class),
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class)
        ));
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}