package com.speedcubers.speedcubing.controller;

import com.speedcubers.speedcubing.entity.Category;
import com.speedcubers.speedcubing.repository.CategoryRepository;
import com.speedcubers.speedcubing.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }

    @PostMapping("/categories")
    public String addCategory(@RequestParam String name) {
        categoryRepository.save(Category.builder().name(name).build());
        return "redirect:/categories";
    }

    @PostMapping("/categories/{id}/delete")
    @Transactional
    public String deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            registrationRepository.deleteByCategoryId(id);
            category.getRounds().size();
            categoryRepository.delete(category);
        }
        return "redirect:/categories";
    }
}
