package first.workshop.judgedemo.service.impl;

import first.workshop.judgedemo.model.entity.Role;
import first.workshop.judgedemo.model.entity.RoleEnum;
import first.workshop.judgedemo.repository.RoleRepository;
import first.workshop.judgedemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    private void seedRoles() {
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.saveAll(
                    new ArrayList<>() {{
                        add(new Role(RoleEnum.ADMIN_ROLE));
                        add(new Role(RoleEnum.USER_ROLE));
                    }});
        }
    }

    @Override
    public Optional<Role> getRole(String role) {
        return Optional.of(roleRepository
                .findAll()
                .stream()
                .filter(r -> r.getName().toString().toLowerCase().contains(role.toLowerCase()))
                .findFirst()
                .orElse(new Role(RoleEnum.ANONYMOUS_ROLE)));
    }
}
