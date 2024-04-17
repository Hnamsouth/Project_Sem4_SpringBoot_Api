package com.example.project_sem4_springboot_api;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.*;
import com.example.project_sem4_springboot_api.repositories.*;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.sql.Date;
import static com.example.project_sem4_springboot_api.seedding.dataSeeding.*;

@Component
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private  final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    // school
    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final SchoolYearSubjectRepository schoolYearSubjectRepository;
    private final SchoolYearSubjectGradeRepository schoolYearSubjectGradeRepository;
    private final RoomRepository   roomRepository;
    private final GradeRepository   gradeRepository;
    private final TeacherSchoolYearRepository   teacherSchoolYearRepository;
    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final StudentStatusRepository studentStatusRepository;
    private final StatusRepository statusRepository;

//    @Getter
    private final List<EStudentStatus> studentStatus = Arrays.stream(EStudentStatus.values()).toList();
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @PostConstruct
    public void initializeData()  {
        createRolePermission();
        createSchoolInfo();
        createStudents();
        createUser("bdht2207a",2);
//        studentStatus.forEach(e->System.out.println(e.name()+" : "+e.getName()));
//        createRoleAccount();
    }
    private void createRolePermission(){
        // Create Permission
        if(permissionRepository.findAll().isEmpty()){
            permissionRepository.saveAll(
                Arrays.stream(EPermission.values()).map(Permission::new).toList()
            );
            System.out.println("Created permission");
        }
        // Create Role
        if(roleRepository.findAll().isEmpty()){
            roleRepository.saveAll(
                Arrays.stream(ERole.values()).map(Role::new).toList()
            );
            System.out.println("Created role");
        }
        /*
        * create role - permission
        * role:
        *   phu-huynh: read:student , read_Teacher, update:student(update student service), read:schedule
        *   teacher: read:student , update:student, read:schedule , read:teacher, update:teacher
        *   bgh: all
        *   nv-van-thu: all:student,
        *   nv-tai-chinh:
        *   nv-thu-vien:
        *
        * */
        roleRepository.findAll().forEach(role ->{
            switch (role.getName()){
                case ROLE_PH -> addPermission(role,permissionRepository.findAllByNameIn(
                    List.of(
                        EPermission.READ_STUDENT,
                        EPermission.UPDATE_STUDENT,
                        EPermission.READ_SCHEDULE
                    )
                ));
                case ROLE_GV -> addPermission(role,permissionRepository.findAllByNameIn(
                    List.of(
                        EPermission.READ_STUDENT,
                        EPermission.UPDATE_STUDENT,
                        EPermission.READ_SCHEDULE,
                        EPermission.READ_TEACHER,
                        EPermission.UPDATE_TEACHER
                    )
                ));
                case ROLE_NV_VT -> addPermission(role,permissionRepository.findAllByNameIn(
                    List.of(
                        EPermission.ALL_USER,
                        EPermission.ALL_SCHEDULE,
                        EPermission.ALL_TEACHER,
                        EPermission.ALL_STUDENT
                    )
                ));
                case ROLE_BGH -> addPermission(role,permissionRepository.findAllByNameIn(
                    List.of(EPermission.values())
                ));
                default ->{}
            }
        });
    }
    private void addPermission(Role role, List<Permission> permissions){
        if(role.getPermission().isEmpty()){
            role.setPermission(permissions);
            roleRepository.save(role);
        }
    }

    /**
     * create school info
     * 1. school year
     * 2. subject
     * 3. room
     * 4. grade
     * 5. teacher
     * 6. teacher-schoolyear
     * 7. school year class
     * 8. school year subject
     * 9. school year subject grade
     * 10. teacher-schoolyear-subject-class
     * */
    private void createSchoolInfo() {
        /**
         *  Tổ chức khai giảng vào ngày 05 tháng 9 năm 2022.
         *  Kết thúc học kỳ I trước ngày 15 tháng 01 năm 2023,
         *  hoàn thành kế hoạch giáo dục học kỳ II trước ngày 25 tháng 5 năm 2023
         *  kết thúc năm học trước ngày 31 tháng 5 năm 2023
         * có 35 tuần thực học (học kỳ I có 18 tuần, học kỳ II có 17 tuần).
         **/
        // create school year
        if(schoolYearRepository.findAll().isEmpty()){
            schoolYearRepository.saveAll(List.of(
                    SchoolYear.builder()
                            .startSem1(Date.valueOf("2023-09-05"))
                            .startSem2(Date.valueOf("2024-01-15"))
                            .end(Date.valueOf("2024-05-31"))
                            .build(),
                    SchoolYear.builder()
                            .startSem1(Date.valueOf("2025-09-05"))
                            .startSem2(Date.valueOf("2026-01-15"))
                            .end(Date.valueOf("2026-05-31"))
                            .build()
                    )
            );
            System.out.println("Created schoolYear data");
        }
        //  create subject
        if (subjectRepository.findAll().isEmpty()){
            List<Subject> subjects = new ArrayList<>();
            ListSubject.forEach((name,code)->{
                subjects.add(
                    Subject.builder()
                        .code(code)
                        .name(name.substring(3))
                        .type(name.substring(0,2).contains("BB") ? ESubjectType.BAT_BUOC: ESubjectType.TU_CHON)
                        .build()
                );
            });
            subjectRepository.saveAll(subjects);
            System.out.println("Created subject data");
        }
        // create room
        if(roomRepository.findAll().isEmpty()){
            List<Room> rooms = new ArrayList<>();
            for(int i=1; i<=20; i++){
                rooms.add(
                    Room.builder().name("Phòng "+i).build()
                );
            }
            roomRepository.saveAll(rooms);
            System.out.println("Created room data");
        }
        // create grade
        if(gradeRepository.findAll().isEmpty()){
            gradeRepository.saveAll(
                Arrays.stream(EGrade.values())
                    .map((grade)->Grade.builder().name(grade).build())
                    .toList()
            );
        }
        // create teacher
        if(teacherRepository.findAll().isEmpty()){
            createUser("teacher",20);
            int check = 1;
            for(int i=1;i<=20;i++){
                // create user
                User user = userRepository.findByUsername("teacher"+i).orElseThrow();
                // create teacher
                String sortName =user.getUserDetail().get(0).isGender() ? "Mr." +  user.getUserDetail().get(0).getLastname(): "Mrs." +  user.getUserDetail().get(0).getLastname();
                Teacher teacher = Teacher.builder()
                        .officerNumber("GV"+i)
                        .joiningDate(new Date(System.currentTimeMillis()))
                        .active(true)
                        .sortName(sortName)
                        .user(user)
                        .build();

                teacherRepository.save(teacher);
                check = i%5==0 ? 1 : check +1;
            }
            System.out.println("Created teacher data");
        }
        // teacher school year
        if(teacherSchoolYearRepository.findAll().isEmpty()){
            teacherSchoolYearRepository.saveAll(teacherRepository.findAll().stream().map((teacher)->TeacherSchoolYear.builder()
                    .schoolYear(schoolYearRepository.findAll().get(0))
                    .teacher(teacher)
                    .build()).toList());
        }
        /**
         *  create school year class
         *
         * @var classes: 15 lớp
         * @var grade: 1-5
         * @var charName: A-E
         */
        if(schoolYearClassRepository.findAll().isEmpty()){
            int classes = 15;
            int grade = 1;
            int charName = 0;
            String[] var = {"A","B","C","D","E"};
            var schoolYear = schoolYearRepository.findById((long) 1).orElseThrow();
            for(int i=1; i <= classes; i++){
                schoolYearClassRepository.save(
                        SchoolYearClass.builder()
                            .className("Lớp " + grade + var[charName])
                            .classCode("L"+grade + var[charName] )
//                            .teacherSchoolYear()
                            .grade(gradeRepository.findByName(EGrade.values()[grade-1]))
                            .room(roomRepository.findById((long) i).orElseThrow())
                            .schoolYear(schoolYear)
                            .build()
                );
                if(i%3==0){
                    grade = grade + 1;
                    charName=0;
                }else{
                    charName=charName+1;
                }
            }
            System.out.println("Created schoolYearClass data");
        }
        // create schoolyear subject: môn học trong năm
        if(schoolYearSubjectRepository.findAll().isEmpty()){
            var schoolYear = schoolYearRepository.findById((long) 1).orElseThrow();
            var subjects =  subjectRepository.findAll().stream().filter((s)->!s.getName().contains("TC-Môn tc")).toList();
            schoolYearSubjectRepository.saveAll(
                subjects.stream().map((s)-> SchoolYearSubject.builder()
                        .subject(s)
                        .schoolYear(schoolYear)
                        .build()).toList()
            );
            System.out.println("Created schoolYear subject data");
        };
        /**
         * create schoolyear subject grade: phân phối chương trình học
         *
         * @var tuần học: 35 -- k1: 18 tuần, k2: 17 tuần
         * @var số tiết học trung bình/tuần: 27
         * @var số tiết cả năm : 1225
         */
        if(schoolYearSubjectGradeRepository.findAll().isEmpty()){
            ListSubjectGrade.forEach((subject,numberOfGrade)->{
                List<SchoolYearSubjectGrade> sysg= new ArrayList<>() ;
                numberOfGrade.forEach((grade,number)->{
                    if(number>0){
                        sysg.add(SchoolYearSubjectGrade.builder()
                            .schoolYearSubject(schoolYearSubjectRepository.findBySubject_Name(subject))
                            .grade(gradeRepository.findByName(grade))
                            .number(number)
                            .sem(ESem.CA_NAM)
                            .build());
                    }
                });
                schoolYearSubjectGradeRepository.saveAll(sysg);
            });
            System.out.println("Created schoolYear subject Grade data");
        }

        /**
         * create teacher-schoolyear-subject-class: phân công giảng dạy
         * 1 giáo viên chỉ dạy 1-2 môn học
         *
         **/


    }
    public void createUser (String role,int number) {
        ERole rolename = ERole.ROLE_DEV;
        int inituser = number;
        String pw ="123456";
        switch (role){
            case "teacher" -> rolename = ERole.ROLE_GV;
            case "parent" -> rolename = ERole.ROLE_PH;
            case "bdht2207a" -> rolename = ERole.ROLE_BGH;
        }
        for(int i=1;i<=number;i++){
            if(userRepository.existsByUsername(role+i)){
                logger.warn("User "+role+i+" already exists");
                inituser--;
                continue;
            }
            User user = User.builder()
                    .username(role+i)
                    .password(passwordEncoder.encode(pw))
                    .realPassword(pw)
                    .roles(roleRepository.findAllByNameIn(List.of(rolename)))
                    .status(1)
                    .createdAt(new Date(System.currentTimeMillis()))
                    .build();
            userRepository.save(user);
        }
        if(inituser >0){
            System.out.println("Created user "+role+" data");
            Faker faker = new Faker();
            for (int i=1;i<=inituser;i++) {
                Name teacher = faker.name();
                User user = userRepository.findByUsername(role + i).orElseThrow();
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

    /**
     * creat student-schoolyear & parent
     *
     * @var initStudent: 15 học sinh/lớp
     * @var parent: 1 phụ huynh/2 học sinh
     */
    private void createStudents(){
        if(statusRepository.findAll().isEmpty()){
            statusRepository.saveAll(
                studentStatus.stream().map((status)->Status.builder()
                    .name(status.getName())
                    .code(status.name())
                    .build()).toList()
            );
        }
        int initStudent = 15;
        List<SchoolYearClass> classes = schoolYearClassRepository.findAll();
        if(userRepository.findAllByUsernameContains("parent").isEmpty()){
            createUser("parent",((classes.size()*initStudent)/2 + 2));
        }
        if(studentRepository.findAll().isEmpty()){
            List<User> userParents = userRepository.findAllByUsernameContains("parent");
            Faker faker = new Faker();
            int studentNum = 1;
            int parentUser = 0;
            var status = statusRepository.findByCode(EStudentStatus.STUDENT_DANG_HOC.name());
            for(int i=1 ; i <= classes.size();i++){
                Name student = faker.name();
                for(int J=1;J<=initStudent;J++){
                    if(studentNum%2!=0){
                        parentUser++;
                    }
                    Student std =  Student.builder()
                            .gender(J>(initStudent/2))
                            .firstName(student.firstName())
                            .lastName(student.lastName())
                            .birthday(faker.date().birthday())
                            .address(faker.address().fullAddress())
                            .studentCode("HS"+classes.get(i-1).getClassCode()+J)
                            .parents(List.of(userParents.get(parentUser)))
                            .build();
                    var newStudent = studentRepository.save(std);
                    // create StudentStatus
                    StudentStatus sts = StudentStatus.builder().student(newStudent).status(status).createdAt(new java.util.Date(System.currentTimeMillis())).description("Bắt đầu nhập học.").build();
                    studentStatusRepository.save(sts);
                    // create student year info
                    studentYearInfoRepository.save(StudentYearInfo.builder()
                            .students(newStudent)
                            .schoolYearClass(classes.get(i-1))
                            .build());
                    studentNum++;
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

        /*
        * create : teacher-schoolyear-subject-class
        *   get: schoolyear-subject
        *   get: shoolyear subject grade
        * */

//        if()

//        var classes = schoolYearClassRepository.findAll();
//        var teachers = teacherRepository.findAll();
//        // assign teacher to class
//        if(classes.get(1).getTeacher()==null){
//            classes.forEach(c->{
//                c.setTeacher(teachers.get(classes.indexOf(c)));
//                schoolYearClassRepository.save(c);
//            });
//        }
//
//        var subjects = subjectRepository.findAll();
//        var rooms = roomRepository.findAll();
//
//        int ngayhoc = 5;
//        int tiethoc = 7;
//        int buoihoc = 2;
//
//        for(int nh=1;nh<=ngayhoc;nh++){
//            for(int i=1;i<=classes.size();i++){
//                for(int j=1;j<=buoihoc;j++){
//                    for(int k=1;k<=tiethoc;k++){
//                        Schedule schedule = Schedule.builder()
//                                .day(nh)
//                                .time(k)
//                                .schoolYearClass(classes.get(i-1))
//                                .room(rooms.get(i-1))
//                                .subject(subjects.get(i-1))
//                                .teacher(teachers.get(i-1))
//                                .build();
//                        // save schedule
//                    }
//                }
//            }
//        }
    }

    public void createRoleAccount(){

    }

}