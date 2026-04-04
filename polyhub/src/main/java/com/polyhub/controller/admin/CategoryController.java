package com.polyhub.controller.admin;

import com.polyhub.entity.Category;
import com.polyhub.entity.Tag;
import com.polyhub.repository.CategoryRepository;
import com.polyhub.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    // Xem danh sách ngành học và tags
    @GetMapping
    public String index(Model model) {
        List<Category> categories = categoryRepository.findAll();
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        return "admin/categories";
    }

    // --- PHẦN NGÀNH HỌC ---

    // Thêm mới chuyên ngành
    @PostMapping("/add")
    public String addCategory(@RequestParam("id") String id, 
                              @RequestParam("name") String name, 
                              @RequestParam(value = "active", required = false) Boolean active,
                              RedirectAttributes redirectAttributes) {
        if(categoryRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("error", "Mã ngành đã tồn tại!");
            return "redirect:/admin/categories";
        }
        Category category = new Category();
        category.setId(id.toUpperCase());
        category.setName(name);
        category.setActive(active != null); // Nếu checkbox check -> true
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("success", "Thêm ngành thành công!");
        redirectAttributes.addFlashAttribute("activeTab", "majors");
        return "redirect:/admin/categories";
    }

    // Chỉnh sửa chuyên ngành
    @PostMapping("/update")
    public String updateCategory(@RequestParam("id") String id, 
                                 @RequestParam("name") String name, 
                                 RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null) {
            category.setName(name);
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("success", "Cập nhật ngành thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy ngành này!");
        }
        redirectAttributes.addFlashAttribute("activeTab", "majors");
        return "redirect:/admin/categories";
    }

    // Đổi trạng thái Ẩn/Hiện (bật/tắt)
    @PostMapping("/toggle/{id}")
    public String toggleActive(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null) {
            category.setActive(!category.getActive());
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("success", "Đã thay đổi trạng thái của ngành " + category.getId());
        }
        redirectAttributes.addFlashAttribute("activeTab", "majors");
        return "redirect:/admin/categories";
    }

    // --- PHẦN QUẢN LÝ TAGS ---

    // Thêm Tag
    @PostMapping("/tags/add")
    public String addTag(@RequestParam("name") String name, RedirectAttributes redirectAttributes) {
        String tagName = name.trim().replaceAll("^#+", ""); // Loại bỏ dấu # nếu admin có gõ vào
        if (tagName.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tên tag không được để trống!");
            return "redirect:/admin/categories";
        }
        
        if (tagRepository.findByNameIgnoreCase(tagName).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Tag #" + tagName + " đã tồn tại!");
        } else {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setIsSystem(true); // Admin tạo thì đánh dấu là System tạo
            tag.setUsageCount(0);
            tagRepository.save(tag);
            redirectAttributes.addFlashAttribute("success", "Thêm tag #" + tagName + " thành công!");
        }
        redirectAttributes.addFlashAttribute("activeTab", "tags");
        return "redirect:/admin/categories";
    }

    // Cập nhật tên Tag
    @PostMapping("/tags/update")
    public String updateTag(@RequestParam("id") Long id, @RequestParam("name") String name, RedirectAttributes redirectAttributes) {
        String tagName = name.trim().replaceAll("^#+", "");
        if (tagName.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tên tag không hợp lệ!");
            return "redirect:/admin/categories";
        }

        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag != null) {
            // Kiểm tra xem tên mới có trùng với tag khác không
            Tag existing = tagRepository.findByNameIgnoreCase(tagName).orElse(null);
            if (existing != null && !existing.getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Tên tag mới #" + tagName + " bị trùng với một tag khác!");
            } else {
                tag.setName(tagName);
                tagRepository.save(tag);
                redirectAttributes.addFlashAttribute("success", "Cập nhật tag thành công!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy tag cần sửa!");
        }
        redirectAttributes.addFlashAttribute("activeTab", "tags");
        return "redirect:/admin/categories";
    }

    // Xóa Tag
    @PostMapping("/tags/delete/{id}")
    public String deleteTag(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if(tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa Tag thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy Tag cần xóa!");
        }
        redirectAttributes.addFlashAttribute("activeTab", "tags");
        return "redirect:/admin/categories";
    }
}

