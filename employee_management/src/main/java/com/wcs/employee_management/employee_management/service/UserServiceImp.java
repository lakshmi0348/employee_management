package com.wcs.employee_management.employee_management.service;

import com.wcs.employee_management.employee_management.DTO.ChangeExistingPassword;
import com.wcs.employee_management.employee_management.DTO.ChangePasswordRequest;
import com.wcs.employee_management.employee_management.DTO.UserCreationRequest;
import com.wcs.employee_management.employee_management.DTO.UserResponseDTO;
import com.wcs.employee_management.employee_management.entity.User;
import com.wcs.employee_management.employee_management.exception.InvalidSearchQueryException;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class UserServiceImp implements UserService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @Transactional
    public User createUser(UserCreationRequest request) {
        User user = request.getUser();
        Integer empId = userRepository.getNextUserId();
        String employeeId = String.format("WCS%03d", empId);
        user.setEmployeeId(employeeId);
        String generatedPassword = generatePassword(9);
        user.setPassword(encryptPassword(generatedPassword));
        emailService.sendPasswordEmail(user.getEmail(), generatedPassword);
        return userRepository.save(user);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserException("User not found with ID: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));
    }

    public List<User> findByIsActive(boolean isActive) {
        return userRepository.findByIsUserActive(isActive);
    }

    public List<UserResponseDTO> searchByUser(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            throw new InvalidSearchQueryException("Search query must not be empty");
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Join<Object, Object> roleJoin = root.join("role", JoinType.LEFT);

        List<Predicate> predicates = buildSearchPredicates(cb, root, roleJoin, searchText.toLowerCase());

        cq.where(cb.or(predicates.toArray(new Predicate[0])));
        List<User> users = entityManager.createQuery(cq).getResultList();

        return users.stream().map(UserResponseDTO::new).toList();
    }

    public void resetPasswordWithVerification(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase());
        if (user != null) {
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

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new InvalidUserException("New password must be different from the old password.");
            }
            // Update password
            user.setPassword(new BCryptPasswordEncoder().encode(request.getNewPassword()));
            userRepository.save(user);
        }
    }

    public User partialChangeUser(Integer id, User userPartial) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserException("User not found with ID: " + id));

        if (userPartial.getFirstName() != null) existingUser.setFirstName(userPartial.getFirstName());
        if (userPartial.getLastName() != null) existingUser.setLastName(userPartial.getLastName());
        if (userPartial.getMiddleName() != null) existingUser.setMiddleName(userPartial.getMiddleName());
        if (userPartial.getMaidenName() != null) existingUser.setMaidenName(userPartial.getMaidenName());
        if (userPartial.getPassword() != null && !userPartial.getPassword().isEmpty()) {
            existingUser.setPassword(encryptPassword(userPartial.getPassword()));
        }
        if (userPartial.getEmail() != null) existingUser.setEmail(userPartial.getEmail());
        if (userPartial.getPhone() != null) existingUser.setPhone(userPartial.getPhone());
        if (userPartial.getRoleId() != null) existingUser.setRoleId(userPartial.getRoleId());
        if (userPartial.getEmployeeId() != null) existingUser.setEmployeeId(userPartial.getEmployeeId());
        if (userPartial.getDateOfBirth() != null) existingUser.setDateOfBirth(userPartial.getDateOfBirth());
        if (userPartial.getGender() != null) existingUser.setGender(userPartial.getGender());
        if (userPartial.getJobTitle() != null) existingUser.setJobTitle(userPartial.getJobTitle());
        if (userPartial.getDepartment() != null) existingUser.setDepartment(userPartial.getDepartment());
        if (userPartial.getDateOfJoining() != null) existingUser.setDateOfJoining(userPartial.getDateOfJoining());
        if (userPartial.getEmploymentType() != null) existingUser.setEmploymentType(userPartial.getEmploymentType());
        if (userPartial.getHourlyRate2() != null) existingUser.setHourlyRate2(userPartial.getHourlyRate2());
        if (userPartial.getHourlyRate3() != null) existingUser.setHourlyRate3(userPartial.getHourlyRate3());
        if (userPartial.getBenefitClassCode() != null) existingUser.setBenefitClassCode(userPartial.getBenefitClassCode());
        if (userPartial.getBenefitDescription() != null) existingUser.setBenefitDescription(userPartial.getBenefitDescription());
        if (userPartial.getBenefitAccrualRate() != null) existingUser.setBenefitAccrualRate(userPartial.getBenefitAccrualRate());
        if (userPartial.getBenefitStatus() != null) existingUser.setBenefitStatus(userPartial.getBenefitStatus());
        if (userPartial.getClassCode() != null) existingUser.setClassCode(userPartial.getClassCode());
        if (userPartial.getClassDescription() != null) existingUser.setClassDescription(userPartial.getClassDescription());
        if (userPartial.getClassAccrualRate() != null) existingUser.setClassAccrualRate(userPartial.getClassAccrualRate());
        if (userPartial.getClassStatus() != null) existingUser.setClassStatus(userPartial.getClassStatus());
        if (userPartial.getSupervisorName() != null) existingUser.setSupervisorName(userPartial.getSupervisorName());
        if (userPartial.getSupervisorReport() != null) existingUser.setSupervisorReport(userPartial.getSupervisorReport());
        if (userPartial.getIsSupervisor() != null) existingUser.setIsSupervisor(userPartial.getIsSupervisor());
        if (userPartial.getPhotograph() != null) existingUser.setPhotograph(userPartial.getPhotograph());
        if (userPartial.getMaritalStatus() != null) existingUser.setMaritalStatus(userPartial.getMaritalStatus());
        if (userPartial.getEthnicGroup() != null) existingUser.setEthnicGroup(userPartial.getEthnicGroup());
        if (userPartial.getEeoClass() != null) existingUser.setEeoClass(userPartial.getEeoClass());
        if (userPartial.getSsn() != null) existingUser.setSsn(userPartial.getSsn());
        if (userPartial.getWorkInState() != null) existingUser.setWorkInState(userPartial.getWorkInState());
        if (userPartial.getLiveInState() != null) existingUser.setLiveInState(userPartial.getLiveInState());
        if (userPartial.getCitizenship() != null) existingUser.setCitizenship(userPartial.getCitizenship());
        if (userPartial.getEmergencyContact() != null) existingUser.setEmergencyContact(userPartial.getEmergencyContact());
        if (userPartial.getEmergencyHomePhoneNumber() != null) existingUser.setEmergencyHomePhoneNumber(userPartial.getEmergencyHomePhoneNumber());
        if (userPartial.getEmergencyWorkPhone() != null) existingUser.setEmergencyWorkPhone(userPartial.getEmergencyWorkPhone());
        if (userPartial.getEmergencyContactRelation() != null) existingUser.setEmergencyContactRelation(userPartial.getEmergencyContactRelation());
        if (userPartial.getAlterEmergencyContact() != null) existingUser.setAlterEmergencyContact(userPartial.getAlterEmergencyContact());
        if (userPartial.getAlterEmergencyHomePhoneNumber() != null) existingUser.setAlterEmergencyHomePhoneNumber(userPartial.getAlterEmergencyHomePhoneNumber());
        if (userPartial.getAlterEmergencyWorkPhone() != null) existingUser.setAlterEmergencyWorkPhone(userPartial.getAlterEmergencyWorkPhone());
        if (userPartial.getZipcode() != null) existingUser.setZipcode(userPartial.getZipcode());
        if (userPartial.getHomeEmail() != null) existingUser.setHomeEmail(userPartial.getHomeEmail());
        if (userPartial.getBusinessEmail() != null) existingUser.setBusinessEmail(userPartial.getBusinessEmail());
        if (userPartial.getHomePhone() != null) existingUser.setHomePhone(userPartial.getHomePhone());
        if (userPartial.getBusinessPhone() != null) existingUser.setBusinessPhone(userPartial.getBusinessPhone());
        if (userPartial.getCellPhone() != null) existingUser.setCellPhone(userPartial.getCellPhone());
        if (userPartial.getDivision() != null) existingUser.setDivision(userPartial.getDivision());
        if (userPartial.getPosition() != null) existingUser.setPosition(userPartial.getPosition());
        if (userPartial.getDutyType() != null) existingUser.setDutyType(userPartial.getDutyType());
        if (userPartial.getHireDate() != null) existingUser.setHireDate(userPartial.getHireDate());
        if (userPartial.getOriginalHireDate() != null) existingUser.setOriginalHireDate(userPartial.getOriginalHireDate());
        if (userPartial.getTerminationDate() != null) existingUser.setTerminationDate(userPartial.getTerminationDate());
        if (userPartial.getTerminationReason() != null) existingUser.setTerminationReason(userPartial.getTerminationReason());
        if (userPartial.getVoluntaryTermination() != null) existingUser.setVoluntaryTermination(userPartial.getVoluntaryTermination());
        if (userPartial.getRehireDate() != null) existingUser.setRehireDate(userPartial.getRehireDate());
        if (userPartial.getRateType() != null) existingUser.setRateType(userPartial.getRateType());
        if (userPartial.getRate() != null) existingUser.setRate(userPartial.getRate());
        if (userPartial.getPayFrequency() != null) existingUser.setPayFrequency(userPartial.getPayFrequency());
        if (userPartial.getPayFrequencyText() != null) existingUser.setPayFrequencyText(userPartial.getPayFrequencyText());
        if (userPartial.getHomeDepartment() != null) existingUser.setHomeDepartment(userPartial.getHomeDepartment());
        if (userPartial.getDepartmentText() != null) existingUser.setDepartmentText(userPartial.getDepartmentText());
        if (userPartial.getCity() != null) existingUser.setCity(userPartial.getCity());
        if (userPartial.getCountry() != null) existingUser.setCountry(userPartial.getCountry());

        return userRepository.save(existingUser);
    }


    public User updateUser(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserException("User not found with ID: " + id));

        // Update fields
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setDepartment(updatedUser.getDepartment());
        existingUser.setJobTitle(updatedUser.getJobTitle());
        existingUser.setDateOfJoining(updatedUser.getDateOfJoining());
        existingUser.setEmploymentType(updatedUser.getEmploymentType());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(encryptPassword(updatedUser.getPassword()));
        }
        if (updatedUser.getRoleId() != null) {
            existingUser.setRoleId(updatedUser.getRoleId());
        }

        return userRepository.save(existingUser);
    }

    public void markUserAsInactive(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setUserActive(false);
        userRepository.save(user);
    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public ResponseEntity<?> changePassword(ChangeExistingPassword request) {

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password cannot be the same as the old password.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully.");
    }

    private List<Predicate> buildSearchPredicates(
            CriteriaBuilder cb,
            Root<User> root,
            Join<Object, Object> roleJoin,
            String searchText) {

        String likeQuery = "%" + searchText.toLowerCase() + "%";
        List<Predicate> predicates = new ArrayList<>();

        // Basic user fields
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("firstName"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("lastName"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("email"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("phone")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("id")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("employeeId"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("jobTitle"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("department"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("gender"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("employmentType"), ""))), likeQuery));

        // Supervisor details
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("supervisorName"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("supervisorReport"), ""))), likeQuery));

        // Hourly rates
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("hourlyRate2")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("hourlyRate3")), ""))), likeQuery));

        // Benefits
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("benefitClassCode")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("benefitDescription"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("benefitAccrualRate")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("benefitStatus"), ""))), likeQuery));

        // Class-related
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("classCode")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("classDescription"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("classAccrualRate")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("classStatus"), ""))), likeQuery));

        // Personal info
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("maritalStatus"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("ethnicGroup"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("citizenship"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("ssn")), ""))), likeQuery));

        // Emergency contacts
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("emergencyContact"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("emergencyContactRelation"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("emergencyHomePhoneNumber")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("emergencyWorkPhone")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("alterEmergencyContact")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("alterEmergencyHomePhoneNumber")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("alterEmergencyWorkPhone")), ""))), likeQuery));
        // Boolean fields
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("workInState")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("liveInState")), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(cb.function("TO_CHAR", String.class, root.get("isSupervisor")), ""))), likeQuery));
        // Zipcode (new field)
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(root.get("zipcode"), ""))), likeQuery));
        // Role join fields
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(roleJoin.get("name"), ""))), likeQuery));
        predicates.add(cb.like(cb.lower(cb.trim(cb.coalesce(roleJoin.get("code"), ""))), likeQuery));

        return predicates;
    }



        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        private static final SecureRandom random = new SecureRandom();

        public static String generatePassword(int length) {
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int rndCharAt = random.nextInt(CHARACTERS.length());
                char rndChar = CHARACTERS.charAt(rndCharAt);
                sb.append(rndChar);
            }
            return sb.toString();
        }



}

