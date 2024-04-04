package com.example.project_sem4_springboot_api.seedding;

import com.example.project_sem4_springboot_api.entities.enums.EGrade;

import java.util.List;
import java.util.Map;

public class dataSeeding {
    public static List<String> ListSubject = List.of(
            "BB-Toán",
            "BB-Tiếng việt",
            "BB-Anh",
            "BB-Lịch sử và Địa lý",
            "BB-Đạo đức",
            "BB-GGTC",
            "BB-Âm nhạc",
            "BB-Mỹ thuật",
            "BB-Tin",
            "BB-Tự nhiên và Xã hội",
            "BB-Hoạt động trải nghiệm",
            "TC-Hoạt động tập thể",
            "TC-Nghệ thuật tăng cường",
            "TC-Tiếng dân tộc",
            "TC-Hướng dẫn học",
            "TC-Môn tc 2",
            "TC-Môn tc 3",
            "TC-Môn tc 4"
    );
    /*
    * k1: 945 - 27/tuan/
    * k5: 1015 29/tuan
    * */
    public static Map<String,Map<EGrade,Integer>> ListSubjectGrade = Map.ofEntries(
            Map.entry("Toán",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,105),
                    Map.entry(EGrade.KHOI_2,175),
                    Map.entry(EGrade.KHOI_3,175),
                    Map.entry(EGrade.KHOI_4,175),
                    Map.entry(EGrade.KHOI_5,175)
            )),
            Map.entry("Tiếng việt",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,420),
                    Map.entry(EGrade.KHOI_2,350),
                    Map.entry(EGrade.KHOI_3,245),
                    Map.entry(EGrade.KHOI_4,245),
                    Map.entry(EGrade.KHOI_5,245)
            )),
            Map.entry("Anh",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,70),
                    Map.entry(EGrade.KHOI_2,70),
                    Map.entry(EGrade.KHOI_3,210),
                    Map.entry(EGrade.KHOI_4,210),
                    Map.entry(EGrade.KHOI_5,210)
            )),
            Map.entry("Lịch sử và Địa lý",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,0),
                    Map.entry(EGrade.KHOI_2,0),
                    Map.entry(EGrade.KHOI_3,0),
                    Map.entry(EGrade.KHOI_4,70),
                    Map.entry(EGrade.KHOI_5,70)
            )),
            Map.entry("Đạo đức",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,35),
                    Map.entry(EGrade.KHOI_5,35)
            )),
            Map.entry("Giáo dục thể chất",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,70),
                    Map.entry(EGrade.KHOI_2,70),
                    Map.entry(EGrade.KHOI_3,70),
                    Map.entry(EGrade.KHOI_4,70),
                    Map.entry(EGrade.KHOI_5,70)
            )),
            Map.entry("Âm nhạc",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,35),
                    Map.entry(EGrade.KHOI_5,35)
            )),
            Map.entry("Mỹ thuật",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,35),
                    Map.entry(EGrade.KHOI_5,35)
            )),
            Map.entry("Tin",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,0),
                    Map.entry(EGrade.KHOI_2,0),
                    Map.entry(EGrade.KHOI_3,70),
                    Map.entry(EGrade.KHOI_4,70),
                    Map.entry(EGrade.KHOI_5,70)
            )),
            Map.entry("Tự nhiên và Xã hội",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,70),
                    Map.entry(EGrade.KHOI_2,70),
                    Map.entry(EGrade.KHOI_3,70),
                    Map.entry(EGrade.KHOI_4,0),
                    Map.entry(EGrade.KHOI_5,0)
            )),
            Map.entry("Hoạt động trải nghiệm",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,105),
                    Map.entry(EGrade.KHOI_2,105),
                    Map.entry(EGrade.KHOI_3,105),
                    Map.entry(EGrade.KHOI_4,105),
                    Map.entry(EGrade.KHOI_5,105)
            )),
            Map.entry("Hoạt động tập thể",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,35),
                    Map.entry(EGrade.KHOI_5,35)
            )),
            Map.entry("Nghệ thuật tăng cường",Map.ofEntries(
                    Map.entry(EGrade.KHOI_1,35),
                    Map.entry(EGrade.KHOI_2,35),
                    Map.entry(EGrade.KHOI_3,35),
                    Map.entry(EGrade.KHOI_4,0),
                    Map.entry(EGrade.KHOI_5,0)
            ))
    );
}
