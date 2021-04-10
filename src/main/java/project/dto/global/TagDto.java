package project.dto.global;

public class TagDto {

    private final String name;
    private final double weight;

    public TagDto(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

}
