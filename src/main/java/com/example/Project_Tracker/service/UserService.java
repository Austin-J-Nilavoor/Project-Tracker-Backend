package com.example.Project_Tracker.service;

import com.example.Project_Tracker.dto.UserDto;
import com.example.Project_Tracker.entity.Ibu;
import com.example.Project_Tracker.entity.User;
import com.example.Project_Tracker.repository.IbuRepository;
import com.example.Project_Tracker.repository.UserRepository;
import com.example.Project_Tracker.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final IbuRepository ibuRepository;
    private final MapperUtils mapperUtils;

    // ✅ Get all users
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ✅ Get single user by ID
    public UserDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    // ✅ Update existing user
    public UserDto updateUser(String id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setRole(userDto.getRole());

        if (userDto.getIbuId() != null) {
            Ibu ibu = ibuRepository.findById(userDto.getIbuId())
                    .orElseThrow(() -> new RuntimeException("IBU not found"));
            existingUser.setIbu(ibu);
        }

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    // ✅ Delete user
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    // ✅ Convert Entity → DTO
    private UserDto convertToDto(User user) {
        UserDto dto = mapperUtils.map(user, UserDto.class);

        if (user.getIbu() != null) {
            dto.setIbuId(user.getIbu().getId());
            dto.setIbuName(user.getIbu().getName());
        } else {
            dto.setIbuId(null);
            dto.setIbuName(null);
        }

        return dto;
    }
}
