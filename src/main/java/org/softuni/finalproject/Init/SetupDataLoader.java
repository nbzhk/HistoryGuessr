package org.softuni.finalproject.Init;

import org.softuni.finalproject.model.entity.PictureEntity;
import org.softuni.finalproject.model.entity.UserEntity;
import org.softuni.finalproject.model.entity.UserRoleEntity;
import org.softuni.finalproject.model.enums.UserRoleEnum;
import org.softuni.finalproject.repository.PictureRepository;
import org.softuni.finalproject.repository.RolesRepository;
import org.softuni.finalproject.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PictureRepository pictureRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository, RolesRepository rolesRepository, PictureRepository pictureRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.pictureRepository = pictureRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup){
            return;
        }
        createAdminRoleIfNotFound();
        createUserRoleIfNotFound();
        createAdminIfNotExist();
        addPictureDataIfNotFound();

        alreadySetup = true;

    }

    public void createAdminIfNotExist() {
        Optional<UserEntity> admin = this.userRepository.findByUsername("admin");

        if (admin.isEmpty()) {
            UserRoleEntity adminRole = rolesRepository.findByUserRole(UserRoleEnum.ADMIN);
            UserRoleEntity userRole = rolesRepository.findByUserRole(UserRoleEnum.USER);
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmail("admin@admin.com");
            user.setRegistrationDate(LocalDate.now());
            user.setUserRoles(List.of(adminRole, userRole));

            userRepository.save(user);
        }
    }

    @Transactional
   public void createAdminRoleIfNotFound() {
        UserRoleEntity adminRole = rolesRepository.findByUserRole(UserRoleEnum.ADMIN);
        if (adminRole == null) {
            adminRole = new UserRoleEntity();
            adminRole.setUserRole(UserRoleEnum.ADMIN);
            this.rolesRepository.save(adminRole);
        }

    }

    @Transactional
    public void createUserRoleIfNotFound() {
        UserRoleEntity userRole = rolesRepository.findByUserRole(UserRoleEnum.USER);
        if (userRole == null) {
            userRole = new UserRoleEntity();
            userRole.setUserRole(UserRoleEnum.USER);
            this.rolesRepository.save(userRole);
        }

    }

    @Transactional
    public void addPictureDataIfNotFound() {
        if (this.pictureRepository.count() == 0) {
            PictureEntity[] pictures = getPictures();

            this.pictureRepository.saveAll(Arrays.asList(pictures));
        }


    }

   public PictureEntity[] getPictures() {
        PictureEntity firstPicture = new PictureEntity();
        firstPicture.setUrl("https://www.dropbox.com/scl/fi/2zlun9r5vfrgwm0l31cit/Train_wreck_at_Montparnasse_1895.jpg?rlkey=bc3oxzi1m7raawbpjx6pwpnrs&dl=0&raw=1");
        firstPicture.setDescription("The Montparnasse derailment occurred at 16:00 on 22 October 1895 when the Granville–Paris Express overran the buffer stop at its Gare Montparnasse terminus.");
        firstPicture.setLatitude(48.84);
        firstPicture.setLongitude(2.3186);
        firstPicture.setYear(1895);

        PictureEntity secondPicture = new PictureEntity();
        secondPicture.setUrl("https://www.dropbox.com/scl/fi/29n6wlrj54i8uit7epyuo/First_Flight.jpg?rlkey=a4klpu6e98btfawtc6t23l0mn&dl=0&raw=1");
        secondPicture.setDescription("Historic photo of the Wright brothers' third test glider being launched at Kill Devil Hills, North Carolina, on October 10, 1902. Wilbur Wright is at the controls, Orville Wright is at left, and Dan Tate (a local resident and friend of the Wright brothers) is at right.");
        secondPicture.setLatitude(36.0307);
        secondPicture.setLongitude(-75.676);
        secondPicture.setYear(1902);

        PictureEntity thirdPicture = new PictureEntity();
        thirdPicture.setUrl("https://www.dropbox.com/scl/fi/r4k6eo9ke6t4jy5fzqt1a/Bbwall.jpg?rlkey=ype1vbo4p11g8js7i04inmhcf&dl=0&raw=1");
        thirdPicture.setDescription("The Fall of the Berlin Wall, 1989.");
        thirdPicture.setLatitude(52.5162);
        thirdPicture.setLongitude(13.3777);
        thirdPicture.setYear(1989);

        PictureEntity fourthPicture = new PictureEntity();
        fourthPicture.setUrl("https://www.dropbox.com/scl/fi/uktgc3lq5amyp1inl58tv/Archduke.jpg?rlkey=99r89zrlab8a8q3ylsdv44njf&dl=0&raw=1");
        fourthPicture.setDescription("Franz Ferdinand and his wife Sophie leave the Sarajevo Guildhall after reading a speech on June 28 1914. They were assassinated five minutes later.");
        fourthPicture.setLatitude(43.8589);
        fourthPicture.setLongitude(18.4338);
        fourthPicture.setYear(1914);

        PictureEntity fifthPicture = new PictureEntity();
        fifthPicture.setUrl("https://www.dropbox.com/scl/fi/em76d9ewpkdapuphnigpu/DogProtest.jpg?rlkey=vom9k7rk0oa1xxlz2o8o5mpvi&dl=0&raw=1");
        fifthPicture.setDescription("A dog protest against Brexit demanding another 'wooferendum' - the protestors and their owners gather in Waterloo Place with the Duke of York column in the background.");
        fifthPicture.setLatitude(51.5051);
        fifthPicture.setLongitude(-0.115);
        fifthPicture.setYear(2018);

        return new PictureEntity[] {
                firstPicture,
                secondPicture,
                thirdPicture,
                fourthPicture,
                fifthPicture
        };
    }

}
