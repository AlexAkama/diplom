package project.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "restore_codes")
@NoArgsConstructor
@Getter
@Setter
public class RestoreCode extends Identified{

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Date time;

    private String email;

    public RestoreCode(String code) {
        this.code = code;
        this.time = new Date();
    }

}
