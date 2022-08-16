package codeflix.catalog.admin.application._shared.base;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN anIn);
}