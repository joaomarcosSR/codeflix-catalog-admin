package codeflix.catalog.admin.infrastructure.configuration.usecases;

import codeflix.catalog.admin.application.genre.create.CreateGenreUseCase;
import codeflix.catalog.admin.application.genre.create.CreateGenreUseCaseImpl;
import codeflix.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import codeflix.catalog.admin.application.genre.delete.DeleteGenreUseCaseImpl;
import codeflix.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import codeflix.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCaseImpl;
import codeflix.catalog.admin.application.genre.retrieve.list.ListGenreUseCase;
import codeflix.catalog.admin.application.genre.retrieve.list.ListGenreUseCaseImpl;
import codeflix.catalog.admin.application.genre.update.UpdateGenreUseCase;
import codeflix.catalog.admin.application.genre.update.UpdateGenreUseCaseImpl;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.genre.gateway.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public GenreUseCaseConfig(final GenreGateway genreGateway, final CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new CreateGenreUseCaseImpl(this.categoryGateway, this.genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new UpdateGenreUseCaseImpl(this.categoryGateway, this.genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new GetGenreByIdUseCaseImpl(this.genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenresUseCase() {
        return new ListGenreUseCaseImpl(this.genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DeleteGenreUseCaseImpl(this.genreGateway);
    }
}
