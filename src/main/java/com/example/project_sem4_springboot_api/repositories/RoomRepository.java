package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>
{
}
