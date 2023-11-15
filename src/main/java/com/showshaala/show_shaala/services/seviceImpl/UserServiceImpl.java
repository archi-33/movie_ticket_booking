package com.showshaala.show_shaala.services.seviceImpl;

import com.showshaala.show_shaala.entities.User;
import com.showshaala.show_shaala.payload.ServiceResponse;
import com.showshaala.show_shaala.payload.UserDto;
import com.showshaala.show_shaala.payload.requestDto.UpdateUserDetailsDto;
import com.showshaala.show_shaala.payload.requestDto.UserEntryDto;
import com.showshaala.show_shaala.repositories.UserRepo;
import com.showshaala.show_shaala.services.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  /**
   * The default role assigned to new users.
   */
  public static final String DEFAULT_ROLE = "ROLE_USER";

  /**
   * The password encoder used to hash and verify passwords.
   */
  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * The repository for accessing User entities.
   */
  @Autowired
  private UserRepo userRepo;

  @Autowired
  private ModelMapper modelMapper;

  /**
   * Creates a new user in the system.
   *
   * @param userEntryDto The user entity to create.
   * @return A ServiceResponse containing user information if successful, an error message
   * otherwise.
   */
  @Override
  public ServiceResponse<UserDto> createUser(UserEntryDto userEntryDto) {

    Optional<User> userBySameMail = userRepo.findByEmail(userEntryDto.getEmail());
    Optional<User> userBySameUsername = userRepo.findByUsername(userEntryDto.getUsername());

    if (userBySameUsername.isEmpty() && userBySameMail.isEmpty()) {
      User user = User.builder()
          .firstName(userEntryDto.getFirstName())
          .lastName(userEntryDto.getLastName())
          .gender(userEntryDto.getGender())
          .email(userEntryDto.getEmail())
          .password(passwordEncoder.encode(userEntryDto.getPassword()))
          .build();
      user.setRole(DEFAULT_ROLE);
      User savedUser = userRepo.save(user);

//      UserDto userDto = new UserDto();
//      userDto.setEmail(savedUser.getEmail());
//      userDto.setFirstName(savedUser.getFirstName());
//      userDto.setLastName(savedUser.getLastName());
//      userDto.setGender(savedUser.getGender());
//      userDto.setId(savedUser.getId());
      UserDto userDto = modelMapper.map(user, UserDto.class);
      return new ServiceResponse<>(true, userDto,
          "Successfully created the user... PLEASE LOGIN!!!!!");
    } else if (userBySameMail.isPresent() && userBySameUsername.isPresent()) {
      return new ServiceResponse<>(false, null,
          "The specified Email id and username is already present");
    } else if (userBySameMail.isPresent()) {
      return new ServiceResponse<>(false, null, "The specified Email id is already present");
    } else {
      return new ServiceResponse<>(false, null, "The specified username is already present");
    }
  }

  /**
   * Authenticates a user based on email and password.
   *
   * @param email    The email of the user.
   * @param password The user's password.
   * @return A ServiceResponse containing user information if authentication is successful, an error
   * message otherwise.
   */
  @Override
  public ServiceResponse<UserDto> getUser(String email, String password) {

    Optional<User> userOpt = userRepo.findByEmail(email);

    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (passwordEncoder.matches(password, user.getPassword())) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setGender(user.getGender());
        userDto.setId(user.getUserId());
        return new ServiceResponse<>(true, userDto, "User logged in successfully.");

      } else {
        return new ServiceResponse<>(false, null, "Entered Password is wrong");
      }
    } else {
      return new ServiceResponse<>(false, null, "Entered Email ID does not exist");
    }
  }

  /**
   * Retrieves the logged-in user based on their email.
   *
   * @param email The email of the user.
   * @return The User object representing the logged-in user.
   */
  public User getLoggedInUser(String email) {
    return userRepo.findByEmail(email).get();
  }

  /**
   * An array of role names that are considered as admin roles.
   */
  public static final String[] adminAccess = {"ROLE_ADMIN"};

  /**
   * Retrieves the roles of the logged-in user based on their email.
   *
   * @param email The email of the user.
   * @return A list of roles assigned to the user, including admin roles if applicable.
   */
  public List<String> getRolesOfLoggedInUser(String email) {
    String roles = getLoggedInUser(email).getRole();
    List<String> assignRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());
    if (assignRoles.contains("ROLE_ADMIN")) {
      return Arrays.stream(adminAccess).collect(Collectors.toList());
    } else {
      return List.of(new String[]{"ROLE_USER"});
    }
  }

  /**
   * Assigns a new role to a user.
   *
   * @param email     The email of the user to whom the role is assigned.
   * @param userRole  The new role to be assigned.
   * @param principal The Principal object representing the currently logged-in user.
   * @return A ServiceResponse indicating whether the role assignment was successful and providing
   * details.
   */
  public ServiceResponse<User> giveAccess(String email, String userRole, Principal principal) {
    Optional<User> user = userRepo.findByEmail(email);
    if (user.isPresent()) {

      List<String> activeRoles = getRolesOfLoggedInUser(email);
//      String newRole = "";

      if (!(activeRoles.contains(userRole))) {

//        newRole = user.get().getRole() + "," + userRole;
        user.get().setRole(userRole);
        userRepo.save(user.get());
        return new ServiceResponse<>(true, user.get(),
            "Hello!!! " + user.get().getEmail() + ". New Role has been assigned to you by "
                + principal.getName() + "i.e., " + user.get().getRole());

      } else {
        return new ServiceResponse<>(false, user.get(), "The user is already " + userRole);
      }

    }
    return new ServiceResponse<>(false, null, "cannot find the user with specified mail..!!!!");

  }

  /**
   * Loads all user details as UserDto objects.
   *
   * @return A list of UserDto objects containing user details.
   */
  @Override
  public List<UserDto> loadAll() {

    List<User> list = userRepo.findAll();
    List<UserDto> newList = new ArrayList<>();

    for (int i = 0; i < list.size(); i++) {

      newList.add(new UserDto(list.get(i)));

    }

    return newList;
  }

  /**
   * Updates user details.
   *
   * @param updateUserDetailsDto The updated user details.
   * @param principal            The Principal object representing the currently logged-in user.
   * @return A ServiceResponse indicating whether the update was successful and providing details.
   */
  @Override
  public ServiceResponse<UserDto> update(UpdateUserDetailsDto updateUserDetailsDto,
      Principal principal) {
    User loggedinUser = userRepo.findByEmail(principal.getName()).get();
    Optional<User> checkNewMail = userRepo.findByEmail(updateUserDetailsDto.getUpdatedMail());
    if (checkNewMail.isEmpty()) {
      loggedinUser.setEmail(updateUserDetailsDto.getUpdatedMail());
      loggedinUser.setFirstName(updateUserDetailsDto.getUpdatedFirstName());
      loggedinUser.setLastName(updateUserDetailsDto.getUpdatedLastName());
      loggedinUser.setPassword(passwordEncoder.encode(updateUserDetailsDto.getUpdatedPassword()));
      userRepo.save(loggedinUser);

    } else {
      return new ServiceResponse<>(false, null, "Given mail id is already present...!!!!");

    }
    return new ServiceResponse<>(true, new UserDto(updateUserDetailsDto),
        "The user details are successfully updated...");

  }

  /**
   * Updates any user's details.
   *
   * @param id                   The id of the user whose details needs to be updated.
   * @param updateUserDetailsDto The updated user details.
   * @param principal            The Principal object representing the currently logged-in user.
   * @return A ServiceResponse indicating whether the update was successful and providing details.
   */
  @Override
  public ServiceResponse<UserDto> updateAnyUser(Long id, UpdateUserDetailsDto updateUserDetailsDto,
      Principal principal) {
    Optional<User> updateDetailsOfUser = userRepo.findById(id);
    if (updateDetailsOfUser.isEmpty()) {
      return new ServiceResponse<>(false, null,
          "Given id of the user is not present.PLEASE CHECK THE ID OF THE USER TO BE UPDATED..!!!!");
    }
    Optional<User> checkNewMail = userRepo.findByEmail(updateUserDetailsDto.getUpdatedMail());
    if (checkNewMail.isEmpty()) {
      updateDetailsOfUser.get().setEmail(updateUserDetailsDto.getUpdatedMail());
      updateDetailsOfUser.get().setFirstName(updateUserDetailsDto.getUpdatedFirstName());
      updateDetailsOfUser.get().setLastName(updateUserDetailsDto.getUpdatedLastName());
      updateDetailsOfUser.get()
          .setPassword(passwordEncoder.encode(updateUserDetailsDto.getUpdatedPassword()));
      userRepo.save(updateDetailsOfUser.get());

    } else {
      return new ServiceResponse<>(false, null,
          "Given mail id to be updated is already present.TRY ANOTHER MAIL..!!!!");

    }
    return new ServiceResponse<>(true, new UserDto(updateUserDetailsDto),
        "The user details of " + updateDetailsOfUser.get().getEmail()
            + " are successfully updated by " + principal.getName());


  }
}
