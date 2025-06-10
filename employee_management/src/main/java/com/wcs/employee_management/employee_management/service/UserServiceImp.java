package com.wcs.employee_management.employee_management.service;

import com.wcs.employee_management.employee_management.DTO.UserCreationRequest;
import com.wcs.employee_management.employee_management.DTO.UserResponseDTO;
import com.wcs.employee_management.employee_management.entity.User;
import com.wcs.employee_management.employee_management.exception.InvalidSearchQueryException;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.repository.RoleRepository;
import com.wcs.employee_management.employee_management.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class UserServiceImp implements UserService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userServiceRepository;
    @Autowired
    private RoleRepository rolesRepository;

    @Transactional
    public User createUser(UserCreationRequest request) {
        User user = request.getUser();
        return userServiceRepository.save(user);
    }

    public User getUserById(Integer id) {
        return userServiceRepository.findById(id)
                .orElseThrow(() -> new InvalidUserException("User not found with ID: " + id));
    }

    public List<User> getAllUsers() {

        return userServiceRepository.findAll();
    }

    @Override
    public List<UserResponseDTO> searchByUser(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            throw new InvalidSearchQueryException("Search query must not be empty");
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Join<Object, Object> roleJoin = root.join("role", JoinType.LEFT);

        String likeQuery = "%" + searchText.toLowerCase() + "%";
        Expression<String> id = cb.toString(root.get("Id"));
        Expression<String> convertId = cb.coalesce(id, "");
        Predicate IdPredicate = cb.like(cb.lower(cb.trim(convertId)), likeQuery);
        Predicate namePredicate = cb.like(cb.lower(root.get("name")), likeQuery);
        Predicate emailPredicate = cb.like(cb.lower(root.get("email")), likeQuery);
        Expression<String> phoneExpr = cb.toString(root.get("phone"));
        Expression<String> convertPhoneExpr = cb.coalesce(phoneExpr, "");
        Predicate phonePredicate = cb.like(cb.lower(cb.trim(convertPhoneExpr)), likeQuery);
        Expression<String> roleIdExpr = cb.toString(root.get("roleId"));
        Expression<String> convertRoleIdExpr = cb.coalesce(roleIdExpr, "");
        Predicate roleIdPredicate = cb.like(cb.lower(cb.trim(convertRoleIdExpr)), likeQuery);
        Predicate roleNamePredicate = cb.like(cb.lower(roleJoin.get("name")), likeQuery);
        Predicate roleCodePredicate = cb.like(cb.lower(roleJoin.get("code")),likeQuery);

        cq.where(cb.or(namePredicate, emailPredicate, phonePredicate, roleIdPredicate, roleNamePredicate, IdPredicate,roleCodePredicate));

        List<User> result = entityManager.createQuery(cq).getResultList();
        return result.stream()
                .map(UserResponseDTO::new)
                .toList();

    }

}

