package project.dto.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h3>Объект тега</h3>
 * <tr>{@link TagDto#name}<td></td><td>Имя тега</td></tr>
 * <tr>{@link TagDto#weight}<td></td><td>Вес тега</td></tr>
 */
@AllArgsConstructor
@Getter
public class TagDto {

    /**
     * Имя тега
     */
    private final String name;

    /**
     * Вес тега
     */
    private final double weight;

}
