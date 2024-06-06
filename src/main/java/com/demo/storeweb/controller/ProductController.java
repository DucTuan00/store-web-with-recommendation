package com.demo.storeweb.controller;

import com.demo.storeweb.dto.ProductDTO;
import com.demo.storeweb.model.Product;
import com.demo.storeweb.service.ProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String showProducts(Model model,HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("productDTO", productDTO);
        return "create-product";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDTO productDTO, BindingResult result) {
        if(productDTO.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDTO", "imageFile", "Yêu cầu thêm ảnh"));
        }

        if(result.hasErrors()) {
            return "create-product";
        }

        productService.createProduct(productDTO);

        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);

            ProductDTO productDTO = productService.showDataInEditPage(id);

            model.addAttribute("productDTO", productDTO);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/products";
        }
        return "edit-product";
    }

    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id,
                                @Valid @ModelAttribute ProductDTO productDTO,
                                BindingResult result) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);

            if(result.hasErrors()) {
                return "edit-product";
            }

            productService.updateProduct(id, productDTO);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {
        try {
            productService.deleteProduct(id);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return "redirect:/products";
    }
}
