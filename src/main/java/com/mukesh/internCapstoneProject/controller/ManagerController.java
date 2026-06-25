package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.response.FetchInternsResponseDTO;
import com.mukesh.internCapstoneProject.global.ApiResponse;
import com.mukesh.internCapstoneProject.service.ManagerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
@Tag(name = "Manager related APIs")
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/interns")
    public ResponseEntity<ApiResponse<List<FetchInternsResponseDTO>>> fetchInternsDetails(@RequestParam(name = "page") Integer page) {
        List<FetchInternsResponseDTO> response = managerService.fetchAllInterns(page);
        return ApiResponse.success(
                HttpStatus.OK,
                "Fetched all the interns under requested Manager.",
                response
        );
    }
}
