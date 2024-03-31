package com.example.project_sem4_springboot_api.seedding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class dataSeeding {
    public static List<String> ListSubject = List.of(
            "Toán",
            "Tiếng việt",
            "Anh",
            "Lịch sử và Địa lý",
            "Đạo đức",
            "GGTC",
            "Âm nhạc",
            "Mỹ thuật",
            "Tin",
            "Tự nhiên và Xã hội",
            "HDTN"
    );

    public static Map<Integer,Map<String,Integer>> ListSubjectGrade = Map.ofEntries(
            Map.entry(1,Map.ofEntries(
                    Map.entry("Toán",105),
                    Map.entry("Tiếng việt",420),
                    Map.entry("Anh",0),
                    Map.entry("Lịch sử và Địa lý",0),
                    Map.entry("Đạo đức",0),
                    Map.entry("GGTC",70),
                    Map.entry("Âm nhạc",6),
                    Map.entry("Mỹ thuật",70),
                    Map.entry("Tin",0),
                    Map.entry("Tự nhiên và Xã hội",70),
                    Map.entry("HDTN",105)
            )),
            Map.entry(2,Map.ofEntries(
                    Map.entry("Toán",1),
                    Map.entry("Tiếng việt",2),
                    Map.entry("Anh",3),
                    Map.entry("Lịch sử và Địa lý",4),
                    Map.entry("Đạo đức",0),
                    Map.entry("GGTC",5),
                    Map.entry("Âm nhạc",6),
                    Map.entry("Mỹ thuật",70),
                    Map.entry("Tin",0),
                    Map.entry("Tự nhiên và Xã hội",70),
                    Map.entry("HDTN",105)
            )),
            Map.entry(3,Map.ofEntries(
                    Map.entry("Toán",1),
                    Map.entry("Tiếng việt",2),
                    Map.entry("Anh",3),
                    Map.entry("Lịch sử và Địa lý",4),
                    Map.entry("Đạo đức",0),
                    Map.entry("GGTC",5),
                    Map.entry("Âm nhạc",6),
                    Map.entry("Mỹ thuật",70),
                    Map.entry("Tin",70),
                    Map.entry("Tự nhiên và Xã hội",70),
                    Map.entry("HDTN",105)
            )),
            Map.entry(4,Map.ofEntries(
                    Map.entry("Toán",1),
                    Map.entry("Tiếng việt",2),
                    Map.entry("Anh",3),
                    Map.entry("Lịch sử và Địa lý",4),
                    Map.entry("Đạo đức",0),
                    Map.entry("GGTC",5),
                    Map.entry("Âm nhạc",6),
                    Map.entry("Mỹ thuật",70),
                    Map.entry("Tin",70),
                    Map.entry("Tự nhiên và Xã hội",0),
                    Map.entry("HDTN",105)
                    )
            )
    );
}
