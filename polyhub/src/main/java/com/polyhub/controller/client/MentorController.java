package com.polyhub.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.polyhub.service.CategoryService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MentorController {
    
    private final CategoryService categoryService;

    @GetMapping("/mentors")
    public String index(Model model) {
        model.addAttribute("categories", categoryService.getActiveCategoriesForDropdown());
        return "client/mentors"; // Mở file src/main/resources/templates/client/mentors.html
    }
}
