package first.workshop.judgedemo.model.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentServiceAddModel {
    private Integer score;
    private String textContent;
    private String userId;
}
