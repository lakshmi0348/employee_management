package com.wcs.employee_management.employee_management.service;

import com.wcs.employee_management.employee_management.DTO.ChangePasswordRequest;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


@Slf4j
@Service
public class UserServiceImp implements UserService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository rolesRepository;

    @Transactional
    public User createUser(UserCreationRequest request) {
        User user = request.getUser();
        return userRepository.save(user);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserException("User not found with ID: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByIsActive(boolean isActive) {
        return userRepository.findByIsUserActive(isActive);
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
        Predicate namePredicate = cb.like(cb.lower(root.get("fullName")), likeQuery);
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


    public void resetPasswordWithVerification(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase());
        if(user!=null) {
            // Parse and clean inputs
            LocalDate inputDob;
            try {
                inputDob = LocalDate.parse(request.getDateOfBirth().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                throw new InvalidUserException("Invalid date format. Use yyyy-MM-dd.");
            }

            String inputPhone = request.getPhoneNumber().trim();
            String dbPhone = user.getPhone() != null ? String.valueOf(user.getPhone()).trim() : "";

            System.out.println("Input DOB: " + inputDob + " | DB DOB: " + user.getDateOfBirth());
            System.out.println("Input Phone: " + inputPhone + " | DB Phone: " + dbPhone);

            if (!dbPhone.equals(inputPhone) || !user.getDateOfBirth().equals(inputDob)) {
                throw new InvalidUserException("Phone number or date of birth does not match.");
            }

            // Update password
            user.setPassword(new BCryptPasswordEncoder().encode(request.getNewPassword()));
            userRepository.save(user);
        }
    }



    public User partialChangeUser(Integer id, User userPatch) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserException("User not found with ID: " + id));

        if (userPatch.getFullName() != null) existingUser.setFullName(userPatch.getFullName());
        if (userPatch.getLastName() != null) existingUser.setLastName(userPatch.getLastName());
        if (userPatch.getDateOfBirth() != null) existingUser.setDateOfBirth(userPatch.getDateOfBirth());
        if (userPatch.getGender() != null) existingUser.setGender(userPatch.getGender());
        if (userPatch.getDepartment() != null) existingUser.setDepartment(userPatch.getDepartment());
        if (userPatch.getJobTitle() != null) existingUser.setJobTitle(userPatch.getJobTitle());
        if (userPatch.getDateOfJoining() != null) existingUser.setDateOfJoining(userPatch.getDateOfJoining());
        if (userPatch.getEmploymentType() != null) existingUser.setEmploymentType(userPatch.getEmploymentType());
        if (userPatch.getPhone() != null) existingUser.setPhone(userPatch.getPhone());
        if (userPatch.getEmail() != null) existingUser.setEmail(userPatch.getEmail());

        if (userPatch.getPassword() != null && !userPatch.getPassword().isEmpty()) {
            existingUser.setPassword(encryptPassword(userPatch.getPassword()));
        }

        if (userPatch.getRoleId() != null) {
            existingUser.setRoleId(userPatch.getRoleId());
        }

        return userRepository.save(existingUser);
    }


    public User updateUser(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserException("User not found with ID: " + id));

        // Update fields
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setDepartment(updatedUser.getDepartment());
        existingUser.setJobTitle(updatedUser.getJobTitle());
        existingUser.setDateOfJoining(updatedUser.getDateOfJoining());
        existingUser.setEmploymentType(updatedUser.getEmploymentType());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setEmail(updatedUser.getEmail());

        // Only update password if a new one is provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(encryptPassword(updatedUser.getPassword()));
        }

        // Update roleId if provided
        if (updatedUser.getRoleId() != null) {
            existingUser.setRoleId(updatedUser.getRoleId());
        }

        return userRepository.save(existingUser);
    }

    public void markUserAsInactive(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        user.setUserActive(false);  // or user.setActive(false);
        userRepository.save(user);
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }


}

