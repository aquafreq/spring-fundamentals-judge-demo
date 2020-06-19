package first.workshop.judgedemo.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class CommentBindingModel {
    private Integer score;
    private String textContent;
}
