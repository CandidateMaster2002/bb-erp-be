package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.Category;
import com.boltblazers.erp.leads.CategoryRepository;
import com.boltblazers.erp.leads.dto.CategoryGroupResponse;
import com.boltblazers.erp.leads.dto.CategoryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ========== Category Groups (parents) ==========

    public List<CategoryGroupResponse> getAllGroups(String categoryType) {
        String type = (categoryType != null) ? categoryType : "LEAD";
        List<Category> parents = categoryRepository.findAll().stream()
                .filter(c -> c.getParent() == null && type.equals(c.getCategoryType()))
                .collect(Collectors.toList());

        return parents.stream().map(this::toGroupResponse).collect(Collectors.toList());
    }

    public CategoryGroupResponse getGroup(Long id) {
        Category parent = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category group not found: " + id));
        if (parent.getParent() != null) {
            throw new RuntimeException("ID " + id + " is a value, not a group");
        }
        return toGroupResponse(parent);
    }

    public CategoryGroupResponse createGroup(CategoryRequest request) {
        Category group = new Category();
        group.setName(request.getName().trim());
        if (request.getCategoryType() != null) {
            group.setCategoryType(request.getCategoryType());
        }
        categoryRepository.save(group);
        return toGroupResponse(group);
    }

    public CategoryGroupResponse updateGroup(Long id, CategoryRequest request) {
        Category group = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category group not found: " + id));
        if (group.getParent() != null) {
            throw new RuntimeException("ID " + id + " is a value, not a group");
        }
        group.setName(request.getName().trim());
        categoryRepository.save(group);
        return toGroupResponse(group);
    }

    public void deleteGroup(Long id) {
        Category group = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category group not found: " + id));
        if (group.getParent() != null) {
            throw new RuntimeException("ID " + id + " is a value, not a group. Use deleteValue instead.");
        }
        categoryRepository.delete(group); // cascades to children
    }

    // ========== Category Values (children) ==========

    public CategoryGroupResponse.CategoryValueResponse addValue(Long groupId, CategoryRequest request) {
        Category group = categoryRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Category group not found: " + groupId));
        if (group.getParent() != null) {
            throw new RuntimeException("ID " + groupId + " is a value, not a group");
        }

        Category value = new Category();
        value.setName(request.getName().trim());
        value.setParent(group);
        value.setCategoryType(group.getCategoryType());
        categoryRepository.save(value);

        CategoryGroupResponse.CategoryValueResponse res = new CategoryGroupResponse.CategoryValueResponse();
        res.setId(value.getId());
        res.setName(value.getName());
        return res;
    }

    public CategoryGroupResponse.CategoryValueResponse updateValue(Long valueId, CategoryRequest request) {
        Category value = categoryRepository.findById(valueId)
                .orElseThrow(() -> new RuntimeException("Category value not found: " + valueId));
        if (value.getParent() == null) {
            throw new RuntimeException("ID " + valueId + " is a group, not a value. Use updateGroup instead.");
        }
        value.setName(request.getName().trim());
        categoryRepository.save(value);

        CategoryGroupResponse.CategoryValueResponse res = new CategoryGroupResponse.CategoryValueResponse();
        res.setId(value.getId());
        res.setName(value.getName());
        return res;
    }

    public void deleteValue(Long valueId) {
        Category value = categoryRepository.findById(valueId)
                .orElseThrow(() -> new RuntimeException("Category value not found: " + valueId));
        if (value.getParent() == null) {
            throw new RuntimeException("ID " + valueId + " is a group, not a value. Use deleteGroup instead.");
        }
        categoryRepository.delete(value);
    }

    // ========== Mapping ==========

    private CategoryGroupResponse toGroupResponse(Category group) {
        CategoryGroupResponse res = new CategoryGroupResponse();
        res.setId(group.getId());
        res.setName(group.getName());
        res.setCategoryType(group.getCategoryType());

        List<CategoryGroupResponse.CategoryValueResponse> values = group.getChildren().stream()
                .map(child -> {
                    CategoryGroupResponse.CategoryValueResponse v = new CategoryGroupResponse.CategoryValueResponse();
                    v.setId(child.getId());
                    v.setName(child.getName());
                    return v;
                })
                .collect(Collectors.toList());
        res.setValues(values);

        return res;
    }
}
