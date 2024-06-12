package com.demo.storeweb.controller;

import com.demo.storeweb.model.Product;
import com.demo.storeweb.model.User;
import com.demo.storeweb.model.UserOrder;
import com.demo.storeweb.service.UserOrderService;
import com.demo.storeweb.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserOrderService orderService;

    private static Long tempID = null;
    
    @GetMapping("/")
    public String showHomePage(Model model, HttpSession session) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "home";
    }

    @GetMapping("/order")
    public String order(@RequestParam int productId, Model model, HttpSession session) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "order";
    }

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<Void> placeOrder(
            @RequestParam Integer productId,
            @RequestParam String phone,
            @RequestParam String address,
            HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserOrder order = orderService.placeOrder(productId, loggedInUser, phone, address);
        session.setAttribute("orderID", order.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rate")
    @ResponseBody
    public ResponseEntity<Void> rateProduct(
            @RequestParam int rating,
            HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Handle the rating logic here, e.g., saving the rating information.
        orderService.rateOrder((Long)session.getAttribute("orderID"), rating);
        session.removeAttribute("orderID");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        return "home";
    }
}
