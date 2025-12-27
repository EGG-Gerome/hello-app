package cloud.tangyuan.hellocommon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Name implements Serializable {
    private String firstname;
    private String lastname;
}