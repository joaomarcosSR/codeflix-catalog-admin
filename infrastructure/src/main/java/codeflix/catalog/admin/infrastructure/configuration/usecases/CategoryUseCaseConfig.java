package codeflix.catalog.admin.infrastructure.configuration.usecases;

import codeflix.catalog.admin.application.category.create.CreateCategoryUseCase;
import codeflix.catalog.admin.application.category.create.CreateCategoryUseCaseImpl;
import codeflix.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import codeflix.catalog.admin.application.category.delete.DeleteCategoryUseCaseImpl;
import codeflix.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import codeflix.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCaseImpl;
import codeflix.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import codeflix.catalog.admin.application.category.retrieve.list.ListCategoriesUseCaseImpl;
import codeflix.catalog.admin.application.category.update.UpdateCategoryUseCase;
import codeflix.catalog.admin.application.category.update.UpdateCategoryUseCaseImpl;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new CreateCategoryUseCaseImpl(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new UpdateCategoryUseCaseImpl(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new GetCategoryByIdUseCaseImpl(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new ListCategoriesUseCaseImpl(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DeleteCategoryUseCaseImpl(categoryGateway);
    }
}
