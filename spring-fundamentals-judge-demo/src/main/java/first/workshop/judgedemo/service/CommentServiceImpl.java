package first.workshop.judgedemo.service;

import first.workshop.judgedemo.model.entity.Homework;
import first.workshop.judgedemo.model.entity.User;
import first.workshop.judgedemo.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(ModelMapper modelMapper, CommentRepository commentRepository) {
        this.modelMapper = modelMapper;
        this.commentRepository = commentRepository;
    }
}
