package project.dto.global;

/**
 * <h3>Объект тега</h3>
 * <tr>{@link TagDto#name}<td></td><td>Имя тега</td></tr>
 * <tr>{@link TagDto#weight}<td></td><td>Вес тега</td></tr>
 */
public class TagDto {

    /**
     * Имя тега
     */
    private final String name;

    /**
     * Вес тега
     */
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
