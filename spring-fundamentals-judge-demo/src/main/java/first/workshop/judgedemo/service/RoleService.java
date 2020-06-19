package first.workshop.judgedemo.service;

import first.workshop.judgedemo.model.entity.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> getRole(String role);
}
