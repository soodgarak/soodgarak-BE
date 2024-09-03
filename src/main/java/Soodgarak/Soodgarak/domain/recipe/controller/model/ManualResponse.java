package Soodgarak.Soodgarak.domain.recipe.controller.model;

public record ManualResponse(
        Long manualId,
        String manual,
        String manualImgUrl
) {
    public static ManualResponse of(
            Long manualId,
            String manual,
            String manualImgUrl
    ) {
        return new ManualResponse(manualId, manual, manualImgUrl);
    }
}
