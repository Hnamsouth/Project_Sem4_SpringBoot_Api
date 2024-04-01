package com.example.project_sem4_springboot_api;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EPermission;
import com.example.project_sem4_springboot_api.entities.enums.ERole;
import com.example.project_sem4_springboot_api.repositories.*;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.sql.Date;

import static com.example.project_sem4_springboot_api.seedding.dataSeeding.*;

@Component
@RequiredArgsConstructor
@Service
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private  final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    // school
    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final RoomRepository   roomRepository;

    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;

    @PostConstruct
    public void initializeData()  {
        createRolePermission();
        createSchoolInfo();
        createTeacher();
        createStudents();
    }
    private void createRolePermission(){
        // Create Permission
        if(permissionRepository.findAll().isEmpty()){
            for (EPermission permission : EPermission.values()) {
                permissionRepository.save(new Permission(permission));
            }
            System.out.println("Created permission");
        }
        // Create Role
        if(roleRepository.findAll().isEmpty()){
            for (ERole role : ERole.values()) {
                roleRepository.save(new Role(role));
            }
            System.out.println("Created role");
        }
        // create role - permission
        if(roleRepository.findAll().isEmpty()){
            roleRepository.findAll().forEach(role ->{
                if(role.getPermission().size()==0){
                    List<Permission> lp = permissionRepository.findAll().stream()
                            .filter(p -> p.getName().toString().substring(0,3).contains(role.getName().toString().substring(5)))
                            .toList();
                    role.setPermission(lp);
                    roleRepository.save(role);
                }
            });
            System.out.println("Created role & permission");
        }
    }
    private void createSchoolInfo(){
        /*
         *  Tổ chức khai giảng vào ngày 05 tháng 9 năm 2022.
         *  Kết thúc học kỳ I trước ngày 15 tháng 01 năm 2023,
         *  hoàn thành kế hoạch giáo dục học kỳ II trước ngày 25 tháng 5 năm 2023
         *  kết thúc năm học trước ngày 31 tháng 5 năm 2023
         * có 35 tuần thực học (học kỳ I có 18 tuần, học kỳ II có 17 tuần).
         *   */
        // create school year
        if(schoolYearRepository.findAll().isEmpty()){
            schoolYearRepository.save(
                    SchoolYear.builder()
                            .startSem1(Date.valueOf("2023-09-05"))
                            .startSem2(Date.valueOf("2024-01-15"))
                            .end(Date.valueOf("2024-05-31"))
                            .build()
            );
            System.out.println("Created schoolYear data");
        }


        // create room
        if(roomRepository.findAll().isEmpty()){
            for(int i=1; i<=20; i++){
                roomRepository.save(
                        Room.builder()
                            .name("Phòng "+i)
                            .build()
                );
            }
            System.out.println("Created room data");
        }

        // create school year class
        /*
        * 20 lop
        * 1a - 1d ... 5a - 5d
        * */
        if(schoolYearClassRepository.findAll().isEmpty()){
            int check = 1;
            String[] var = {"A","B","C","D","E"};
            var schoolyear =  schoolYearRepository.findById((long) 1).orElseThrow();
            for(int i=1; i<=20; i++){
                schoolYearClassRepository.save(
                        SchoolYearClass.builder()
                            .className("Lớp " + check + var[check])
                            .classCode(check + var[check] )
//                          .teacher()
                            .grade(check)
                            .room(roomRepository.findById((long) i).orElseThrow())
                            .schoolYear(schoolyear)
                            .build()
                );
                check = i%4==0 ? 1 : check + 1;
            }
            System.out.println("Created schoolYearClass data");
        }


    }
    void createTeacher() {
        // create subject
        if (subjectRepository.findAll().isEmpty()){
            ListSubject.forEach((s) -> {
                Subject sj = Subject.builder()
                        .code(s.toUpperCase(Locale.ENGLISH))
                        .name(s)
                        .type("Bắt buộc")
                        .number(ListSubject.indexOf(s))
                        .build();
                subjectRepository.save(sj);
            });
            System.out.println("Created subject data");
        }
        if(teacherRepository.findAll().isEmpty()){
            createUser("teacher",20);
            int check = 1;
            for(int i=1;i<=20;i++){
                // create user
                User user = userRepository.findByUsernameCustom("teacher"+i);
                List<Subject> subjects = subjectRepository.findAllByIdInSubject( List.of(check*2L,check*2L-1L));
                // create teacher & add subjects
                String sortName =user.getUserDetaill().isGender() ? "Mr." +  user.getUserDetaill().getLastname(): "Mrs." +  user.getUserDetaill().getLastname();
                Teacher teacher = Teacher.builder()
                        .officerNumber("GV"+i)
                        .joiningDate(new Date(System.currentTimeMillis()))
                        .active(true)
                        .sortName(sortName)
                        .user(user)
                        .subjects(subjects)
                        .build();

                teacherRepository.save(teacher);
                check = i%5==0 ? 1 : check +1;
            }
            System.out.println("Created teacher data");
        }


    }
    public void createUser (String role,int number) {

            for(int i=1;i<=number;i++){
                User user = User.builder()
                        .username(role+i)
                        .password(passwordEncoder.encode("123456"))
                        .roles(roleRepository.findByIdIn(Set.of(1L)))
                        .status(1)
                        .createdAt(new Date(System.currentTimeMillis()))
                        .build();
                userRepository.save(user);
            }
            System.out.println("Created user "+role+" data");
            if(!role.equals("parent")){
                Faker faker = new Faker();
                for (int i=1;i<=number;i++) {
                    Name teacher = faker.name();
                    User user = userRepository.findByUsernameCustom(role + i);
                    var userDetail = UserDetail.builder()
                            .firstname(teacher.firstName())
                            .lastname(teacher.lastName())
                            .address(faker.address().fullAddress())
                            .phone(faker.phoneNumber().cellPhone())
                            .email(role+i+"@gmail.com")
                            .gender(i>10)
                            .birthday(faker.date().birthday())
                            .citizen_id(faker.address().countryCode())
                            .avatar(faker.avatar().image())
                            .user(user)
                            .build();
                    userDetailRepository.save(userDetail);
                }
                System.out.println("Created user "+role+" detail data");
            }
    }
    private void createStudents(){
        /*
        * 1 lop 30 hs 15 nam 15 nu
        * moi phu huynh co 2 con
        */
        int initStudent =20;
        List<SchoolYearClass> classes = schoolYearClassRepository.findAll();
        if(userRepository.findAllByUsernameContains("parent").isEmpty()){
            createUser("parent",classes.size()*(initStudent/2));
        }
        if(studentRepository.findAll().isEmpty()){
            List<User> userParents = userRepository.findAllByUsernameContains("parent");
            Faker faker = new Faker();
            int check = 1;
            for(int i=1 ; i <=classes.size();i++){
                Name student = faker.name();
                Parent parentS = new Parent();
                for(int J=1;J<=initStudent;J++){
                    Parent parent = Parent.builder()
                            .fullName(faker.name().fullName())
                            .phone(faker.phoneNumber().toString())
                            .address(faker.address().fullAddress())
                            .email(faker.internet().emailAddress())
                            .gender(check%2==0)
                            .user(userParents.get(check))
                            .build();
                    parentS = J%2!=0 ? parentRepository.save(parent):parentS;
                    check = J%2==0 ? check + 1 : check;
                    Student std =  Student.builder()
                            .gender(J>(initStudent/2))
                            .firstName(student.firstName())
                            .lastName(student.lastName())
                            .birthday(faker.date().birthday())
                            .address(faker.address().fullAddress())
                            .studentCode("HS"+classes.get(i-1).getClassCode()+J)
                            .parents(List.of(parentS))
                            .build();
                    studentRepository.save(std);
                    // create student year info
                    List<StudentYearInfo> studentYearInfos = List.of(
                            StudentYearInfo.builder()
                                    .sem(1).students(std)
                                    .schoolYearClass(classes.get(i-1))
                                    .build(),
                            StudentYearInfo.builder()
                                    .sem(2).students(std)
                                    .schoolYearClass(classes.get(i-1))
                                    .build()
                    );
                    studentYearInfoRepository.saveAll(studentYearInfos);
                }
            }
        }


    }

    private void createSchedule (){
        /* create schedule
        * 1 lop 1 tuan 1 tkb
        * 1 tuan 5 ngay hoc
        * 1 ngay 2 buoi hoc. sang 4 - chieu 3
        * gán bộ môn dạy của thầy cho lớp học cụ thể : vd. thầy A dạy toán cho lớp 1a,2b,3c...
        * giáo viên từ cấp trung học cơ sở trở lên chỉ dạy từ 1-2 môn  học.
        * Nhưng, giáo viên tiểu học chỉ trừ có môn
        * Âm nhạc, Mĩ Thuật, tiếng Anh và Thể dục là không dạy còn lại sẽ “ôm hết”.
        * --> gv chủ nhiệm sẽ dạy các môn chinh trừ : nhạc, mĩ thuật, anh , thể dục
        * --> các môn khác có thể trùng tiết học
        * -->
        * */


        var classes = schoolYearClassRepository.findAll();
        var teachers = teacherRepository.findAll();
        // assign teacher to class
        if(classes.get(1).getTeacher()==null){
            classes.forEach(c->{
                c.setTeacher(teachers.get(classes.indexOf(c)));
                schoolYearClassRepository.save(c);
            });
        }

        var subjects = subjectRepository.findAll();
        var rooms = roomRepository.findAll();

        int ngayhoc = 5;
        int tiethoc = 7;
        int buoihoc = 2;

        for(int nh=1;nh<=ngayhoc;nh++){
//            for(int i=1;i<=classes.size();i++){
//                for(int j=1;j<=buoihoc;j++){
                    for(int k=1;k<=tiethoc;k++){
                        Schedule schedule = Schedule.builder()
//                                .day(nh)
//                                .time(k)
//                                .schoolYearClass(classes.get(i-1))
//                                .room(rooms.get(i-1))
//                                .subject(subjects.get(i-1))
//                                .teacher(teachers.get(i-1))
                                .build();
                        // save schedule
                    }
//                }
//            }
        }




    }

}